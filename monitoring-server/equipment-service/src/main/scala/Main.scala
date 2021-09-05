import slick.dbio._
import slick.jdbc.{PostgresProfile, JdbcProfile}
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
        val pgdb: Database = Database.forConfig("postgres")
        val context: DataContext = new DataContext(PostgresProfile)

        val modelRepo = new SlickEquipmentModelRepo(context)
        val instanceRepo = new SlickEquipmentInstanceRepo(context)
        val paramRepo = new SlickParamRepo(context)

        val dbManager = new SlickDbManager(pgdb, PostgresProfile)
        val modelService =
            new DefaultEquipmentModelService[Future, DBIO](
              modelRepo,
              dbManager
            )

        Await.result(dbManager.execute(context.setup), Duration.Inf)

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
}
