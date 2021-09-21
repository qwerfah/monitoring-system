package com.qwerfah.gateway.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.finagle.{SimpleFilter, Service}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.Future

import io.finch.catsEffect._
import io.finch._
import io.finch.circe._

import io.circe.generic.auto._

import com.qwerfah.gateway.services._
import com.qwerfah.gateway.Startup
import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.common.services._
import com.qwerfah.common.controllers.Controller
import com.qwerfah.equipment.json.Decoders


object EquipmentController extends Controller {
    import Startup._
    import Decoders._

    private val equipmentService = implicitly[EquipmentService[Future]]

    type Req = com.twitter.finagle.http.Request

    class AuthFilter extends SimpleFilter[Req, Response] {
        def apply(
          request: Req,
          service: Service[Req, Response]
        ): com.twitter.util.Future[Response] =
            request match {
                case req if req.authorization.contains("secret") => service(req)
                case _ =>
                    com.twitter.util.Future(Response(Status.Unauthorized))
            }
    }

    private val getModels = get("models") {
        equipmentService.getAll map { result => result.asOutput }
    }

    val api = (getModels)
}
