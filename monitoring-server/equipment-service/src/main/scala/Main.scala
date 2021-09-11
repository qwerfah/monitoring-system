import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import slick.dbio._

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}

import io.finch._
import io.finch.catsEffect._
import io.finch.circe._

import io.circe.generic.auto._

import controllers._
import main.Startup._

object Main extends TwitterServer {
    startup()

    val server =
        Http.serve(
          ":8081",
          ModelsController.api.toService
        )
    onExit { server.close() }

    com.twitter.util.Await.ready(server)
}
