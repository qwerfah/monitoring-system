import scala.concurrent.ExecutionContext
import scalaz.Monad
import slick.dbio.DBIO

trait DBIOInstances {
    implicit def instance(implicit ec: ExecutionContext): Monad[DBIO] =
        new Monad[DBIO] {
            override def bind[A, B](fa: DBIO[A])(f: A => DBIO[B]) =
                fa.flatMap(f)
            override def point[A](a: => A): DBIO[A] = DBIO.successful(a)
        }
}

object dbio extends DBIOInstances
