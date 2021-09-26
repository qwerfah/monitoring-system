package com.qwerfah.gateway

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}

import io.finch.Application
import io.finch.circe._
import io.circe.generic.auto._

import com.qwerfah.gateway.controllers._
import com.qwerfah.common.json.Encoders
import com.qwerfah.common.controllers._

object Main extends TwitterServer {
    import Startup._
    import Encoders._

    object GatewaySessionController extends SessionController

    val server =
        Http.serve(
          ":8082",
          RequestLoggingFilter.andThen(
            EquipmentController.api
                .:+:(GatewaySessionController.api)
                .toServiceAs[Application.Json]
          )
        )
    onExit { server.close() }

    com.twitter.util.Await.ready(server)
}
