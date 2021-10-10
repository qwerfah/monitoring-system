package com.qwerfah.equipment

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}

import io.finch.Application
import io.finch.circe._
import io.circe.generic.auto._

import com.qwerfah.equipment.controllers._
import com.qwerfah.common.json.Encoders

import com.qwerfah.common.controllers._

object Main extends TwitterServer {
    import Startup._
    import Encoders._

    object EquipmentSessionController extends SessionController

    startup()

    val server =
        Http.serve(
          config.getString("port"),
          RequestLoggingFilter
              .andThen(RequestReportingFilter)
              .andThen(
                EquipmentModelController.api
                    .:+:(EquipmentInstanceController.api)
                    .:+:(ParamController.api)
                    .:+:(EquipmentSessionController.api)
                    .toServiceAs[Application.Json]
              )
        )
    onExit {
        server.close()
        pgdb.close()
        actorSystem.terminate()
    }

    com.twitter.util.Await.ready(server)
}
