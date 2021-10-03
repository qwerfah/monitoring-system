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

    private def equipmentService = implicitly[EquipmentService[Future]]

    private def getModels = get("models") {
        equipmentService.getAll map { _.asOutput }
    }

    def api = "api" :: "equipment" :: (getModels).handle(errorHandler)
}
