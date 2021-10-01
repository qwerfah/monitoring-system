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
          ":8084",
          OperationRecordController.api
              .:+:(ReportingSessionController.api)
              .toServiceAs[Application.Json]
        )
    onExit {
        server.close()
        subscriptionRef.close()
    }

    com.twitter.util.Await.ready(server)

}