package com.qwerfah.equipment.repos.slick

import slick.dbio._
import slick.jdbc.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

import com.qwerfah.equipment.repos._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._
import com.qwerfah.common.Uid

class SlickEquipmentModelRepo(implicit val context: EquipmentContext)
  extends EquipmentModelRepo[DBIO] {
    import context.profile.api._

    override def get: DBIO[Seq[EquipmentModel]] = context.models.result

    override def getById(id: Int): DBIO[Option[EquipmentModel]] =
        context.models
            .filter(m => !m.isDeleted && m.id === id)
            .result
            .headOption

    override def getByUid(uid: Uid): DBIO[Option[EquipmentModel]] =
        context.models
            .filter(m => !m.isDeleted && m.uid === uid)
            .result
            .headOption

    override def add(model: EquipmentModel): DBIO[EquipmentModel] =
        (context.models returning context.models.map(_.id)
            into ((model, id) => model.copy(id = Some(id)))) += model

    override def update(model: EquipmentModel): DBIO[Int] = {
        val targetRows =
            context.models.filter(m => !m.isDeleted && m.uid === model.uid)
        for {
            booleanOption <- targetRows.result.headOption
            updateActionOption = booleanOption.map(b =>
                targetRows.update(model.copy(id = b.id))
            )
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeById(id: Int): DBIO[Int] = {
        val targetRows =
            context.models.filter(m => !m.isDeleted && m.id === id)
        for {
            booleanOption <- targetRows.result.headOption
            updateActionOption = booleanOption.map(m =>
                targetRows.update(m.copy(isDeleted = true))
            )
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeByUid(uid: Uid): DBIO[Int] = {
        val targetRows =
            context.models.filter(m => !m.isDeleted && m.uid === uid)
        for {
            booleanOption <- targetRows.result.headOption
            updateActionOption = booleanOption.map(m =>
                targetRows.update(m.copy(isDeleted = true))
            )
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def restoreByUid(uid: Uid): DBIO[Int] = {
        val targetRows =
            context.models.filter(m => m.isDeleted && m.uid === uid)
        for {
            booleanOption <- targetRows.result.headOption
            updateActionOption = booleanOption.map(m =>
                targetRows.update(m.copy(isDeleted = false))
            )
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }
}
