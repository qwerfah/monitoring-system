package com.qwerfah.documentation.models

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

class DocumentationContext(implicit jdbcProfile: JdbcProfile, config: Config)
  extends DataContext(jdbcProfile) {
    import profile.api._

    protected final class FileTable(tag: Tag)
      extends Table[File](tag, "FILES") {
        def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
        def uid = column[Uid]("UUID", O.Unique)
        def filename = column[String]("FILENAME")
        def content = column[Array[Byte]]("CONTENT")

        def * = (
          id.?,
          uid,
          filename,
          content
        ).<>(File.tupled, File.unapply)
    }

    val files = TableQuery[FileTable]

    val setup = DBIO.seq(
      files.schema.++(users.schema).createIfNotExists,
      files.exists.result flatMap { exisits =>
          if (!exisits) files ++= initialFiles
          else DBIO.successful(None)
      },
      users.exists.result flatMap { exists =>
          if (!exists) addUsers
          else DBIO.successful(None)
      }
    )

    private val initialFiles = Seq(
      File(None, randomUid, "file_1.txt", "content_1".getBytes),
      File(None, randomUid, "file_2.txt", "content_2".getBytes),
      File(None, randomUid, "file_3.txt", "content_3".getBytes)
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
