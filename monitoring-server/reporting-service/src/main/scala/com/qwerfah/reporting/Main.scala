package com.qwerfah.reporting

import com.twitter.server.TwitterServer
import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.finagle.http.{Request, Response}

import io.finch.circe._
import io.finch.Application

import io.circe.generic.auto._

import com.qwerfah.reporting.controllers._

import com.qwerfah.common.controllers._
import com.qwerfah.common.json.Encoders

object Main extends TwitterServer {
    import Startup._
    import Encoders._

    object ReportingSessionController extends SessionController

    startup()

    val server =
        Http.serve(
          config.getString("port"),
          RequestLoggingFilter.andThen(
            OperationRecordController.api
                .:+:(ReportingSessionController.api)
                .:+:(StatController.api)
                .toServiceAs[Application.Json]
          )
        )
    onExit {
        server.close()
        subscriptionRef.close()
        actorSystem.terminate()
        pgdb.close()
    }

    com.twitter.util.Await.ready(server)
}
