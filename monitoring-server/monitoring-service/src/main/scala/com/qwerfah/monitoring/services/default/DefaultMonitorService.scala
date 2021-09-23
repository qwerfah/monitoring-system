package com.qwerfah.monitoring.services.default

import cats.Monad
import cats.implicits._

import com.qwerfah.monitoring.services._
import com.qwerfah.monitoring.repos._
import com.qwerfah.monitoring.models._
import com.qwerfah.monitoring.resources._
import com.qwerfah.equipment.resources._
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid

class DefaultMonitorService[DB[_]: Monad, F[_]: Monad](
  monitorRepo: MonitorRepo[DB],
  monitorParamRepo: MonitorParamRepo[DB],
  dbManager: DbManager[F, DB]
) extends MonitorService[F] {
    override def get: F[ServiceResponse[Seq[MonitorResponse]]] = ???

    override def get(uid: Uid): F[ServiceResponse[MonitorResponse]] = ???

    override def getByInstanceUid(
      instanceUid: Uid
    ): F[ServiceResponse[Seq[MonitorResponse]]] = ???

    override def getParams(uid: Uid): F[ServiceResponse[Seq[ParamResponse]]] =
        ???

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
