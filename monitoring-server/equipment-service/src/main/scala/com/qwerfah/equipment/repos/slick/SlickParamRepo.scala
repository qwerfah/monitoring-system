package com.qwerfah.equipment.repos.slick

import slick.dbio._
import scala.concurrent.ExecutionContext.Implicits.global

import com.qwerfah.equipment.repos._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._

class SlickParamRepo(implicit val context: DataContext)
  extends ParamRepo[DBIO] {
    import context.jdbcProfile.api._

    override def getById(id: Int): DBIO[Option[Param]] =
        context.params.filter(_.id === id).result.headOption

    override def getByGuid(uid: Uid): DBIO[Option[Param]] =
        context.params.filter(_.uid === uid).result.headOption

    override def add(param: Param): DBIO[Param] =
        (context.params returning context.params.map(_.id) into ((param, id) =>
            param.copy(id = Some(id))
        )) += param

    override def update(param: Param): DBIO[Int] = {
        val targetRows = context.params.filter(_.uid === param.uid)
        for {
            booleanOption <- targetRows.result.headOption
            updateActionOption = booleanOption.map(b =>
                targetRows.update(param)
            )
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeById(id: Int): DBIO[Int] =
        context.params.filter(_.id === id).delete

    override def removeByGuid(uid: Uid): DBIO[Int] =
        context.params.filter(_.uid === uid).delete
}