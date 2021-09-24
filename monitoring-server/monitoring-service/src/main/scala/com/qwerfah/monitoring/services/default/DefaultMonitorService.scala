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
import com.qwerfah.common.exceptions.NoMonitor
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
    ): F[ServiceResponse[MonitorResponse]] = ???

    override def addParam(
      param: MonitorParam
    ): F[ServiceResponse[ResponseMessage]] = ???

    override def update(
      request: MonitorRequest
    ): F[ServiceResponse[ResponseMessage]] = ???

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] = ???

    override def removeParam(
      param: MonitorParam
    ): F[ServiceResponse[ResponseMessage]] = ???
}
