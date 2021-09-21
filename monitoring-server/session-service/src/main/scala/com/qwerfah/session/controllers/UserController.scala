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

import com.qwerfah.session.services._
import com.qwerfah.session.repos.slick._
import com.qwerfah.session.models._
import com.qwerfah.session.resources._
import com.qwerfah.session.Startup
import com.qwerfah.session.json.Decoders
import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.common.services._
import com.qwerfah.common.services.response._
import com.qwerfah.common.controllers.Controller

object UserController extends Controller {
    import Startup._
    import Decoders._

    private val userService = implicitly[UserService[Future]]
    private val tokenService = implicitly[TokenService[Future]]

    private def authorize[A](
      token: Option[String],
      action: String => Future[Output[A]]
    ): Future[Output[A]] = token match {
        case Some(value) =>
            tokenService.validate(value) flatMap {
                case ObjectResponse(result) => action(result)
                case e: NotFoundResponse =>
                    Future.successful(
                      Unauthorized(e)
                    )
            }
        case None => Future.successful(Unauthorized(NoTokenHeader))
    }

    private val getUsers = get("users" :: headerOption("Authorization")) {
        token: Option[String] =>
            authorize(
              token,
              _ => for { result <- userService.getAll } yield result.asOutput
            )
    }

    private val getUser =
        get("users" :: path[Uid] :: headerOption("Authorization")) {
            (uid: Uid, token: Option[String]) =>
                authorize(
                  token,
                  _ =>
                      for {
                          result <- userService.get(uid)
                      } yield result.asOutput
                )
        }

    private val register =
        post("users" :: "register" :: jsonBody[UserRequest]) {
            request: UserRequest =>
                for {
                    result <- userService.register(request)
                } yield result.asOutput
        }

    private val login =
        post("users" :: "login" :: jsonBody[Credentials]) {
            credentials: Credentials =>
                for {
                    result <- userService.login(credentials)
                } yield result.asOutput
        }

    private val updateUser =
        patch(
          "users" :: path[Uid] :: jsonBody[UserRequest] :: headerOption(
            "Authorization"
          )
        ) { (uid: Uid, request: UserRequest, token: Option[String]) =>
            authorize(
              token,
              _ =>
                  for {
                      result <- userService.update(uid, request)
                  } yield result.asOutput
            )
        }

    private val deleteUser =
        delete("users" :: path[Uid] :: headerOption("Authorization")) {
            (uid: Uid, token: Option[String]) =>
                authorize(
                  token,
                  _ =>
                      for {
                          result <- userService.remove(uid)
                      } yield result.asOutput
                )
        }

    val api =
        (getUsers :+: getUser :+: register :+: login :+: updateUser :+: deleteUser)
            .handle(errorHandler)
}
