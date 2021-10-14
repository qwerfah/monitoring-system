package com.qwerfah.equipment.services.default

import cats.Monad
import cats.implicits._

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import com.qwerfah.equipment.repos._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.services._
import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.Mappings

import com.qwerfah.monitoring.json.{Decoders => MonitoringDecoders}
import com.qwerfah.monitoring.resources.{AddMonitorRequest, MonitorResponse}

import com.qwerfah.common.Uid
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.json.{Decoders => CommonDecoders}
import com.qwerfah.common.services.response._
import com.qwerfah.common.exceptions._
import com.qwerfah.common.util.Conversions._
import com.qwerfah.common.http.{HttpClient, HttpMethod}
import com.rabbitmq.client.RpcClient.Response

class DefaultEquipmentInstanceService[F[_]: Monad, DB[_]: Monad](
  monitorClient: HttpClient[F],
  generatorClient: HttpClient[F]
)(implicit
  modelRepo: EquipmentModelRepo[DB],
  paramRepo: ParamRepo[DB],
  instanceRepo: EquipmentInstanceRepo[DB],
  dbManager: DbManager[F, DB]
) extends EquipmentInstanceService[F] {
    import Mappings._
    import MonitoringDecoders._
    import CommonDecoders._

    override def getAll(
      modelUid: Option[Uid],
      status: Option[EquipmentStatus]
    ): F[ServiceResponse[Seq[InstanceResponse]]] =
        dbManager.execute(instanceRepo.getWithModelName(modelUid, status)) map {
            _.asResponse.as200
        }

    override def get(uid: Uid): F[ServiceResponse[InstanceResponse]] =
        dbManager.execute(instanceRepo.getByUidWithModelName(uid)) map {
            case Some(instance) => instance.asResponse.as200
            case None           => NoInstance(uid).as404
        }

    override def getMonitors(
      uid: Uid
    ): F[ServiceResponse[Seq[InstanceMonitorResponse]]] =
        dbManager.execute(instanceRepo.getByUidWithModel(uid)) flatMap {
            case Some((instance, model)) =>
                monitorClient.sendAndDecode[Seq[MonitorResponse]](
                  HttpMethod.Get,
                  s"/api/instances/$uid/monitors"
                ) map {
                    case OkResponse(monitors) =>
                        monitors.asResponse(model, instance).as200
                    case e: ErrorResponse => e
                }
            case None => Monad[F].pure(NoInstance(uid).as404)
        }

    override def getMonitors()
      : F[ServiceResponse[Seq[InstanceMonitorResponse]]] =
        monitorClient.sendAndDecode[Seq[MonitorResponse]](
          HttpMethod.Get,
          s"/api/monitors"
        ) flatMap {
            case OkResponse(monitors) =>
                monitors
                    .map(monitor =>
                        dbManager.execute(
                          instanceRepo.getByUidWithModel(monitor.instanceUid)
                        ) map {
                            case Some((instance, model)) =>
                                Some(monitor.asResponse(model, instance))
                            case None => None
                        }
                    )
                    .sequence map { _.filter(_.isDefined).map(_.get).as200 }
            case e: ErrorResponse => Monad[F].pure(e)
        }

    override def add(
      modelUid: Uid,
      request: AddInstanceRequest
    ): F[ServiceResponse[InstanceResponse]] =
        dbManager.execute(modelRepo.getByUid(modelUid)) flatMap {
            case Some(model) =>
                dbManager.execute(
                  instanceRepo.add(request.asInstance(modelUid))
                ) map {
                    _.asResponse.as201
                }
            case None => Monad[F].pure(NoModel(modelUid).as404)
        }

    override def addMonitor(
      instanceUid: Uid,
      request: AddMonitorRequest
    ): F[ServiceResponse[InstanceMonitorResponse]] =
        dbManager.execute(instanceRepo.getByUidWithModel(instanceUid)) flatMap {
            case Some((instance, model))
                if instance.status == EquipmentStatus.Active =>
                request.params
                    .map(uid =>
                        dbManager.execute(paramRepo.getByUid(uid)) map {
                            (uid, _)
                        }
                    )
                    .sequence flatMap { res =>
                    res.collect { case (uid, None) => (uid, None) } match {
                        case errors if errors.isEmpty =>
                            monitorClient.sendAndDecode[MonitorResponse](
                              HttpMethod.Post,
                              s"/api/instances/$instanceUid/monitors",
                              Some(request.asJson.toString)
                            ) map {
                                case s: SuccessResponse[MonitorResponse] =>
                                    s.result.asResponse(model, instance).as201
                                case e: ErrorResponse => e
                            }
                        case errors =>
                            Monad[F].pure(NoParams(errors.map(e => e._1)).as404)
                    }
                }
            case _ => Monad[F].pure(NoInstance(instanceUid).as404)
        }

    override def update(
      uid: Uid,
      request: UpdateInstanceRequest
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(
          instanceRepo.update(request.asInstance.copy(uid = uid))
        ) map {
            case 1 => InstanceUpdated(uid).as200
            case _ => NoInstance(uid).as404
        }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        generatorClient.sendAndDecode[ResponseMessage](
          HttpMethod.Delete,
          s"/api/instances/$uid/params/values"
        ) flatMap {
            case _: SuccessResponse[ResponseMessage] =>
                monitorClient.sendAndDecode[ResponseMessage](
                  HttpMethod.Delete,
                  s"/api/instances/$uid/monitors"
                ) flatMap {
                    case _: SuccessResponse[ResponseMessage] =>
                        dbManager.execute(instanceRepo.removeByUid(uid)) map {
                            case 1 => InstanceRemoved(uid).as200
                            case _ => NoInstance(uid).as404
                        }
                    case e: ErrorResponse =>
                        generatorClient.send(
                          HttpMethod.Patch,
                          s"/api/instances/$uid/params/values/restore"
                        ) map { _ => e }
                }
            case e: ErrorResponse => Monad[F].pure(e)
        }

    override def restore(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(instanceRepo.restoreByUid(uid)) flatMap {
            case 1 =>
                generatorClient.sendAndDecode[ResponseMessage](
                  HttpMethod.Patch,
                  s"/api/instances/$uid/params/values/restore"
                ) flatMap {
                    case s: SuccessResponse[ResponseMessage] =>
                        monitorClient.sendAndDecode[ResponseMessage](
                          HttpMethod.Patch,
                          s"/api/instances/$uid/monitors/restore"
                        ) flatMap {
                            case s: SuccessResponse[ResponseMessage] =>
                                Monad[F].pure(InstanceRestored(uid).as200)
                            case e: ErrorResponse =>
                                generatorClient.sendAndDecode[ResponseMessage](
                                  HttpMethod.Delete,
                                  s"/api/instances/$uid/params/values/restore"
                                ) map { _ => e }
                        }
                    case e: ErrorResponse =>
                        dbManager.execute(instanceRepo.removeByUid(uid)) map {
                            _ => e
                        }
                }
            case _ => Monad[F].pure(NoInstance(uid).as404)
        }
}
