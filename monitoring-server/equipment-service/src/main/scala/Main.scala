import slick.dbio._
import slick.jdbc.{PostgresProfile, JdbcBackend, JdbcProfile}
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, ExecutionContext, Future}
import scalaz._
import scalaz.Scalaz._
import dbio._

import repos.slick._
import services.default._
import models.DataContext

object Main {
    def main(args: Array[String]): Unit = {
        val pgdb: JdbcBackend.Database = Database.forConfig("postgres")
        val context: DataContext = new DataContext(PostgresProfile)

        val modelRepo = new SlickEquipmentModelRepo(context)
        val instanceRepo = new SlickEquipmentInstanceRepo(context)
        val paramRepo = new SlickParamRepo(context)

        val dBIOTransformer = dBIOTransformation(PostgresProfile, pgdb)
        val modelService =
            new DefaultEquipmentModelService[Future, DBIO](
              modelRepo,
              dBIOTransformer
            )

        Await.result(dBIOTransformer(context.setup), Duration.Inf)

        println("Schema created")

        println(
          Await.result(
            modelService.getById(1),
            Duration.Inf
          )
        )
        println(
          Await.result(
            modelService.getById(2),
            Duration.Inf
          )
        )
    }

    private def dBIOTransformation(
      profile: JdbcProfile,
      db: JdbcBackend.Database
    ): DBIO ~> Future = new (DBIO ~> Future) {
        import profile.api._
        override def apply[A](fa: DBIO[A]): Future[A] =
            db.run(fa.transactionally)
    }
}
