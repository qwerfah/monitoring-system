package com.qwerfah.documentation.repos.slick

import slick.dbio._
import slick.jdbc.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

import com.qwerfah.documentation.models._
import com.qwerfah.documentation.repos.FileRepo

import com.qwerfah.common.Uid
import com.qwerfah.common.http.HttpMethod

class SlickFileRepo(implicit val context: DocumentationContext)
  extends FileRepo[DBIO] {
    import context.profile.api._

    override def getMeta: DBIO[Seq[FileMeta]] = context.files.result.map(
      _.map(f => FileMeta(f.id, f.uid, f.modelUid, f.filename, f.contentType))
    )

    override def getModelMeta(modelUid: Uid): DBIO[Seq[FileMeta]] =
        context.files
            .filter(_.modelUid === modelUid)
            .result
            .map(
              _.map(f =>
                  FileMeta(f.id, f.uid, f.modelUid, f.filename, f.contentType)
              )
            )

    override def get(uid: Uid): DBIO[Option[File]] =
        context.files.filter(_.uid === uid).result.headOption

    override def add(file: File): DBIO[FileMeta] =
        (context.files returning context.files.map(
          _.id
        ) into ((f, id) =>
            FileMeta(Some(id), f.uid, f.modelUid, f.filename, f.contentType)
        )) += file

    override def remove(uid: Uid): DBIO[Int] =
        context.files.filter(_.uid === uid).delete
}
