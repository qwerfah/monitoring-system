package com.qwerfah.session.models

import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext.Implicits.global

import enumeratum._

import java.security.MessageDigest

import com.qwerfah.common.models._
import com.qwerfah.common.{Uid, randomUid}
import com.qwerfah.common.resources.UserRole

class SessionContext(implicit jdbcProfile: JdbcProfile)
  extends DataContext(jdbcProfile) {
    import profile.api._

    val setup = DBIO.seq(
      // Create db schema
      users.schema.createIfNotExists,
      // Add default users
      users.exists.result flatMap { exisits =>
          if (!exisits) users ++= initialUsers
          else DBIO.successful(None)
      }
    )

    val initialUsers = Seq(
      User(
        Some(1),
        randomUid,
        "user_1",
        MessageDigest.getInstance("MD5").digest("password_1".getBytes("UTF-8")),
        UserRole.SystemAdmin
      ),
      User(
        Some(2),
        randomUid,
        "user_2",
        MessageDigest.getInstance("MD5").digest("password_2".getBytes("UTF-8")),
        UserRole.EquipmentAdmin
      ),
      User(
        Some(3),
        randomUid,
        "user_3",
        MessageDigest.getInstance("MD5").digest("password_3".getBytes("UTF-8")),
        UserRole.EquipmentUser
      )
    )
}
