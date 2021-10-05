package com.qwerfah.reporting.models

import java.time.LocalDateTime
import java.security.MessageDigest

import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.util.Duration

import enumeratum._

import com.typesafe.config.{Config, ConfigObject}

import io.circe.generic.auto._
import io.circe.config.syntax._

import com.qwerfah.common.{Uid, randomUid, hashString}
import com.qwerfah.common.models._
import com.qwerfah.common.resources.{UserRole, Credentials}
import com.qwerfah.common.models.DataContext
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
        def userName = column[Option[String]]("USER_NAME")
        def serviceId = column[String]("SERVICE_ID")
        def route = column[String]("ROUTE")
        def method = column[HttpMethod]("METHOD")
        def status = column[Int]("STATUS_CODE")
        def elapsed = column[Long]("ELAPSED")
        def time = column[LocalDateTime]("DESCRIPTION")

        def * = (
          id.?,
          uid,
          userName,
          serviceId,
          route,
          method,
          status,
          elapsed,
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

    private val initialRecords = Seq(
      OperationRecord(
        None,
        randomUid,
        Some("user_1"),
        "session",
        "login",
        HttpMethod.Post,
        200,
        123,
        LocalDateTime.now
      ),
      OperationRecord(
        None,
        randomUid,
        Some("user_2"),
        "equipment",
        "models",
        HttpMethod.Get,
        404,
        3421,
        LocalDateTime.now
      ),
      OperationRecord(
        None,
        randomUid,
        Some("user_3"),
        "monitoring",
        "monitors",
        HttpMethod.Post,
        400,
        221,
        LocalDateTime.now
      ),
      OperationRecord(
        None,
        randomUid,
        Some("user_2"),
        "gateway",
        "login",
        HttpMethod.Post,
        401,
        55,
        LocalDateTime.now
      ),
      OperationRecord(
        None,
        randomUid,
        Some("user_3"),
        "session",
        "register",
        HttpMethod.Post,
        200,
        31,
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
              UserRole.Service,
              false
            )

        DBIO.seq(users ++= services)
    }
}
