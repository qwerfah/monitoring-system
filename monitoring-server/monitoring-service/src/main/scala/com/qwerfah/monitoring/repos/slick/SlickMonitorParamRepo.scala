package com.qwerfah.monitoring.repos.slick

import slick.dbio._
import slick.jdbc.JdbcProfile

import com.qwerfah.monitoring.repos.MonitorParamRepo
import com.qwerfah.monitoring.models._
import com.qwerfah.common.Uid

class SlickMonitorParamRepo(implicit val context: MonitoringContext)
  extends MonitorParamRepo[DBIO] {
    import context.profile.api._

    override def get: DBIO[Seq[MonitorParam]] = context.monitorParams.result

    override def get(monitorParam: MonitorParam): DBIO[Option[MonitorParam]] =
        context.monitorParams
            .filter(p =>
                p.paramUid === monitorParam.paramUid &&
                p.paramUid === monitorParam.monitorUid
            )
            .result
            .headOption

    override def getByMonitorUid(monitorUid: Uid): DBIO[Seq[MonitorParam]] =
        context.monitorParams.filter(_.monitorUid === monitorUid).result

    override def getByParamUid(paramUid: Uid): DBIO[Seq[MonitorParam]] =
        context.monitorParams.filter(_.paramUid === paramUid).result

    override def add(monitorParam: MonitorParam): DBIO[Int] =
        context.monitorParams += monitorParam

    override def removeByParamUid(paramUid: Uid): DBIO[Int] =
        context.monitorParams.filter(_.paramUid === paramUid).delete

    override def removeByUid(monitorUid: Uid, paramUid: Uid): DBIO[Int] =
        context.monitorParams
            .filter(p =>
                p.paramUid === paramUid &&
                p.monitorUid === monitorUid
            )
            .delete

    override def remove(monitorParam: MonitorParam): DBIO[Int] =
        context.monitorParams
            .filter(p =>
                p.paramUid === monitorParam.paramUid &&
                p.monitorUid === monitorParam.monitorUid
            )
            .delete

}
