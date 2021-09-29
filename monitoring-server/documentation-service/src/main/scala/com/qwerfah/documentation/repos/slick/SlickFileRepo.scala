package com.qwerfah.documentation.repos.slick

import slick.dbio._
import slick.jdbc.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

import com.qwerfah.documentation.repos.FileRepo
import com.qwerfah.documentation.models.DocumentationContext

import com.qwerfah.common.Uid
import com.qwerfah.common.http.HttpMethod
import com.qwerfah.documentation.models.File

class SlickFileRepo(implicit val context: DocumentationContext)
  extends FileRepo[DBIO] {
    import context.profile.api._

    override def get: DBIO[Seq[File]] = context.files.result

    override def get(uid: Uid): DBIO[Option[File]] =
        context.files.filter(_.uid === uid).result.headOption

    override def add(file: File): DBIO[File] =
        (context.files returning context.files.map(
          _.id
        ) into ((f, id) => f.copy(id = Some(id)))) += file

    override def remove(uid: Uid): DBIO[Int] =
        context.files.filter(_.uid === uid).delete
}
