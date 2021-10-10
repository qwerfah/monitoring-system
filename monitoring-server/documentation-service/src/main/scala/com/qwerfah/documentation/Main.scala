package com.qwerfah.documentation

import com.twitter.server.TwitterServer
import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.finagle.http.{Request, Response}

import io.finch.circe._
import io.finch.Application

import io.circe.generic.auto._

import com.qwerfah.documentation.controllers._

import com.qwerfah.common.controllers._
import com.qwerfah.common.json.Encoders
import com.twitter.util.StorageUnit

object Main extends TwitterServer {
    import Startup._
    import Encoders._

    object DocumentationSessionController extends SessionController

    startup()

    val server =
        Http.server
            .withMaxRequestSize(StorageUnit.fromMegabytes(10))
            .serve(
              config.getString("port"),
              RequestLoggingFilter
                  .andThen(RequestReportingFilter)
                  .andThen(
                    (FileController.api :+: DocumentationSessionController.api)
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
