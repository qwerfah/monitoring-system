package com.qwerfah.generator

import com.twitter.server.TwitterServer
import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.finagle.http.{Request, Response}

import io.finch.circe._
import io.finch.Application

import io.circe.generic.auto._

import com.qwerfah.generator.controllers._

import com.qwerfah.common.controllers._
import com.qwerfah.common.json.Encoders

object Main extends TwitterServer {
    import Startup._
    import Encoders._

    object GeneratorSessionController extends SessionController

    startup()

    val server =
        Http.serve(
          config.getString("port"),
          RequestLoggingFilter
              .andThen(RequestReportingFilter)
              .andThen(
                GeneratorSessionController.api
                    .:+:(ParamValueController.api)
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
