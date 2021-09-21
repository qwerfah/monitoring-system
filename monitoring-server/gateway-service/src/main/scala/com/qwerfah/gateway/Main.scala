package com.qwerfah.gateway

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}

import io.finch.Application
import io.finch.circe._
import io.circe.generic.auto._

import com.qwerfah.gateway.controllers._
import com.qwerfah.common.json.Encoders

object Main extends TwitterServer {
    import Startup._
    import Encoders._

    val server =
        Http.serve(
          ":8082",
          EquipmentController.api.toServiceAs[Application.Json]
        )
    onExit { server.close() }

    com.twitter.util.Await.ready(server)
}
