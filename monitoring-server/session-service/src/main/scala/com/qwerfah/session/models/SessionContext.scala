package com.qwerfah.session.models

import java.security.MessageDigest

import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext.Implicits.global

import enumeratum._

import com.typesafe.config.{Config, ConfigObject}

import io.circe.generic.auto._
import io.circe.config.syntax._

import com.qwerfah.common.models._
import com.qwerfah.common.{Uid, randomUid, hashString}
import com.qwerfah.common.resources.UserRole
import com.qwerfah.common.resources.Credentials

class SessionContext(implicit jdbcProfile: JdbcProfile, config: Config)
  extends DataContext(jdbcProfile) {
    import profile.api._

    val setup = DBIO.seq(
      // Create db schema
      users.schema.createIfNotExists,
      // Add default users
      users.exists.result flatMap { exisits =>
          if (!exisits) addUsers
          else DBIO.successful(None)
      }
    )

    private val initialUsers = Seq(
      User(
        Some(1),
        randomUid,
        "user_1",
        hashString("password_1"),
        UserRole.SystemAdmin,
        false
      ),
      User(
        Some(2),
        randomUid,
        "user_2",
        hashString("password_2"),
        UserRole.EquipmentAdmin,
        false
      ),
      User(
        Some(3),
        randomUid,
        "user_3",
        hashString("password_3"),
        UserRole.EquipmentUser,
        false
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

        DBIO.seq(users ++= initialUsers ++ services)
    }
}
