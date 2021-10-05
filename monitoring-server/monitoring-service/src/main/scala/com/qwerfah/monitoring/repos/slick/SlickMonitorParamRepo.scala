package com.qwerfah.monitoring.repos.slick

import scala.concurrent.ExecutionContext.Implicits.global

import slick.dbio._
import slick.jdbc.JdbcProfile

import com.qwerfah.monitoring.repos.MonitorParamRepo
import com.qwerfah.monitoring.models._
import com.qwerfah.common.Uid

class SlickMonitorParamRepo(implicit val context: MonitoringContext)
  extends MonitorParamRepo[DBIO] {
    import context.profile.api._

    override def get: DBIO[Seq[MonitorParam]] =
        context.monitorParams.filter(!_.isDeleted).result

    override def getByMonitorUid(monitorUid: Uid): DBIO[Seq[MonitorParam]] =
        context.monitorParams
            .filter(p => !p.isDeleted && p.monitorUid === monitorUid)
            .result

    override def add(monitorParam: MonitorParam): DBIO[Int] =
        context.monitorParams += monitorParam

    override def removeByParamUid(paramUid: Uid): DBIO[Int] = {
        val targetRows = context.monitorParams.filter(p =>
            !p.isDeleted && p.paramUid === paramUid
        )
        for {
            values <- targetRows.result
            affected <- DBIO.sequence(
              values.map(p => targetRows.update(p.copy(isDeleted = true)))
            ) map { _.sum }
        } yield affected
    }

    override def removeByMonitorUid(monitorUid: Uid): DBIO[Int] = {
        val targetRows = context.monitorParams.filter(p =>
            !p.isDeleted && p.monitorUid === monitorUid
        )
        for {
            values <- targetRows.result
            affected <- DBIO.sequence(
              values.map(p => targetRows.update(p.copy(isDeleted = true)))
            ) map { _.sum }
        } yield affected
    }

    override def removeByUid(monitorUid: Uid, paramUid: Uid): DBIO[Int] = {
        val targetRows = context.monitorParams.filter(p =>
            !p.isDeleted && p.monitorUid === monitorUid && p.paramUid === paramUid
        )
        for {
            booleanOption <- targetRows.result.headOption
            affected <- booleanOption
                .map(p => targetRows.update(p.copy(isDeleted = true)))
                .getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def restoreByParamUid(paramUid: Uid): DBIO[Int] = {
        val targetRows = context.monitorParams.filter(p =>
            p.isDeleted && p.paramUid === paramUid
        )
        for {
            values <- targetRows.result
            affected <- DBIO.sequence(
              values.map(p => targetRows.update(p.copy(isDeleted = false)))
            ) map { _.sum }
        } yield affected
    }

    override def restoreByMonitorUid(monitorUid: Uid): DBIO[Int] = {
        val targetRows = context.monitorParams.filter(p =>
            p.isDeleted && p.monitorUid === monitorUid
        )
        for {
            values <- targetRows.result
            affected <- DBIO.sequence(
              values.map(p => targetRows.update(p.copy(isDeleted = false)))
            ) map { _.sum }
        } yield affected
    }

    override def restoreByUid(monitorUid: Uid, paramUid: Uid): DBIO[Int] = {
        val targetRows = context.monitorParams.filter(p =>
            p.isDeleted && p.monitorUid === monitorUid && p.paramUid === paramUid
        )
        for {
            booleanOption <- targetRows.result.headOption
            affected <- booleanOption
                .map(p => targetRows.update(p.copy(isDeleted = false)))
                .getOrElse(DBIO.successful(0))
        } yield affected
    }
}
