package com.qwerfah.session

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}

import io.finch.Application
import io.finch.circe._
import io.circe.generic.auto._

import com.qwerfah.session.controllers._

import com.qwerfah.common.json.Encoders
import com.qwerfah.common.controllers._

object Main extends TwitterServer {
    import Startup._
    import Encoders._

    object UserSessionController extends SessionController

    startup()

    val server =
        Http.serve(
          config.getString("port"),
          RequestLoggingFilter
              .andThen(RequestReportingFilter)
              .andThen(
                UserController.api
                    .:+:(UserSessionController.api)
                    .toServiceAs[Application.Json]
              )
        )
    onExit {
        server.close()
        actorSystem.terminate()
    }

    com.twitter.util.Await.ready(server)
}
