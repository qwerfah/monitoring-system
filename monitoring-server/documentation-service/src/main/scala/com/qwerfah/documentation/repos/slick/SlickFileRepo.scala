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

    override def getMeta: DBIO[Seq[FileMeta]] = context.files
        .filter(!_.isDeleted)
        .result
        .map(
          _.map(f =>
              FileMeta(f.id, f.uid, f.modelUid, f.filename, f.contentType)
          )
        )

    override def getModelMeta(modelUid: Uid): DBIO[Seq[FileMeta]] =
        context.files
            .filter(f => !f.isDeleted && f.modelUid === modelUid)
            .result
            .map(
              _.map(f =>
                  FileMeta(f.id, f.uid, f.modelUid, f.filename, f.contentType)
              )
            )

    override def get(uid: Uid): DBIO[Option[File]] =
        context.files
            .filter(f => !f.isDeleted && f.uid === uid)
            .result
            .headOption

    override def add(file: File): DBIO[FileMeta] =
        (context.files returning context.files.map(
          _.id
        ) into ((f, id) =>
            FileMeta(Some(id), f.uid, f.modelUid, f.filename, f.contentType)
        )) += file

    override def remove(uid: Uid): DBIO[Int] = {
        val targetRows =
            context.files.filter(f => !f.isDeleted && f.uid === uid)
        for {
            booleanOption <- targetRows.result.headOption
            updateActionOption = booleanOption.map(f =>
                targetRows.update(f.copy(isDeleted = true))
            )
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeByModelUid(modelUid: Uid): DBIO[Int] = {
        val targetRows =
            context.files.filter(f => !f.isDeleted && f.modelUid === modelUid)
        for {
            values <- targetRows.result
            updateActions = DBIO.sequence(
              values.map(f => targetRows.update(f.copy(isDeleted = true)))
            )
            affected <- updateActions.map { _.sum }
        } yield affected
    }

    override def restore(uid: Uid): DBIO[Int] = {
        val targetRows =
            context.files.filter(f => f.isDeleted && f.uid === uid)
        for {
            booleanOption <- targetRows.result.headOption
            updateActionOption = booleanOption.map(f =>
                targetRows.update(f.copy(isDeleted = false))
            )
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def restoreByModelUid(modelUid: Uid): DBIO[Int] = {
        val targetRows =
            context.files.filter(f => f.isDeleted && f.modelUid === modelUid)
        for {
            values <- targetRows.result
            updateActions = DBIO.sequence(
              values.map(f => targetRows.update(f.copy(isDeleted = false)))
            )
            affected <- updateActions.map { _.sum }
        } yield affected
    }

}
