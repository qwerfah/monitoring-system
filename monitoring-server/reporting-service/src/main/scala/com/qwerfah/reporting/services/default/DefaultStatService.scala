package com.qwerfah.reporting.services.default

import scala.util.{Try, Success, Failure}

import cats.Monad
import cats.data.EitherT
import cats.implicits._

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import com.qwerfah.reporting.services.StatService
import com.qwerfah.reporting.repos.OperationRecordRepo
import com.qwerfah.reporting.resources.{ModelStat, ServiceStat}

import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.json.{Decoders => EquipmentDecoders}

import com.qwerfah.monitoring.resources.MonitorResponse
import com.qwerfah.monitoring.json.{Decoders => MonitoringDecoders}

import com.qwerfah.common.Uid
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.http.{HttpClient, HttpMethod}
import com.qwerfah.common.services.response.{
    ServiceResponse,
    SuccessResponse,
    ErrorResponse
}
import com.qwerfah.common.util.Conversions._

class DefaultStatService[F[_]: Monad, DB[_]: Monad](
  equipmentClient: HttpClient[F],
  monitoringClient: HttpClient[F]
)(implicit
  recordRepo: OperationRecordRepo[DB],
  dbManager: DbManager[F, DB]
) extends StatService[F] {
    import EquipmentDecoders._
    import MonitoringDecoders._

    private def getParamCount(modelUid: Uid): EitherT[F, ErrorResponse, Int] = {
        val f = equipmentClient.sendAndDecode[Seq[ParamResponse]](
          HttpMethod.Get,
          s"/api/models/$modelUid/params"
        ) map {
            case s: SuccessResponse[Seq[ParamResponse]] =>
                Right(s.result.length)
            case e: ErrorResponse => Left(e)
        }
        EitherT(f)
    }

    private def getInstanceCount(
      modelUid: Uid,
      instances: Seq[InstanceResponse]
    ): EitherT[F, ErrorResponse, (Int, Int, Int)] = {
        val modelInstances = instances.filter(_.modelUid == modelUid)
        val active = modelInstances.count(_.status == EquipmentStatus.Active)
        val inactive =
            modelInstances.count(_.status == EquipmentStatus.Inactive)
        val dec =
            modelInstances.count(_.status == EquipmentStatus.Decommissioned)
        EitherT.rightT((active, inactive, dec))
    }

    private def getTotalMonitorCount(
      modelUid: Uid,
      instances: Seq[InstanceResponse]
    ): EitherT[F, ErrorResponse, Int] = {
        val f = monitoringClient.sendAndDecode[Int](
          HttpMethod.Get,
          s"/api/monitors/count",
          Option(
            instances.filter(_.modelUid == modelUid).map(_.uid).asJson.toString
          )
        ) map {
            case s: SuccessResponse[Int] =>
                Right(s.result)
            case e: ErrorResponse => Left(e)
        }
        EitherT(f)
    }

    def getModelStat(model: ModelResponse, instances: Seq[InstanceResponse]) = {
        for {
            paramCount <- getParamCount(model.uid)
            instanceCounts <- getInstanceCount(model.uid, instances)
            monitorCount <- getTotalMonitorCount(model.uid, instances)

        } yield ModelStat(
          model.uid,
          model.name,
          paramCount,
          instanceCounts.sumAll,
          instanceCounts._1,
          instanceCounts._2,
          instanceCounts._3,
          monitorCount
        )
    }

    override def getModelStats(): F[ServiceResponse[Seq[ModelStat]]] =
        equipmentClient.sendAndDecode[Seq[ModelResponse]](
          HttpMethod.Get,
          "/api/models"
        ) flatMap {
            case s1: SuccessResponse[Seq[ModelResponse]] =>
                equipmentClient.sendAndDecode[Seq[InstanceResponse]](
                  HttpMethod.Get,
                  "/api/instances"
                ) flatMap {
                    case s2: SuccessResponse[Seq[InstanceResponse]] =>
                        s1.result
                            .map(model => getModelStat(model, s2.result))
                            .sequence
                            .value map {
                            case Left(e)      => e
                            case Right(stats) => stats.as200
                        }
                    case e: ErrorResponse => Monad[F].pure(e)
                }

            case e: ErrorResponse => Monad[F].pure(e)
        }

    override def getServiceStats(): F[ServiceResponse[Seq[ServiceStat]]] =
        dbManager.execute(recordRepo.get) map { records =>
            val services = records.map(_.serviceId).distinct
            services.map { sid =>
                {
                    val serviceRecords = records.filter(_.serviceId == sid)
                    val requestCount = serviceRecords.length
                    val successCount =
                        serviceRecords.filter(_.status < 300).length
                    val userErrorCount =
                        serviceRecords
                            .filter(r => r.status >= 400 && r.status < 500)
                            .length
                    val serverErrorCount =
                        serviceRecords.filter(_.status >= 500).length
                    val avgRequestTime = serviceRecords
                        .map(_.elapsed)
                        .sum
                        .toFloat / serviceRecords.length.toFloat
                    ServiceStat(
                      sid,
                      requestCount,
                      successCount,
                      userErrorCount,
                      serverErrorCount,
                      avgRequestTime
                    )
                }
            }.as200
        }
}
