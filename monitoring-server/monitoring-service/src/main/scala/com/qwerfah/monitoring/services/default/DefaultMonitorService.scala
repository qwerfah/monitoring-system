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
import com.qwerfah.common.util.Conversions._

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
        dbManager.execute(monitorRepo.get) map { _.asResponse.as200 }

    override def get(uid: Uid): F[ServiceResponse[MonitorResponse]] =
        dbManager.execute(monitorRepo.get(uid)) map {
            case Some(value) => value.asResponse.as200
            case None        => NoMonitor(uid).as404
        }

    override def getByInstanceUid(
      instanceUid: Uid
    ): F[ServiceResponse[Seq[MonitorResponse]]] =
        dbManager.execute(monitorRepo.getByInstanceUid(instanceUid)) map {
            _.asResponse.as200
        }

    override def getParams(
      uid: Uid
    ): F[ServiceResponse[Seq[ParamResponse]]] =
        dbManager.execute(monitorParamRepo.getByMonitorUid(uid)) flatMap {
            _.map(p =>
                client
                    .sendAndDecode[ParamResponse](
                      HttpMethod.Get,
                      s"/params/${p.paramUid}"
                    )
            ).sequence map { params =>
                params.foldLeft(Seq[ParamResponse]()) { (a, b) =>
                    b match {
                        case ObjectResponse(value) => a.appended(value)
                        case other                 => a
                    }
                }
            } map { _.as200 }
        }

    override def add(
      request: MonitorRequest
    ): F[ServiceResponse[MonitorResponse]] =
        dbManager.execute(monitorRepo.add(request.asMonitor)) map {
            _.asResponse.as200
        }

    override def addParam(
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
                    case 1 => MonitorParamAdded(monitorUid).as200
                    case _ =>
                        DuplicatedMonitorParam(
                          request.paramUid,
                          monitorUid
                        ).as409
                }
            case None => Monad[F].pure(NoMonitor(monitorUid).as404)
        }
    }

    override def update(
      uid: Uid,
      request: MonitorRequest
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(
          monitorRepo.update(request.asMonitor.copy(uid = uid))
        ) map {
            case 1 => MonitorUpdated(uid).as200
            case _ => NoMonitor(uid).as404
        }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(monitorRepo.removeByUid(uid)) map {
            case 1 => MonitorRemoved(uid).as200
            case _ => NoMonitor(uid).as404
        }

    override def removeParam(
      monitorUid: Uid,
      paramUid: Uid
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(
          monitorParamRepo.removeByUid(monitorUid, paramUid)
        ) map {
            case 1 => MonitorParamRemoved(monitorUid).as200
            case _ => NoMonitorParam(paramUid, monitorUid).as404
        }
}
