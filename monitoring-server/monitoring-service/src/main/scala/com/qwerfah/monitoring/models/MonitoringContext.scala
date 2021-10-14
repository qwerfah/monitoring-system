package com.qwerfah.monitoring.models

import java.security.MessageDigest

import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext.Implicits.global

import enumeratum._

import com.typesafe.config.{Config, ConfigObject}

import io.circe.generic.auto._
import io.circe.config.syntax._

import com.qwerfah.common.models._
import com.qwerfah.common.models.DataContext
import com.qwerfah.common.{Uid, randomUid, hashString, uidFromString}
import com.qwerfah.common.resources.{UserRole, Credentials}

class MonitoringContext(implicit jdbcProfile: JdbcProfile, config: Config)
  extends DataContext(jdbcProfile) {
    import profile.api._

    /** Equipment instance monitors table definition.
      * @param tag
      *   Table tag.
      */
    final class MonitorTable(tag: Tag) extends Table[Monitor](tag, "MONITORS") {
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
        def uid = column[Uid]("UUID", O.Unique)
        def instanceUid = column[Uid]("INSTANCE_UUID")
        def name = column[String]("NAME")
        def description = column[Option[String]]("DESCRIPTION")
        def isDeleted = column[Boolean]("IS_DELETED")

        def * = (
          id.?,
          uid,
          instanceUid,
          name,
          description,
          isDeleted
        ).<>(Monitor.tupled, Monitor.unapply)
    }

    /** Tracked params table definition.
      * @param tag
      *   Table tag.
      */
    final class MonitorParamTable(tag: Tag)
      extends Table[MonitorParam](tag, "MONITOR_PARAMS") {
        def monitorUid = column[Uid]("MONITOR_UUID")
        def paramUid = column[Uid]("PARAM_UUID")
        def isDeleted = column[Boolean]("IS_DELETED")

        def pk = primaryKey("pk_a", (monitorUid, paramUid))

        def * = (monitorUid, paramUid, isDeleted).<>(
          MonitorParam.tupled,
          MonitorParam.unapply
        )

        def monitor = foreignKey("MONITOR_FK", monitorUid, monitors)(
          _.uid,
          ForeignKeyAction.Cascade,
          ForeignKeyAction.Cascade
        )
    }

    val monitors = TableQuery[MonitorTable]
    val monitorParams = TableQuery[MonitorParamTable]

    val setup = DBIO.seq(
      users.schema
          .++(monitors.schema)
          .++(monitorParams.schema)
          .createIfNotExists,
      monitors.exists.result flatMap { exisits =>
          if (!exisits) monitors ++= initialMonitors else DBIO.successful(None)
      },
      monitorParams.exists.result flatMap { exisits =>
          if (!exisits) monitorParams ++= initialMonitorParams
          else DBIO.successful(None)
      },
      users.exists.result flatMap { exisits =>
          if (!exisits) addUsers else DBIO.successful(None)
      }
    )

    private val monitorUids = Seq(randomUid, randomUid, randomUid)

    private val paramUids = Seq(
      uidFromString("050d4139-d59b-4abb-aa33-a6d09f062be2"),
      uidFromString("9b00e6a2-bb3d-4de0-b513-88137ab287e7"),
      uidFromString("6c552410-f586-4348-a617-0a175397733d")
    )

    private val instanceUids = Seq(
      uidFromString("c9686e98-0a32-4084-b883-7ea2f6334df0"),
      uidFromString("6c4cd202-e693-4245-8cca-6b2ff430e9b3"),
      uidFromString("a2fc3c37-3873-43d2-a662-3254632253f4")
    )

    private val initialMonitors = Seq(
      Monitor(
        Some(0),
        monitorUids(0),
        instanceUids(0),
        "monitor_1",
        Some("Description of monitor_1"),
        false
      ),
      Monitor(
        Some(1),
        monitorUids(1),
        instanceUids(1),
        "monitor_2",
        Some("Description of monitor_2"),
        false
      ),
      Monitor(
        Some(2),
        monitorUids(2),
        instanceUids(2),
        "monitor_3",
        Some("Description of monitor_3"),
        false
      )
    )

    private val initialMonitorParams = Seq(
      MonitorParam(monitorUids(0), paramUids(0), false),
      MonitorParam(monitorUids(1), paramUids(1), false),
      MonitorParam(monitorUids(2), paramUids(2), false)
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
