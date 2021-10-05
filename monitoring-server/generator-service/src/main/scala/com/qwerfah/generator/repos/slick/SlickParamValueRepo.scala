package com.qwerfah.generator.repos.slick

import scala.concurrent.ExecutionContext.Implicits.global

import slick.dbio._
import slick.jdbc.JdbcProfile

import com.qwerfah.generator.repos.ParamValueRepo
import com.qwerfah.generator.models.GeneratorContext
import com.qwerfah.generator.models.ParamValue
import com.qwerfah.common.Uid
import com.qwerfah.generator.models.ParamValue
import scala.annotation.meta.param

class SlickParamValueRepo(implicit val context: GeneratorContext)
  extends ParamValueRepo[DBIO] {
    import context.profile.api._

    override def get: DBIO[Seq[ParamValue]] =
        context.paramValues.filter(!_.isDeleted).result

    override def get(uid: Uid): DBIO[Option[ParamValue]] =
        context.paramValues
            .filter(v => !v.isDeleted && v.uid === uid)
            .result
            .headOption

    override def get(
      paramUid: Option[Uid],
      instanceUid: Option[Uid]
    ): DBIO[Seq[ParamValue]] = context.paramValues
        .filter(!_.isDeleted)
        .filterOpt(paramUid) { case (table, uid) => table.paramUid === uid }
        .filterOpt(instanceUid) { case (table, uid) =>
            table.instanceUid === uid
        }
        .result

    override def getLast(
      paramUid: Uid,
      instanceUid: Uid
    ): DBIO[Option[ParamValue]] = context.paramValues
        .filter(v =>
            !v.isDeleted && v.paramUid === paramUid && v.instanceUid === instanceUid
        )
        .sortBy(_.time.desc)
        .result
        .headOption

    override def add(value: ParamValue): DBIO[ParamValue] =
        (context.paramValues returning context.paramValues.map(_.id) into (
          (pv, id) => pv.copy(id = Some(id))
        )) += value

    override def remove(uid: Uid): DBIO[Int] = {
        val targetRows =
            context.paramValues.filter(v => !v.isDeleted && v.uid === uid)
        for {
            booleanOption <- targetRows.result.headOption
            affected <- booleanOption
                .map(v => targetRows.update(v.copy(isDeleted = true)))
                .getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeByParamUid(paramUid: Uid): DBIO[Int] = {
        val targetRows = context.paramValues.filter(v =>
            !v.isDeleted && v.paramUid === paramUid
        )
        for {
            values <- targetRows.result
            affected <- DBIO.sequence(
              values.map(v => targetRows.update(v.copy(isDeleted = true)))
            ) map { _.sum }
        } yield affected
    }

    override def removeByInstanceUid(instanceUid: Uid): DBIO[Int] = {
        val targetRows = context.paramValues.filter(v =>
            !v.isDeleted && v.instanceUid === instanceUid
        )
        for {
            values <- targetRows.result
            affected <- DBIO.sequence(
              values.map(v => targetRows.update(v.copy(isDeleted = true)))
            ) map { _.sum }
        } yield affected
    }

    override def restore(uid: Uid): DBIO[Int] = {
        val targetRows =
            context.paramValues.filter(v => v.isDeleted && v.uid === uid)
        for {
            booleanOption <- targetRows.result.headOption
            affected <- booleanOption
                .map(v => targetRows.update(v.copy(isDeleted = false)))
                .getOrElse(DBIO.successful(0))
        } yield affected
    }
    override def restoreByParamUid(paramUid: Uid): DBIO[Int] = {
        val targetRows = context.paramValues.filter(v =>
            v.isDeleted && v.paramUid === paramUid
        )
        for {
            values <- targetRows.result
            affected <- DBIO.sequence(
              values.map(v => targetRows.update(v.copy(isDeleted = false)))
            ) map { _.sum }
        } yield affected
    }
    override def restoreByInstanceUid(instanceUid: Uid): DBIO[Int] = {
        val targetRows = context.paramValues.filter(v =>
            v.isDeleted && v.instanceUid === instanceUid
        )
        for {
            values <- targetRows.result
            affected <- DBIO.sequence(
              values.map(v => targetRows.update(v.copy(isDeleted = false)))
            ) map { _.sum }
        } yield affected
    }
}
