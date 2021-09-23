package com.qwerfah.equipment.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.{Future, FuturePool}

import io.finch.catsEffect._
import io.finch._
import io.finch.circe._

import io.circe.generic.auto._

import com.qwerfah.equipment.services._
import com.qwerfah.equipment.repos.slick._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.Startup
import com.qwerfah.common.json.Decoders
import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.common.services._
import com.qwerfah.common.controllers.Controller
import com.qwerfah.common.resources.Credentials

object AuthController extends Controller {
    import Startup._
    import Decoders._

    private val userService = implicitly[UserService[Future]]
    private val tokenService = implicitly[TokenService[Future]]

    private val refresh =
        post("auth" :: "refresh" :: headerOption("Authorization")) {
            header: Option[String] =>
                header match {
                    case Some(token) =>
                        tokenService.refresh(token) map { result =>
                            result.asOutput
                        }
                    case None =>
                        FuturePool.immediatePool { Unauthorized(NoTokenHeader) }
                }
        }

    private val login =
        post("auth" :: "login" :: jsonBody[Credentials]) {
            credentials: Credentials =>
                for {
                    result <- userService.login(credentials)
                } yield result.asOutput
        }

    val api = (refresh :+: login).handle(errorHandler)
}
