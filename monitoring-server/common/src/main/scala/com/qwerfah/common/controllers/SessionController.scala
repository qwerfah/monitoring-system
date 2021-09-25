package com.qwerfah.common.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.{Future, FuturePool}

import io.finch.catsEffect._
import io.finch._
import io.finch.circe._

import io.circe.generic.auto._

import io.catbird.util._

import com.qwerfah.common.json.Decoders
import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.common.services._
import com.qwerfah.common.controllers.Controller
import com.qwerfah.common.resources.Credentials
import com.qwerfah.common.services.response.ObjectResponse

/** Provide endpoints for interservice authorization.
  * @param userService
  *   External users service.
  * @param tokenService
  *   Token managment service.
  */
abstract class SessionController(implicit
  tokenService: TokenService[Future]
) extends Controller {
    import Decoders._

    private val refresh =
        post("session" :: "refresh" :: headerOption("Authorization")) {
            header: Option[String] =>
                authorize(header, tokenService.refresh(_))
        }

    private val login =
        post("session" :: "login" :: jsonBody[Credentials]) {
            credentials: Credentials =>
                for {
                    result <- tokenService.login(credentials)
                } yield result.asOutput
        }

    private val verify =
        post("session" :: "verify" :: headerOption("Authorization")) {
            header: Option[String] =>
                authorize[Future, Uid](
                  header,
                  uid => FuturePool immediatePool { ObjectResponse(uid) }
                )
        }

    val api = login.:+:(refresh).:+:(verify).handle(errorHandler)
}
