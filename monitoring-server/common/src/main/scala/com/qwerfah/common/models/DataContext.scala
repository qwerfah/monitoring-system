package com.qwerfah.common.models

import slick.jdbc.JdbcProfile
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext.Implicits.global

import enumeratum._

import java.security.MessageDigest

import com.qwerfah.common.{Uid, randomUid}
import com.qwerfah.common.resources.UserRole

abstract class DataContext(jdbcProfile: JdbcProfile) extends SlickEnumSupport {
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
    }

    val users = TableQuery[UserTable]

}
