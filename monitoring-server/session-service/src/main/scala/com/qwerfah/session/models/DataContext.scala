package com.qwerfah.session.models

import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext.Implicits.global

import enumeratum._

import java.security.MessageDigest

import com.qwerfah.common.{Uid, randomUid}
import com.qwerfah.session.resources.UserRole

class DataContext(implicit jdbcProfile: JdbcProfile) extends SlickEnumSupport {
    override val profile = jdbcProfile

    import profile.api._

    final class UserTable(tag: Tag) extends Table[User](tag, "USERS") {

        /** User role enum to string mapper. */
        implicit val roleMapper =
            MappedColumnType.base[UserRole, String](
              e => e.toString,
              s => UserRole.withName(s)
            )

        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
        def uid = column[Uid]("UID", O.Unique)
        def login = column[String]("LOGIN", O.Unique, O.Length(30))
        def password = column[Array[Byte]]("PASSWORD")
        def role = column[UserRole]("USER_ROLE")
        def * = (
          id.?,
          uid,
          login,
          password,
          role
        ).<>(User.tupled, User.unapply)

        val users = TableQuery[UserTable]
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
            MessageDigest.getInstance("MD5").digest("password_1".getBytes),
            UserRole.SystemAdmin
          ),
          User(
            Some(2),
            randomUid,
            "user_2",
            MessageDigest.getInstance("MD5").digest("password_2".getBytes),
            UserRole.EquipmentAdmin
          ),
          User(
            Some(3),
            randomUid,
            "user_3",
            MessageDigest.getInstance("MD5").digest("password_3".getBytes),
            UserRole.EquipmentUser
          )
        )
    }
}
