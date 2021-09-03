import scala.concurrent.Future
import slick.dbio.DBIO

class SlickDbManager extends DbManager[Future, DBIO] {

    override def execute[A](action: DBIO[A]): Future[A] = ???
    // db.run(action)
    override def executeTransitionally[A](action: DBIO[A]): Future[A] = ???
    // db.run(action.transactionally)
    override def sequence[A](action: Seq[DBIO[A]]): DBIO[Seq[A]] = ???
    // DBIO.sequence(action)
}
