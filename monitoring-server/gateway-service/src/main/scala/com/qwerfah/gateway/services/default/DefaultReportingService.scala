package com.qwerfah.gateway.services.default

import com.twitter.finagle.http.Response

import cats.Monad
import cats.implicits._

import com.qwerfah.gateway.services.ReportingService

import com.qwerfah.common.http.{HttpClient, HttpMethod}

class DefaultReportingService[F[_]: Monad](reportingClient: HttpClient[F])
  extends ReportingService[F] {
    def getModelStats: F[Response] =
        reportingClient.send(HttpMethod.Get, "/api/reports/models/stats")

    def getServiceStats: F[Response] =
        reportingClient.send(HttpMethod.Get, "/api/reports/services/stats")
}
