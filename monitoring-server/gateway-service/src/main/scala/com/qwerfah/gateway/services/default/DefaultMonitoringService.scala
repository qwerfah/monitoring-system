package com.qwerfah.gateway.services.default

import com.twitter.finagle.{Service, ServiceFactory}
import com.twitter.finagle.Http
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.Future

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

import cats.Monad
import cats.implicits._

import com.qwerfah.gateway.services.MonitoringService

import com.qwerfah.monitoring.resources._
import com.qwerfah.monitoring.json.Decoders

import com.qwerfah.common.Uid
import com.qwerfah.common.http._
import com.qwerfah.common.exceptions._
import com.qwerfah.common.services.response._

class DefaultMonitoringService[F[_]: Monad](client: HttpClient[F])
  extends MonitoringService[F] {
    import Decoders._

    override def getMonitors: F[Response] =
        client.send(HttpMethod.Get, "/api/monitors")

    override def getInstanceMonitors(instanceUid: Uid): F[Response] =
        client.send(HttpMethod.Get, s"/api/instances/$instanceUid/monitors")

    override def getMonitor(uid: Uid): F[Response] =
        client.send(HttpMethod.Get, s"/api/monitors/$uid")

    override def addMonitor(request: AddMonitorRequest): F[Response] =
        client.send(
          HttpMethod.Post,
          "/api/monitors",
          Some(request.asJson.toString)
        )

    override def updateMonitor(
      uid: Uid,
      request: UpdateMonitorRequest
    ): F[Response] = client.send(
      HttpMethod.Patch,
      s"/api/monitors/$uid",
      Some(request.asJson.toString)
    )

    override def removeMonitor(uid: Uid): F[Response] =
        client.send(HttpMethod.Delete, s"/api/monitors/$uid")
}
