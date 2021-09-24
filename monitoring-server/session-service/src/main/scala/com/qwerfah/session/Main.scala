package com.qwerfah.session

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}

import io.finch.Application
import io.finch.circe._
import io.circe.generic.auto._

import com.qwerfah.session.controllers._
import com.qwerfah.common.json.Encoders
import com.qwerfah.common.controllers.RequestLoggingFilter

object Main extends TwitterServer {
    import Startup._
    import Encoders._

    startup()

    val server =
        Http.serve(
          ":8081",
          RequestLoggingFilter.andThen(
            (UserController.api :+: TokenController.api)
                .toServiceAs[Application.Json]
          )
        )
    onExit { server.close() }

    com.twitter.util.Await.ready(server)
}
