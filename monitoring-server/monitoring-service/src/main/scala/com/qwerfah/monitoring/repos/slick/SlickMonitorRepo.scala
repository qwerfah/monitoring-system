package com.qwerfah.monitoring.repos.slick

import scala.concurrent.ExecutionContext.Implicits.global

import slick.dbio._
import slick.jdbc.JdbcProfile

import com.qwerfah.monitoring.repos.MonitorRepo
import com.qwerfah.monitoring.models._
import com.qwerfah.common.Uid

class SlickMonitorRepo(implicit val context: MonitoringContext)
  extends MonitorRepo[DBIO] {
    import context.profile.api._

    override def get: DBIO[Seq[Monitor]] =
        context.monitors.filter(!_.isDeleted).result

    override def get(id: Int): DBIO[Option[Monitor]] =
        context.monitors
            .filter(m => !m.isDeleted && m.id === id)
            .result
            .headOption

    override def get(uid: Uid): DBIO[Option[Monitor]] =
        context.monitors
            .filter(m => !m.isDeleted && m.uid === uid)
            .result
            .headOption

    override def getByInstanceUid(instanceUid: Uid): DBIO[Seq[Monitor]] =
        context.monitors
            .filter(m => !m.isDeleted && m.instanceUid === instanceUid)
            .result

    override def getInstances: DBIO[Seq[Uid]] =
        context.monitors
            .filter(!_.isDeleted)
            .result
            .map(seq => seq.map(_.instanceUid).distinct)

    override def add(monitor: Monitor): DBIO[Monitor] =
        (context.monitors returning context.monitors.map(_.id) into (
          (param, id) => param.copy(id = Some(id))
        )) += monitor

    override def update(monitor: Monitor): DBIO[Int] = {
        val targetRows =
            context.monitors.filter(m => !m.isDeleted && m.uid === monitor.uid)
        for {
            old <- targetRows.result.headOption
            affected <- old.map(b =>
                targetRows.update(
                  monitor.copy(id = b.id, instanceUid = b.instanceUid)
                )
            ) getOrElse (DBIO.successful(0))
        } yield affected
    }

    override def removeById(id: Int): DBIO[Int] = {
        val targetRows =
            context.monitors.filter(m => !m.isDeleted && m.id === id)
        for {
            booleanOption <- targetRows.result.headOption
            affected <- booleanOption
                .map(m => targetRows.update(m.copy(isDeleted = true)))
                .getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeByUid(uid: Uid): DBIO[Int] = {
        val targetRows =
            context.monitors.filter(m => !m.isDeleted && m.uid === uid)
        for {
            booleanOption <- targetRows.result.headOption
            affected <- booleanOption
                .map(m => targetRows.update(m.copy(isDeleted = true)))
                .getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeByInstanceUid(instanceUid: Uid): DBIO[Int] = {
        val targetRows = context.monitors.filter(m =>
            !m.isDeleted && m.instanceUid === instanceUid
        )
        for {
            values <- targetRows.result
            affected <- DBIO.sequence(
              values.map(m => targetRows.update(m.copy(isDeleted = true)))
            ) map { _.sum }
        } yield affected
    }

    override def restoreByUid(uid: Uid): DBIO[Int] = {
        val targetRows =
            context.monitors.filter(m => m.isDeleted && m.uid === uid)
        for {
            booleanOption <- targetRows.result.headOption
            affected <- booleanOption
                .map(m => targetRows.update(m.copy(isDeleted = false)))
                .getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def restoreByInstanceUid(instanceUid: Uid): DBIO[Int] = {
        val targetRows = context.monitors.filter(m =>
            m.isDeleted && m.instanceUid === instanceUid
        )
        for {
            values <- targetRows.result
            affected <- DBIO.sequence(
              values.map(m => targetRows.update(m.copy(isDeleted = false)))
            ) map { _.sum }
        } yield affected
    }
}
