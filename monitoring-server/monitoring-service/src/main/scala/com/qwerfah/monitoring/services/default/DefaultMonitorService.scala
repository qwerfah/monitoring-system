package com.qwerfah.monitoring.services.default

import cats.{Monad, Applicative}
import cats.implicits._

import com.qwerfah.monitoring.services._
import com.qwerfah.monitoring.repos._
import com.qwerfah.monitoring.models._
import com.qwerfah.monitoring.resources._
import com.qwerfah.monitoring.Mappings

import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.json.{Decoders => EquipmentDecoders}

import com.qwerfah.generator.resources._
import com.qwerfah.generator.json.{Decoders => GeneratorDecoders}

import com.qwerfah.common.db.DbManager
import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid
import com.qwerfah.common.exceptions._
import com.qwerfah.common.http._
import com.qwerfah.common.util.Conversions._

class DefaultMonitorService[F[_]: Monad, DB[_]: Monad](
  equipmentClient: HttpClient[F],
  generatorClient: HttpClient[F]
)(implicit
  monitorRepo: MonitorRepo[DB],
  monitorParamRepo: MonitorParamRepo[DB],
  dbManager: DbManager[F, DB]
) extends MonitorService[F] {
    import Mappings._
    import EquipmentDecoders._
    import GeneratorDecoders._

    override def getMonitors: F[ServiceResponse[Seq[MonitorResponse]]] =
        dbManager.execute(monitorRepo.get) map { _.asResponse.as200 }

    override def getMonitor(uid: Uid): F[ServiceResponse[MonitorResponse]] =
        dbManager.execute(monitorRepo.get(uid)) map {
            case Some(value) => value.asResponse.as200
            case None        => NoMonitor(uid).as404
        }

    override def getMonitorCount(uids: Seq[Uid]): F[ServiceResponse[Int]] =
        uids.map { uid =>
            dbManager.execute(monitorRepo.getByInstanceUid(uid))
        }.sequence map { res =>
            res.flatten.length.as200
        }

    override def getInstanceMonitors(
      instanceUid: Uid
    ): F[ServiceResponse[Seq[MonitorResponse]]] =
        dbManager.execute(monitorRepo.getByInstanceUid(instanceUid)) map {
            _.asResponse.as200
        }

    override def getMonitoringInstances: F[ServiceResponse[Seq[Uid]]] =
        dbManager.execute(monitorRepo.getInstances) map { _.as200 }

    override def getMonitorParams(
      uid: Uid
    ): F[ServiceResponse[Seq[ParamResponse]]] =
        dbManager.execute(monitorRepo.get(uid)) flatMap {
            case Some(_) =>
                dbManager.execute(
                  monitorParamRepo.getByMonitorUid(uid)
                ) flatMap {
                    _.map(p =>
                        equipmentClient.sendAndDecode[ParamResponse](
                          HttpMethod.Get,
                          s"/api/params/${p.paramUid}"
                        ) map {
                            case OkResponse(value) => value
                            case e: ErrorResponse  => e
                        }
                    ).sequence map {
                        _.collect { case p: ParamResponse => p }.as200
                    }
                }
            case None => Monad[F].pure(NoMonitor(uid).as404)
        }

    override def getMonitorParamValues(
      uid: Uid
    ): F[ServiceResponse[Seq[ParamValueResponse]]] =
        dbManager.execute(monitorRepo.get(uid)) flatMap {
            case Some(monitor) =>
                dbManager.execute(
                  monitorParamRepo.getByMonitorUid(uid)
                ) flatMap {
                    _.map(p =>
                        generatorClient.sendAndDecode[Seq[ParamValueResponse]](
                          HttpMethod.Get,
                          s"/api/instances/${monitor.instanceUid}/params/${p.paramUid}/values"
                        ) map {
                            case OkResponse(value) => value
                            case _                 => Seq()
                        }
                    ).sequence map { _.flatten.as200 }
                }
            case None => Monad[F].pure(NoMonitor(uid).as404)
        }

    override def addMonitor(
      instanceUid: Uid,
      request: AddMonitorRequest
    ): F[ServiceResponse[MonitorResponse]] = {
        val monitor = request.asMonitor(instanceUid)
        val actions = Seq(
          monitorRepo.add(monitor),
          dbManager.sequence(
            request.params.map(paramUid =>
                monitorParamRepo.add(MonitorParam(monitor.uid, paramUid, false))
            )
          ) map { _ => monitor }
        )

        dbManager.executeTransactionally(dbManager.sequence(actions)) map {
            _.head.asResponse.as201
        }
    }

    override def addMonitorParam(
      monitorUid: Uid,
      request: MonitorParamRequest
    ): F[ServiceResponse[ResponseMessage]] = {
        dbManager.execute(monitorRepo.get(monitorUid)) flatMap {
            case Some(value) =>
                dbManager.execute(
                  monitorParamRepo.add(
                    request.asParam.copy(monitorUid = monitorUid)
                  )
                ) map {
                    case 1 => MonitorParamAdded(monitorUid).as201
                    case _ =>
                        DuplicatedMonitorParam(
                          request.paramUid,
                          monitorUid
                        ).as409
                }
            case None => Monad[F].pure(NoMonitor(monitorUid).as404)
        }
    }

    override def updateMonitor(
      uid: Uid,
      request: UpdateMonitorRequest
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(
          monitorRepo.update(request.asMonitor.copy(uid = uid))
        ) map {
            case 1 => MonitorUpdated(uid).as200
            case _ => NoMonitor(uid).as404
        }

    override def removeMonitor(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(monitorRepo.removeByUid(uid)) flatMap {
            case 1 =>
                dbManager.execute(
                  monitorParamRepo.removeByMonitorUid(uid)
                ) map { _ => MonitorRemoved(uid).as200 }
            case _ => Monad[F].pure(NoMonitor(uid).as404)
        }

    override def removeInstanceMonitors(
      instanceUid: Uid
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(monitorRepo.getByInstanceUid(instanceUid)) flatMap {
            case monitors if monitors.nonEmpty =>
                monitors.map { m =>
                    dbManager.execute(
                      monitorParamRepo.removeByMonitorUid(m.uid)
                    )
                }.sequence flatMap { _ =>
                    dbManager.execute(
                      monitorRepo.removeByInstanceUid(instanceUid)
                    )
                } map { _ => InstanceMonitorsRemoved(instanceUid).as200 }
            case _ => Monad[F].pure(NoInstanceMonitors(instanceUid).as404)
        }

    override def restoreMonitor(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        for {
            result <- dbManager.execute(monitorRepo.restoreByUid(uid))
            _ <- dbManager.execute(monitorParamRepo.restoreByMonitorUid(uid))
        } yield result match {
            case 1 => MonitorRestored(uid).as200
            case _ => NoMonitor(uid).as404
        }

    override def restoreInstanceMonitors(
      instanceUid: Uid
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(
          monitorRepo.restoreByInstanceUid(instanceUid)
        ) flatMap {
            case i if i > 0 =>
                dbManager.execute(
                  monitorRepo.getByInstanceUid(instanceUid)
                ) flatMap {
                    _.map { m =>
                        dbManager.execute(
                          monitorParamRepo.restoreByMonitorUid(m.uid)
                        )
                    }.sequence
                } map { _ => InstanceMonitorsRestored(instanceUid).as200 }
            case _ => Monad[F].pure(NoInstanceMonitors(instanceUid).as404)
        }

    override def removeMonitorParam(
      monitorUid: Uid,
      paramUid: Uid
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(
          monitorParamRepo.removeByUid(monitorUid, paramUid)
        ) map {
            case 1 => MonitorParamRemoved(paramUid, monitorUid).as200
            case _ => NoMonitorParam(paramUid, monitorUid).as404
        }

    override def removeMonitorParamsForMonitor(
      monitorUid: Uid
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(monitorParamRepo.removeByMonitorUid(monitorUid)) map {
            case i if i > 0 => MonitorParamsRemoved(monitorUid).as200
            case _          => NoMonitorParams(monitorUid).as404
        }

    override def removeMonitorParamsForParam(
      paramUid: Uid
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(monitorParamRepo.removeByParamUid(paramUid)) map {
            case i if i > 0 => ParamTrackingsRemoved(paramUid).as200
            case _          => NoParamTrackings(paramUid).as404
        }

    override def restoreMonitorParam(
      monitorUid: Uid,
      paramUid: Uid
    ): F[ServiceResponse[ResponseMessage]] = dbManager.execute(
      monitorParamRepo.restoreByUid(monitorUid, paramUid)
    ) map {
        case 1 => MonitorParamRemoved(paramUid, monitorUid).as200
        case _ => NoMonitorParam(paramUid, monitorUid).as404
    }

    override def restoreMonitorParamsForMonitor(
      monitorUid: Uid
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(
          monitorParamRepo.restoreByMonitorUid(monitorUid)
        ) map {
            case i if i > 0 => MonitorParamsRestored(monitorUid).as200
            case _          => NoMonitorParams(monitorUid).as404
        }

    override def restoreMonitorParamsForParam(
      paramUid: Uid
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(monitorParamRepo.restoreByParamUid(paramUid)) map {
            case i if i > 0 => ParamTrackingsRestored(paramUid).as200
            case _          => NoParamTrackings(paramUid).as404
        }
}
