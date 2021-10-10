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

class DefaultMonitoringService[F[_]: Monad](
  equipmentClient: HttpClient[F],
  monitoringClient: HttpClient[F]
) extends MonitoringService[F] {
    import Decoders._

    override def getMonitors: F[Response] =
        equipmentClient.send(HttpMethod.Get, "/api/instances/monitors")

    override def getInstanceMonitors(instanceUid: Uid): F[Response] =
        equipmentClient.send(
          HttpMethod.Get,
          s"/api/instances/$instanceUid/monitors"
        )

    override def getMonitor(uid: Uid): F[Response] =
        monitoringClient.send(HttpMethod.Get, s"/api/monitors/$uid")

    override def getMonitorParams(uid: Uid): F[Response] =
        monitoringClient.send(HttpMethod.Get, s"/api/monitors/$uid/params")

    override def getMonitorParamValues(uid: Uid): F[Response] = monitoringClient
        .send(HttpMethod.Get, s"/api/monitors/$uid/params/values")

    override def addMonitor(
      instanceUid: Uid,
      request: AddMonitorRequest
    ): F[Response] =
        equipmentClient.send(
          HttpMethod.Post,
          s"/api/instances/$instanceUid/monitors",
          Some(request.asJson.toString)
        )

    override def updateMonitor(
      uid: Uid,
      request: UpdateMonitorRequest
    ): F[Response] = monitoringClient.send(
      HttpMethod.Patch,
      s"/api/monitors/$uid",
      Some(request.asJson.toString)
    )

    override def removeMonitor(uid: Uid): F[Response] =
        monitoringClient.send(HttpMethod.Delete, s"/api/monitors/$uid")
}
