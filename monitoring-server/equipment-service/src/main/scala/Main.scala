import slick.dbio._
import slick.jdbc.{PostgresProfile, JdbcProfile}
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, ExecutionContext, Future}

import com.twitter.finagle.{Http, ListeningServer}
// import com.twitter.util.Future

import cats.effect.{IO, IOApp, Blocker, ExitCode, Resource}
import cats.effect.syntax._

import io.finch._
import io.finch.catsEffect._
import io.finch.circe._

import io.circe.generic.auto._

import scalaz.Monad

import dbio._
import repos.slick._
import services.default._
import models.DataContext
import com.twitter.finagle.Http
import cats.effect.IO
import models.EquipmentModel
import services.EquipmentModelService

object ModelsController {

    def apply[F[_]: Monad](service: EquipmentModelService[F]) =
        get("models" :: path[Int]) { modelId: Int =>
            for { result <- service.getById(modelId) } yield result.get
        }
}

object Main extends IOApp {

    val pgdb: Database = Database.forConfig("postgres")
    val context: DataContext = new DataContext(PostgresProfile)

    val modelRepo = new SlickEquipmentModelRepo(context)
    val instanceRepo = new SlickEquipmentInstanceRepo(context)
    val paramRepo = new SlickParamRepo(context)

    val dbManager = new SlickDbManager(pgdb, PostgresProfile)

    import scalaz.Scalaz._
    val modelService =
        new DefaultEquipmentModelService[Future, DBIO](
          modelRepo,
          dbManager
        )

    Await.result(dbManager.execute(context.setup), Duration.Inf)

    def startServer: IO[ListeningServer] =
        IO(
          Http.server.serve(
            ":8081",
            (getModelById)
                .toServiceAs[Application.Json]
          )
        )

    def run(args: List[String]): IO[ExitCode] = {
        val server = Resource.make(startServer)(s =>
            IO.suspend(implicitly[ToAsync[Future, IO]].apply(s.close()))
        )

        server.use(_ => IO.never)
    }
}

object db {
    import scalaz.Scalaz._

    def main(args: Array[String]): Unit = {

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
