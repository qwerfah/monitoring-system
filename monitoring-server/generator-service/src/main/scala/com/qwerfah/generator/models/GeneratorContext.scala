package com.qwerfah.generator.models

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
import com.qwerfah.common.models.DataContext

class MonitoringContext(implicit jdbcProfile: JdbcProfile, config: Config)
  extends DataContext(jdbcProfile) {
    import profile.api._

    /** Equipment model param values table definition.
      * @param tag
      *   Table tag.
      */
    final class ParamValueTable(tag: Tag)
      extends Table[ParamValue](tag, "PARAM_VALUES") {
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
        def uid = column[Uid]("UUID", O.Unique)
        def paramUid = column[Uid]("PARAM_UUID")
        def instanceUid = column[Uid]("INSTANCE_UUID")
        def value = column[String]("VALUE")

        def * = (
          id.?,
          uid,
          paramUid,
          instanceUid,
          value
        ).<>(ParamValue.tupled, ParamValue.unapply)
    }

    val paramValues = TableQuery[ParamValueTable]

    val setup = DBIO.seq(
      users.schema
          .++(paramValues.schema)
          .createIfNotExists,
      paramValues.exists.result flatMap { exisits =>
          if (!exisits) paramValues ++= initialParamValues
          else DBIO.successful(None)
      },
      users.exists.result flatMap { exisits =>
          if (!exisits) addUsers else DBIO.successful(None)
      }
    )

    private val initialParamValues = Seq(
      ParamValue(None, randomUid, randomUid, randomUid, "100"),
      ParamValue(None, randomUid, randomUid, randomUid, "200"),
      ParamValue(None, randomUid, randomUid, randomUid, "300")
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
