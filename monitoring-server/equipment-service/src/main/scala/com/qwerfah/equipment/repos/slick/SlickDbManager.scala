package com.qwerfah.equipment.repos.slick

import slick.dbio.DBIO
import slick.jdbc.JdbcProfile
import slick.jdbc.JdbcBackend.Database
import scala.concurrent.Future

import com.qwerfah.equipment.repos.DbManager

/** Slick implementation of db manager trait.
  * @param db
  *   Jdbc backend db instance.
  * @param profile
  *   Jdbc profile (specifies db config).
  */
class SlickDbManager(implicit db: Database, profile: JdbcProfile)
  extends DbManager[Future, DBIO] {

    /** Launch action execution and return Future result placeholder.
      * @param action
      *   Slick DBIO action.
      * @return
      *   Future result placeholder.
      */
    override def execute[A](action: DBIO[A]): Future[A] = db.run(action)

    /** Launch action (that may be sequence of actions) execution
      * transactionally and return Future result placeholder.
      * @param action
      *   Slick DBIO action.
      * @return
      *   Future result placeholder.
      */
    override def executeTransactionally[A](action: DBIO[A]): Future[A] = {
        import profile.api._

        db.run(action.transactionally)
    }

    /** Pack sequence of DBIO actions into single DBIO action that can be
      * execute transactionally.
      * @param action
      *   DBIO actions sequence.
      * @return
      *   Single DBIO action.
      */
    override def sequence[A](action: Seq[DBIO[A]]): DBIO[Seq[A]] =
        DBIO.sequence(action)
}
