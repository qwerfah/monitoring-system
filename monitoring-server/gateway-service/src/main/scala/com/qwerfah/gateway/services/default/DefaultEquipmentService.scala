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
import com.qwerfah.common.services.response._
import com.qwerfah.common.exceptions._
import com.qwerfah.common.http._

class DefaultEquipmentService[F[_]: Monad](implicit client: HttpClient[F])
  extends EquipmentService[F] {
    import Decoders._

    override def getAll: F[ServiceResponse[Seq[ModelResponse]]] =
        client.sendAndDecode(Get, "/modelss")
}
