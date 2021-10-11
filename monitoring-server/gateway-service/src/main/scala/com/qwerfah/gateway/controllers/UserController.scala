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

import io.catbird.util._

import com.qwerfah.gateway.services._
import com.qwerfah.gateway.Startup
import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.common.services._
import com.qwerfah.common.controllers.Controller
import com.qwerfah.common.json.Decoders
import com.qwerfah.common.resources.UserRequest

object UserController extends Controller {
    import Startup._
    import Decoders._

    private def userService = implicitly[UserService[Future]]

    private val getUsers = get("users" :: headerOption("Authorization")) {
        header: Option[String] =>
            authorizeRaw(header, adminRoles, _ => userService.getUsers)
    }

    private val registerUser = post(
      "users" :: "register" :: jsonBody[UserRequest] :: headerOption(
        "Authorization"
      )
    ) { (request: UserRequest, header: Option[String]) =>
        userService.registerUser(request)
    }

    private val addUser = post(
      "users" :: jsonBody[UserRequest] :: headerOption("Authorization")
    ) { (request: UserRequest, header: Option[String]) =>
        userService.addUser(request)
    }

    private val removeUser = delete(
      "users" :: path[Uid] :: headerOption("Authorization")
    ) { (userUid: Uid, header: Option[String]) =>
        authorizeRaw(header, adminRoles, _ => userService.removeUser(userUid))
    }

    val api = "api" :: "session" :: getUsers
        .:+:(addUser)
        .:+:(registerUser)
        .:+:(removeUser)
        .handle(errorHandler)
}
