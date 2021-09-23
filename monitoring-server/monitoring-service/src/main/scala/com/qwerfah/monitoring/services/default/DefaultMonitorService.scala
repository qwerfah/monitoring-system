package com.qwerfah.monitoring.services.default

import com.qwerfah.monitoring.services._

class DefaultMonitorService[DB[_]: Monad, F[_]: Monad](
  monitorRepo: MonitorRepo[DB],
  monitorParamRepo: MonitorParamRepo[DB],
  dbManager: DbManager[F, DB]
) extends MonitorService[F] {
    override def get: F[ServiceResponse[Seq[MonitorResponse]]] = ???

    override def get(uid: Uid): F[ServiceResponse[MonitorResponse]] = {}

    override def getParams(uid: Uid): F[ServiceResponse[Seq[MonitorParam]]] = {}

    override def add(
      request: MonitorRequest
    ): F[ServiceResponse[MonitorResponse]] = {}

    override def addParam(
      param: MonitorParam
    ): F[ServiceResponse[ResponseMessage]] = {}

    override def update(
      request: MonitorRequest
    ): F[ServiceReposponse[ResponseMessage]] = {}

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] = {}

    override def removeParam(
      param: MonitorParam
    ): F[ServiceResponse[ResponseMessage]] = {}
}
