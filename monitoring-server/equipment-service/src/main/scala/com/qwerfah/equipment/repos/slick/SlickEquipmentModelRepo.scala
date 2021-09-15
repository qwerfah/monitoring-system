package com.qwerfah.equipment.repos.slick

import slick.dbio._
import slick.jdbc.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

import com.qwerfah.equipment.repos._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._

class SlickEquipmentModelRepo(implicit val context: DataContext)
  extends EquipmentModelRepo[DBIO] {
    import context.jdbcProfile.api._

    override def get: DBIO[Seq[EquipmentModel]] = context.models.result

    override def getById(id: Int): DBIO[Option[EquipmentModel]] =
        context.models.filter(_.id === id).result.headOption

    override def getByGuid(uid: Uid): DBIO[Option[EquipmentModel]] =
        context.models.filter(_.uid === uid).result.headOption

    override def add(model: EquipmentModel): DBIO[EquipmentModel] =
        (context.models returning context.models.map(_.id)
            into ((model, id) => model.copy(id = Some(id)))) += model

    override def update(model: EquipmentModel): DBIO[Int] = {
        val targetRows = context.models.filter(_.uid === model.uid)
        for {
            booleanOption <- targetRows.result.headOption
            updateActionOption = booleanOption.map(b =>
                targetRows.update(model.copy(id = b.id))
            )
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeById(id: Int): DBIO[Int] =
        context.models.filter(_.id === id).delete

    override def removeByGuid(uid: Uid): DBIO[Int] =
        context.models.filter(_.uid === uid).delete

}