package com.qwerfah.session.repos.slick

import slick.dbio._
import scala.concurrent.ExecutionContext.Implicits.global

import com.qwerfah.session.repos._
import com.qwerfah.session.models._
import com.qwerfah.session.resources._
import com.qwerfah.common.Uid

class SlickUserRepo(implicit val context: DataContext) extends UserRepo[DBIO] {
    import context.profile.api._

    override def get: DBIO[Seq[User]] = context.users.result

    override def getById(id: Int): DBIO[Option[User]] =
        context.users.filter(_.id === id).result.headOption

    override def getByUid(uid: Uid): DBIO[Option[User]] =
        context.users.filter(_.uid === uid).result.headOption

    override def add(user: User): DBIO[User] =
        (context.users returning context.users.map(_.id) into ((user, id) =>
            user.copy(id = Some(id))
        )) += user

    override def update(user: User): DBIO[Int] = {
        val targetRows = context.users.filter(_.uid === user.uid)
        for {
            booleanOption <- targetRows.result.headOption
            updateActionOption = booleanOption.map(b => targetRows.update(user))
            affected <- updateActionOption.getOrElse(DBIO.successful(0))
        } yield affected
    }

    override def removeById(id: Int): DBIO[Int] =
        context.users.filter(_.id === id).delete

    override def removeByUid(uid: Uid): DBIO[Int] =
        context.users.filter(_.uid === uid).delete

}
