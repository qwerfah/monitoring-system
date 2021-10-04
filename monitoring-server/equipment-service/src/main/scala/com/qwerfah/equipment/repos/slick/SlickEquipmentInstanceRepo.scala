package com.qwerfah.equipment.repos.slick

import slick.dbio._
import slick.jdbc.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

import com.qwerfah.equipment.repos._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._
import com.qwerfah.common.Uid

class SlickEquipmentInstanceRepo(implicit val context: EquipmentContext)
  extends EquipmentInstanceRepo[DBIO] {
    import context.profile.api._

    override def get: DBIO[Seq[EquipmentInstance]] = context.instances.result

    override def getById(id: Int): DBIO[Option[EquipmentInstance]] =
        context.instances
            .filter(i => !i.isDeleted && i.id === id)
            .result
            .headOption

    override def getByUid(uid: Uid): DBIO[Option[EquipmentInstance]] =
        context.instances
            .filter(i => !i.isDeleted && i.uid === uid)
            .result
            .headOption

    override def getByModelUid(modelUid: Uid): DBIO[Seq[EquipmentInstance]] =
        context.instances
            .filter(i => !i.isDeleted && i.modelUid === modelUid)
            .result

    override def add(instance: EquipmentInstance): DBIO[EquipmentInstance] =
        (context.instances returning context.instances.map(_.id)
            into ((instance, id) => instance.copy(id = Some(id)))) += instance

    override def update(instance: EquipmentInstance): DBIO[Int] = {
        val targetRows =
            context.instances.filter(i =>
                !i.isDeleted && i.uid === instance.uid
            )
        for {
            old <- targetRows.result.headOption
            updateActionOption = old.map(b =>
                targetRows.update(
                  instance.copy(id = b.id, modelUid = b.modelUid)
                )
            )
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeById(id: Int): DBIO[Int] = {
        val targetRows =
            context.instances.filter(i => !i.isDeleted && i.id === id)
        for {
            booleanOption <- targetRows.result.headOption
            updateActionOption = booleanOption.map(i =>
                targetRows.update(i.copy(isDeleted = true))
            )
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeByUid(uid: Uid): DBIO[Int] = {
        val targetRows =
            context.instances.filter(i => !i.isDeleted && i.uid === uid)
        for {
            booleanOption <- targetRows.result.headOption
            updateActionOption = booleanOption.map(i =>
                targetRows.update(i.copy(isDeleted = true))
            )
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def restoreByUid(uid: Uid): DBIO[Int] = {
        val targetRows =
            context.instances.filter(i => i.isDeleted && i.uid === uid)
        for {
            booleanOption <- targetRows.result.headOption
            updateActionOption = booleanOption.map(i =>
                targetRows.update(i.copy(isDeleted = false))
            )
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }
}
