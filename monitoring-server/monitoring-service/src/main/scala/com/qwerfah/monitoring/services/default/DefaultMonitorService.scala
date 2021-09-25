package com.qwerfah.monitoring.services.default

import cats.{Monad, Applicative}
import cats.implicits._

import com.qwerfah.monitoring.services._
import com.qwerfah.monitoring.repos._
import com.qwerfah.monitoring.models._
import com.qwerfah.monitoring.resources._
import com.qwerfah.monitoring.Mappings
import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.json.Decoders
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid
import com.qwerfah.common.exceptions._
import com.qwerfah.common.http._

class DefaultMonitorService[F[_]: Monad, DB[_]: Monad](
  client: HttpClient[F]
)(implicit
  monitorRepo: MonitorRepo[DB],
  monitorParamRepo: MonitorParamRepo[DB],
  dbManager: DbManager[F, DB]
) extends MonitorService[F] {
    import Mappings._
    import Decoders._

    override def get: F[ServiceResponse[Seq[MonitorResponse]]] =
        dbManager.execute(monitorRepo.get) map { ObjectResponse(_) }

    override def get(uid: Uid): F[ServiceResponse[MonitorResponse]] =
        dbManager.execute(monitorRepo.get(uid)) map {
            case Some(value) => ObjectResponse(value)
            case None        => NotFoundResponse(NoMonitor(uid))
        }

    override def getByInstanceUid(
      instanceUid: Uid
    ): F[ServiceResponse[Seq[MonitorResponse]]] =
        dbManager.execute(monitorRepo.getByInstanceUid(instanceUid)) map {
            ObjectResponse(_)
        }

    override def getParams(
      uid: Uid
    ): F[ServiceResponse[Seq[ParamResponse]]] = {
        dbManager.execute(monitorParamRepo.getByMonitorUid(uid)) flatMap {
            _.map(p =>
                client
                    .sendAndDecode[ParamResponse](Get, s"/params/${p.paramUid}")
            ).sequence map { params =>
                params.foldLeft(Seq[ParamResponse]()) { (a, b) =>
                    b match {
                        case ObjectResponse(value) => a.appended(value)
                        case other                 => a
                    }
                }
            } map { ObjectResponse(_) }
        }
    }

    override def add(
      request: MonitorRequest
    ): F[ServiceResponse[MonitorResponse]] = {
        val monitor: Monitor = request
        dbManager.execute(monitorRepo.add(monitor)) map { ObjectResponse(_) }
    }

    override def addParam(
      monitorUid: Uid,
      param: MonitorParamRequest
    ): F[ServiceResponse[ResponseMessage]] = {
        val monitorParam: MonitorParam = param
        dbManager.execute(monitorRepo.get(monitorUid)) flatMap {
            case Some(value) =>
                dbManager.execute(
                  monitorParamRepo.add(
                    monitorParam.copy(monitorUid = monitorUid)
                  )
                ) map {
                    case 1 =>
                        ObjectResponse(MonitorParamAdded(monitorUid))
                    case _ =>
                        ConflictResponse(
                          DuplicatedMonitorParam(param.paramUid, monitorUid)
                        )
                }
            case None =>
                Monad[F].pure(NotFoundResponse(NoMonitor(monitorUid)))
        }
    }

    override def update(
      uid: Uid,
      request: MonitorRequest
    ): F[ServiceResponse[ResponseMessage]] = {
        val monitor: Monitor = request

        dbManager.execute(monitorRepo.update(monitor.copy(uid = uid))) map {
            case 1 => ObjectResponse(MonitorUpdated(uid))
            case _ => NotFoundResponse(NoMonitor(uid))
        }
    }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(monitorRepo.removeByUid(uid)) map {
            case 1 => ObjectResponse(MonitorRemoved(uid))
            case _ => NotFoundResponse(NoMonitor(uid))
        }

    override def removeParam(
      monitorUid: Uid,
      paramUid: Uid
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(
          monitorParamRepo.removeByUid(monitorUid, paramUid)
        ) map {
            case 1 => ObjectResponse(MonitorParamRemoved(monitorUid))
            case _ =>
                NotFoundResponse(
                  NoMonitorParam(paramUid, monitorUid)
                )
        }
}
