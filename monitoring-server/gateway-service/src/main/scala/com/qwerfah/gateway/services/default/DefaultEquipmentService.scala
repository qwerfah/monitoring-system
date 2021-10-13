package com.qwerfah.gateway.services.default

import com.twitter.finagle.{Service, ServiceFactory}
import com.twitter.finagle.Http
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.Future

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

import cats.Monad
import cats.implicits._

import com.qwerfah.gateway.services.EquipmentService

import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.json.Decoders

import com.qwerfah.common.Uid
import com.qwerfah.common.http._
import com.qwerfah.common.exceptions._
import com.qwerfah.common.services.response._

class DefaultEquipmentService[F[_]: Monad](client: HttpClient[F])
  extends EquipmentService[F] {
    import Decoders._

    override def getModels: F[Response] = {
        client.send(HttpMethod.Get, "/api/models")
    }

    override def getModel(uid: Uid): F[Response] =
        client.send(HttpMethod.Get, s"/api/models/$uid")

    override def addModel(request: ModelRequest): F[Response] =
        client.send(
          HttpMethod.Post,
          s"/api/models",
          Some(request.asJson.toString)
        )

    override def updateModel(uid: Uid, request: ModelRequest): F[Response] =
        client.send(
          HttpMethod.Patch,
          s"/api/models/$uid",
          Some(request.asJson.toString)
        )

    override def removeModel(uid: Uid): F[Response] =
        client.send(HttpMethod.Delete, s"/api/models/$uid")

    override def getInstances: F[Response] =
        client.send(HttpMethod.Get, "/api/instances")

    override def getModelInstances(modelUid: Uid): F[Response] =
        client.send(HttpMethod.Get, s"/api/models/$modelUid/instances")

    override def getActiveModelInstances(modelUid: Uid): F[Response] =
        client.send(HttpMethod.Get, s"/api/models/$modelUid/instances/active")

    override def getInstance(uid: Uid): F[Response] =
        client.send(HttpMethod.Get, s"/api/instances/$uid")

    override def addInstance(
      modelUid: Uid,
      request: AddInstanceRequest
    ): F[Response] = {
        client.send(
          HttpMethod.Post,
          s"/api/models/${modelUid}/instances",
          Some(request.asJson.toString)
        )
    }

    override def updateInstance(
      uid: Uid,
      request: UpdateInstanceRequest
    ): F[Response] = client.send(
      HttpMethod.Patch,
      s"/api/instances/$uid",
      Some(request.asJson.toString)
    )

    override def removeInstance(uid: Uid): F[Response] =
        client.send(HttpMethod.Delete, s"/api/instances/$uid")

    override def getParams: F[Response] =
        client.send(HttpMethod.Get, s"/api/params")

    override def getModelParams(modelUid: Uid): F[Response] =
        client.send(HttpMethod.Get, s"/api/models/$modelUid/params")

    override def getInstanceParams(instanceUid: Uid): F[Response] =
        client.send(HttpMethod.Get, s"/api/instances/$instanceUid/params")

    override def getParam(uid: Uid): F[Response] =
        client.send(HttpMethod.Get, s"/api/params/$uid")

    override def addParam(
      modelUid: Uid,
      request: AddParamRequest
    ): F[Response] =
        client.send(
          HttpMethod.Post,
          s"/api/models/${modelUid}/params",
          Some(request.asJson.toString)
        )

    override def updateParam(
      uid: Uid,
      request: UpdateParamRequest
    ): F[Response] = client.send(
      HttpMethod.Patch,
      s"/api/params/$uid",
      Some(request.asJson.toString)
    )

    override def removeParam(uid: Uid): F[Response] =
        client.send(HttpMethod.Delete, s"/api/params/$uid")
}
