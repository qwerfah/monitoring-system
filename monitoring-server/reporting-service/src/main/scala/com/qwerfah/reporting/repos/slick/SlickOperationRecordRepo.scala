package com.qwerfah.reporting.repos.slick

import java.time.LocalDateTime

import slick.dbio._
import slick.jdbc.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

import com.qwerfah.reporting.models._
import com.qwerfah.reporting.repos.OperationRecordRepo

import com.qwerfah.common.Uid
import com.qwerfah.common.http.HttpMethod

class SlickOperationRecordRepo(implicit val context: ReportingContext)
  extends OperationRecordRepo[DBIO] {
    import context.profile.api._

    override def get: DBIO[Seq[OperationRecord]] =
        context.operationRecords.result

    override def get(uid: Uid): DBIO[Option[OperationRecord]] =
        context.operationRecords.filter(_.uid === uid).result.headOption

    override def get(
      serviceId: Option[String],
      method: Option[HttpMethod],
      status: Option[Int],
      fromDate: Option[LocalDateTime],
      toDate: Option[LocalDateTime]
    ): DBIO[Seq[OperationRecord]] = {
        import context.methodMapper

        context.operationRecords
            .filterOpt(serviceId) { case (table, id) => table.serviceId === id }
            .filterOpt(method) { case (table, m) => table.method === m }
            .filterOpt(status) { case (table, s) => table.status === s }
            .filterOpt(fromDate) { case (table, int) => table.time >= fromDate }
            .filterOpt(toDate) { case (table, int) => table.time <= toDate }
            .result
    }

    override def add(record: OperationRecord): DBIO[OperationRecord] =
        (context.operationRecords returning context.operationRecords.map(
          _.id
        ) into ((record, id) => record.copy(id = Some(id)))) += record

    override def remove(uid: Uid): DBIO[Int] =
        context.operationRecords.filter(_.uid === uid).delete

    override def remove(serviceId: String): DBIO[Int] =
        context.operationRecords.filter(_.serviceId === serviceId).delete
}
