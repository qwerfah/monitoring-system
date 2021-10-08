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

    def get: DBIO[Seq[(EquipmentInstance, String)]] = (for {
        (i, m) <-
            context.instances.filter(!_.isDeleted) join context.models.filter(
              !_.isDeleted
            ) on (_.modelUid === _.uid)
    } yield (i, m.name)).result

    override def getById(id: Int): DBIO[Option[EquipmentInstance]] =
        context.instances
            .filter(i => !i.isDeleted && i.id === id)
            .result
            .headOption

    override def getByUid(uid: Uid): DBIO[Option[(EquipmentInstance, String)]] =
        (for {
            (i, m) <-
                context.instances.filter(i =>
                    !i.isDeleted && i.uid === uid
                ) join context.models.filter(
                  !_.isDeleted
                ) on (_.modelUid === _.uid)
        } yield (i, m.name)).result.headOption

    override def getByModelUid(
      modelUid: Uid
    ): DBIO[Seq[(EquipmentInstance, String)]] =
        (for {
            (i, m) <-
                context.instances.filter(i =>
                    !i.isDeleted && i.modelUid === modelUid
                ) join context.models.filter(
                  !_.isDeleted
                ) on (_.modelUid === _.uid)
        } yield (i, m.name)).result

    override def add(
      instance: EquipmentInstance
    ): DBIO[(EquipmentInstance, String)] =
        for {
            result <- ((context.instances returning context.instances.map(_.id)
                into ((instance, id) =>
                    instance.copy(id = Some(id))
                )) += instance)
            model <- context.models
                .filter(m => !m.isDeleted && m.uid === result.modelUid)
                .result
                .head
        } yield (result, model.name)

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

    override def removeByModelUid(modelUid: Uid): DBIO[Int] = {
        val targetRows = context.instances.filter(i =>
            !i.isDeleted && i.modelUid === modelUid
        )
        for {
            values <- targetRows.result
            updateActions = DBIO.sequence(
              values.map(i => targetRows.update(i.copy(isDeleted = true)))
            )
            affected <- updateActions.map { _.sum }
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

    override def restoreByModelUid(modelUid: Uid): DBIO[Int] = {
        val targetRows = context.instances.filter(i =>
            i.isDeleted && i.modelUid === modelUid
        )
        for {
            values <- targetRows.result
            updateActions = DBIO.sequence(
              values.map(i => targetRows.update(i.copy(isDeleted = false)))
            )
            affected <- updateActions.map { _.sum }
        } yield affected
    }

}
