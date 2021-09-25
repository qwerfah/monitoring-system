package com.qwerfah.equipment

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}

import io.finch.Application
import io.finch.circe._
import io.circe.generic.auto._

import com.qwerfah.equipment.controllers._
import com.qwerfah.common.json.Encoders
import com.twitter.finagle.SimpleFilter

import com.qwerfah.common.controllers._

object Main extends TwitterServer {
    import Startup._
    import Encoders._

    object EquipmentAuthController extends AuthController

    startup()

    val server =
        Http.serve(
          ":8081",
          RequestLoggingFilter.andThen(
            EquipmentModelController.api
                .:+:(EquipmentInstanceController.api)
                .:+:(ParamController.api)
                .:+:(EquipmentAuthController.api)
                .toServiceAs[Application.Json]
          )
        )
    onExit { server.close() }

    com.twitter.util.Await.ready(server)
}
