package com.qwerfah.reporting.models

import java.time.LocalDateTime
import java.security.MessageDigest

import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext.Implicits.global

import enumeratum._

import com.typesafe.config.{Config, ConfigObject}

import io.circe.generic.auto._
import io.circe.config.syntax._

import com.qwerfah.common.{Uid, randomUid, hashString}
import com.qwerfah.common.models._
import com.qwerfah.common.resources.{UserRole, Credentials}
import _root_.com.qwerfah.common.models.DataContext
import com.qwerfah.common.http.HttpMethod

class ReportingContext(implicit jdbcProfile: JdbcProfile, config: Config)
  extends DataContext(jdbcProfile) {
    import profile.api._

    implicit lazy val methodMapper = mappedColumnTypeForEnum(HttpMethod)

    /** System services operation records table definition.
      * @param tag
      *   Table tag.
      */
    protected final class OperationRecordTable(tag: Tag)
      extends Table[OperationRecord](tag, "OPERATION_RECORDS") {
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
        def uid = column[Uid]("UUID", O.Unique)
        def serviceId = column[String]("SERVICE_ID")
        def route = column[String]("ROUTE")
        def method = column[HttpMethod]("METHOD")
        def status = column[Int]("STATUS_CODE")
        def time = column[LocalDateTime]("DESCRIPTION")

        def * = (
          id.?,
          uid,
          serviceId,
          route,
          method,
          status,
          time
        ).<>(OperationRecord.tupled, OperationRecord.unapply)
    }

    val operationRecords = TableQuery[OperationRecordTable]

    val setup = DBIO.seq(
      operationRecords.schema.++(users.schema).createIfNotExists,
      operationRecords.exists.result flatMap { exisits =>
          if (!exisits) operationRecords ++= initialRecords
          else DBIO.successful(None)
      },
      users.exists.result flatMap { exists =>
          if (!exists) addUsers
          else DBIO.successful(None)
      }
    )

    val initialRecords = Seq(
      OperationRecord(
        None,
        randomUid,
        "session",
        "login",
        HttpMethod.Post,
        200,
        LocalDateTime.now
      ),
      OperationRecord(
        None,
        randomUid,
        "equipment",
        "models",
        HttpMethod.Get,
        404,
        LocalDateTime.now
      ),
      OperationRecord(
        None,
        randomUid,
        "monitoring",
        "monitors",
        HttpMethod.Post,
        400,
        LocalDateTime.now
      ),
      OperationRecord(
        None,
        randomUid,
        "gateway",
        "login",
        HttpMethod.Post,
        401,
        LocalDateTime.now
      ),
      OperationRecord(
        None,
        randomUid,
        "session",
        "register",
        HttpMethod.Post,
        200,
        LocalDateTime.now
      )
    )

    private def addUsers = {
        val services = collection.mutable.ListBuffer[User]()

        for (
          creds <- config.getObjectList("serviceCredentials").toArray;
          cred <- creds.asInstanceOf[ConfigObject].toConfig.as[Credentials]
        )
            services += User(
              None,
              randomUid,
              cred.login,
              hashString(cred.password),
              UserRole.Service
            )

        DBIO.seq(users ++= services)
    }
}
