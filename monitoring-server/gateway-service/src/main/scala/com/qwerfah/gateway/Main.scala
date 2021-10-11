package com.qwerfah.gateway

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.http.filter.Cors

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

    val policy: Cors.Policy = Cors.UnsafePermissivePolicy

    val server =
        Http.serve(
          config.getString("port"),
          new Cors.HttpFilter(policy)
              .andThen(
                RequestLoggingFilter
                    .andThen(RequestReportingFilter)
                    .andThen(
                      GatewaySessionController.api
                          .:+:(UserController.api)
                          .:+:(EquipmentController.api)
                          .:+:(DocumentationController.api)
                          .:+:(MonitoringController.api)
                          .toServiceAs[Application.Json]
                    )
              )
        )
    onExit {
        server.close()
        actorSystem.terminate()
    }

    com.twitter.util.Await.ready(server)
}
