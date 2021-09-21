package com.qwerfah.gateway.services.default

import com.twitter.finagle.{Service, ServiceFactory}
import com.twitter.finagle.Http
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.Future

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

import cats.Monad
import cats.implicits._

import com.qwerfah.gateway.services.EquipmentService
import com.qwerfah.common.services.response._
import com.qwerfah.common.exceptions._
import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.json.Decoders

class DefaultEquipmentService extends EquipmentService[Future] {
    import Decoders._

    private def decodeJson[A: Decoder](body: String) = {
        decode[A](body) match {
            case Right(value) => ObjectResponse(value)
            case Left(error) => UnprocessableResponse(BadEquipmentServiceResult)
        }
    }

    override def getAll: Future[ServiceResponse[Seq[ModelResponse]]] = {
        val equipmentService: Service[Request, Response] =
            Http.client.newService("localhost:8081")

        val request = Request("/models")

        equipmentService(request) map { response =>
            response.status match {
                case Status.Ok =>
                    decodeJson[Seq[ModelResponse]](response.contentString)
                case Status.InternalServerError =>
                    InternalErrorResponse(EquipmentServiceInternalError)
                case Status.ServiceUnavailable =>
                    BadGatewayResponse(EquipmentServiceUnavailable)
                case _ => UnknownErrorResponse(UnknownEquipmentServiceResponse)
            }

        }
    }
}
