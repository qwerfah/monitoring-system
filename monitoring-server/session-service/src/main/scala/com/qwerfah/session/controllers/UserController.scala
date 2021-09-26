package com.qwerfah.session.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.{Future, FuturePool}

import io.finch.catsEffect._
import io.finch._
import io.finch.circe._

import io.catbird.util._

import com.qwerfah.session.Startup

import com.qwerfah.common.services._
import com.qwerfah.common.resources._
import com.qwerfah.common.json.Decoders
import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.common.services.response._
import com.qwerfah.common.controllers.Controller

object UserController extends Controller {
    import Startup._
    import Decoders._

    private val userService = implicitly[UserService[Future]]
    private implicit val tokenService = implicitly[TokenService[Future]]

    private val getUsers = get("users" :: headerOption("Authorization")) {
        header: Option[String] =>
            authorize(header, serviceRoles, _ => userService.getAll)
    }

    private val getUser =
        get("users" :: path[Uid] :: headerOption("Authorization")) {
            (uid: Uid, header: Option[String]) =>
                authorize(header, serviceRoles, _ => userService.get(uid))
        }

    private val register =
        post("users" :: "register" :: jsonBody[UserRequest]) {
            request: UserRequest =>
                userService.register(request) map { _.asOutput }
        }

    private val updateUser =
        patch(
          "users" :: path[Uid] :: jsonBody[UserRequest] :: headerOption(
            "Authorization"
          )
        ) { (uid: Uid, request: UserRequest, header: Option[String]) =>
            authorize(
              header,
              serviceRoles,
              _ => userService.update(uid, request)
            )
        }

    private val deleteUser =
        delete("users" :: path[Uid] :: headerOption("Authorization")) {
            (uid: Uid, header: Option[String]) =>
                authorize(header, serviceRoles, _ => userService.remove(uid))
        }

    val api = getUsers
        .:+:(getUser)
        .:+:(register)
        .:+:(updateUser)
        .:+:(deleteUser)
        .handle(errorHandler)
}
