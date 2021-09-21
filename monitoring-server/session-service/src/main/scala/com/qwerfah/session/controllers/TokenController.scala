package com.qwerfah.session.controllers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}
import io.finch.catsEffect._
import io.finch._
import io.finch.circe._

import io.circe.generic.auto._

import com.qwerfah.session.Startup
import com.qwerfah.session.json.Decoders
import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.common.services._
import com.qwerfah.common.services.response._
import com.qwerfah.common.controllers.Controller

object TokenController extends Controller {
    import Startup._
    import Decoders._

    private val tokenService = implicitly[TokenService[Future]]

    private val refresh =
        post("tokens" :: "refresh" :: headerOption("Authorization")) {
            header: Option[String] =>
                header match {
                    case Some(token) =>
                        tokenService.refresh(token) map { result =>
                            result.asOutput
                        }
                    case None => Future.successful(Unauthorized(NoTokenHeader))
                }
        }

    private val validate =
        patch("tokens" :: "validate" :: headerOption("Authorization")) {
            header: Option[String] =>
                header match {
                    case Some(token) =>
                        tokenService.validate(token) map { result =>
                            result.asOutput
                        }
                    case None => Future.successful(Unauthorized(NoTokenHeader))
                }
        }

    val api = (refresh :+: validate).handle(errorHandler)
}
