package com.qwerfah.common.repos.slick

import slick.dbio._
import scala.concurrent.ExecutionContext.Implicits.global

import com.qwerfah.common.repos.UserRepo
import com.qwerfah.common.models._
import com.qwerfah.common.resources._
import com.qwerfah.common.Uid
import com.fasterxml.jackson.module.scala.deser.overrides

class SlickUserRepo(implicit val context: DataContext) extends UserRepo[DBIO] {
    import context.profile.api._

    override def get: DBIO[Seq[User]] =
        context.users.filter(!_.isDeleted).result

    override def getById(id: Int): DBIO[Option[User]] =
        context.users.filter(u => !u.isDeleted && u.id === id).result.headOption

    override def getByUid(uid: Uid): DBIO[Option[User]] =
        context.users
            .filter(u => !u.isDeleted && u.uid === uid)
            .result
            .headOption

    override def getByLogin(login: String): DBIO[Option[User]] =
        context.users
            .filter(u => !u.isDeleted && u.login === login)
            .result
            .headOption

    override def add(user: User): DBIO[User] =
        (context.users returning context.users.map(_.id) into ((user, id) =>
            user.copy(id = Some(id))
        )) += user

    override def update(user: User): DBIO[Int] = {
        val targetRows =
            context.users.filter(u => !u.isDeleted && u.uid === user.uid)
        for {
            booleanOption <- targetRows.result.headOption
            affected <- booleanOption
                .map(b => targetRows.update(user))
                .getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeById(id: Int): DBIO[Int] = {
        val targetRows =
            context.users.filter(u => !u.isDeleted && u.id === id)
        for {
            booleanOption <- targetRows.result.headOption
            affected <- booleanOption
                .map(u => targetRows.update(u.copy(isDeleted = true)))
                .getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeByUid(uid: Uid): DBIO[Int] = {
        val targetRows =
            context.users.filter(u => !u.isDeleted && u.uid === uid)
        for {
            booleanOption <- targetRows.result.headOption
            affected <- booleanOption
                .map(u => targetRows.update(u.copy(isDeleted = true)))
                .getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def restoreByUid(uid: Uid): DBIO[Int] = {
        val targetRows =
            context.users.filter(u => !u.isDeleted && u.uid === uid)
        for {
            booleanOption <- targetRows.result.headOption
            affected <- booleanOption
                .map(u => targetRows.update(u.copy(isDeleted = false)))
                .getOrElse(DBIO.successful(0))
        } yield affected
    }
}
