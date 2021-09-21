package com.qwerfah.gateway.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}
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
import com.qwerfah.equipment.json.Decoders

object EquipmentController {
    import Startup._
    import Decoders._

    private val equipmentService = implicitly[EquipmentService[Future]]

    private val getModels = get("models") {
        equipmentService.getAll map {
            case ObjectResponse(value) => Ok(value)
            case ErrorResponse(error)  => InternalServerError(error)
        }
    } handle { case e: Exception =>
        InternalServerError(e)
    }

    val api = getModels
}
