package com.qwerfah.equipment

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}

import com.qwerfah.equipment.controllers._
import io.finch.Application

import io.finch.circe._
import io.circe.generic.auto._

object Main extends TwitterServer {
    import Startup._
    import Encoders._

    startup()

    val server =
        Http.serve(
          ":8081",
          (EquipmentModelController.api :+: EquipmentInstanceController.api)
              .toServiceAs[Application.Json]
        )
    onExit { server.close() }

    com.twitter.util.Await.ready(server)
}
