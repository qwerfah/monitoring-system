package com.qwerfah.common.repos.slick

import slick.dbio._
import scala.concurrent.ExecutionContext.Implicits.global

import enumeratum._

import com.qwerfah.common.repos.UserRepo
import com.qwerfah.common.models._
import com.qwerfah.common.resources.UserRole
import com.qwerfah.common.Uid
import com.fasterxml.jackson.module.scala.deser.overrides
import com.qwerfah.common.resources.UserRole._

class SlickUserRepo(implicit val context: DataContext) extends UserRepo[DBIO] {
    import context.profile.api._
    import context.roleMapper

    override def get: DBIO[Seq[User]] =
        context.users.result

    override def getById(id: Int): DBIO[Option[User]] =
        context.users.filter(_.id === id).result.headOption

    override def getByUid(uid: Uid): DBIO[Option[User]] =
        context.users.filter(_.uid === uid).result.headOption

    override def getUserByLogin(login: String): DBIO[Option[User]] =
        context.users
            .filter(u =>
                (u.role === (SystemAdmin: UserRole)
                || u.role === (EquipmentAdmin: UserRole)
                || u.role === (EquipmentUser: UserRole))
                && u.login === login
            )
            .result
            .headOption

    override def getServiceByLogin(login: String): DBIO[Option[User]] =
        context.users
            .filter(u => u.role === (Service: UserRole) && u.login === login)
            .result
            .headOption

    override def add(user: User): DBIO[User] =
        (context.users returning context.users.map(_.id) into ((user, id) =>
            user.copy(id = Some(id))
        )) += user

    override def update(user: User): DBIO[Int] = {
        val targetRows = context.users.filter(_.uid === user.uid)
        for {
            booleanOption <- targetRows.result.headOption
            affected <- booleanOption
                .map(b => targetRows.update(user))
                .getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeById(id: Int): DBIO[Int] = context.users.filter(_.id === id).delete

    override def removeByUid(uid: Uid): DBIO[Int] = context.users.filter(_.uid === uid).delete
}
