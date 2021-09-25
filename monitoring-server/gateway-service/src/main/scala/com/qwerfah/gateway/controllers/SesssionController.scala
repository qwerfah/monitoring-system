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
import com.qwerfah.common.json.Decoders

object SessionController extends Controller {
    import Startup._
    import Decoders._

    private def sessionService = implicitly[SessionService[Future]]

    private val validate = post("validate" :: headerOption("Authorization")) {
        header: Option[String] =>
            sessionService.validate(header.get) map { _.asOutput }
    }

    val api = (validate).handle(errorHandler)
}
