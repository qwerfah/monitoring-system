package com.qwerfah.monitoring.repos.slick

import slick.dbio._
import slick.jdbc.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

import com.qwerfah.monitoring.repos.MonitorRepo
import com.qwerfah.monitoring.models._
import com.qwerfah.common.Uid

class SlickMonitorRepo(implicit val context: MonitoringContext)
  extends MonitorRepo[DBIO] {
    import context.profile.api._

    override def get: DBIO[Seq[Monitor]] = context.monitors.result

    override def get(id: Int): DBIO[Option[Monitor]] =
        context.monitors.filter(_.id === id).result.headOption

    override def get(uid: Uid): DBIO[Option[Monitor]] =
        context.monitors.filter(_.uid === uid).result.headOption

    override def getByInstanceUid(instanceUid: Uid): DBIO[Seq[Monitor]] =
        context.monitors.filter(_.instanceUid === instanceUid).result

    override def getInstances: DBIO[Seq[Uid]] =
        context.monitors.result.map(seq => seq.map(_.instanceUid).distinct)

    override def add(monitor: Monitor): DBIO[Monitor] =
        (context.monitors returning context.monitors.map(_.id) into (
          (param, id) => param.copy(id = Some(id))
        )) += monitor

    override def update(monitor: Monitor): DBIO[Int] = {
        val targetRows = context.monitors.filter(_.uid === monitor.uid)
        for {
            old <- targetRows.result.headOption
            updateActionOption = old.map(b =>
                targetRows.update(
                  monitor.copy(id = b.id, instanceUid = b.instanceUid)
                )
            )
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeById(id: Int): DBIO[Int] =
        context.monitors.filter(_.id === id).delete

    override def removeByUid(uid: Uid): DBIO[Int] =
        context.monitors.filter(_.uid === uid).delete

}
