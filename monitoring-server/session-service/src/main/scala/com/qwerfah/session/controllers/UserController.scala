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
import cats.effect.IO
import com.twitter.finagle.http.Status
import com.twitter.finagle.SimpleFilter
import com.twitter.finagle.Service

import com.qwerfah.common.repos.TokenRepo
import scala.util.Try

object UserController {
    import Startup._
    import Decoders._

    private val userService = implicitly[UserService[Future]]
    private val tokenService = implicitly[TokenService[Future]]

    private val getUsers = get("users") {
        for { result <- userService.getAll } yield result match {
            case ObjectResponse(users) => Ok(users)
        }
    } handle { case e: Exception => InternalServerError(e) }

    private val getUser = get("users" :: path[Uid]) { uid: Uid =>
        for { result <- userService.get(uid) } yield result match {
            case ObjectResponse(user) => Ok(user)
            case EmptyResponse        => NotFound(NoUserException(uid))
        }
    } handle { case e: Exception => InternalServerError(e) }

    private val register =
        post("users" :: "register" :: jsonBody[UserRequest]) {
            request: UserRequest =>
                for {
                    result <- userService.register(request)
                } yield result match {
                    case ObjectResponse(user) => Ok(user)
                }
        } handle {
            case e: InvalidJsonBodyException => BadRequest(e)
            case e: Exception                => InternalServerError(e)
        }

    private val login =
        post("users" :: "login" :: jsonBody[Credentials]) {
            credentials: Credentials =>
                for {
                    result <- userService.login(credentials)
                } yield result match {
                    case ObjectResponse(token) => Ok(token)
                    case EmptyResponse =>
                        NotFound(InvalidCredentialsException)
                }
        } handle {
            case e: InvalidJsonBodyException => BadRequest(e)
            case e: Exception                => InternalServerError(e)
        }

    val auth = header("Authorization") map { token =>
        tokenService.validate(token) map { case ObjectResponse(result) =>
            Ok(result)
        }
    }

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

    private val refresh =
        post("users" :: "refresh" :: header("Authorization")) { token: String =>
            tokenService.validate(token) flatMap {
                case ObjectResponse(uid) => userService.refresh(uid)
                case ErrorResponse(error) =>
                    Future.successful(ErrorResponse(error))
            } map {
                case ObjectResponse(token) => Ok(token)
                case EmptyResponse =>
                    NotFound(NoTokenUserException)
                case ErrorResponse(error) =>
                    Unauthorized(new Exception(error.getMessage))
            }
        } handle { case e: Exception => InternalServerError(e) }

    private val updateUser =
        patch("users" :: path[Uid] :: jsonBody[UserRequest]) {
            (uid: Uid, request: UserRequest) =>
                for {
                    result <- userService.update(uid, request)
                } yield result match {
                    case response: StringResponse => Ok(response)
                    case EmptyResponse => NotFound(NoUserException(uid))
                }
        } handle {
            case e: InvalidJsonBodyException => BadRequest(e)
            case e: Exception                => InternalServerError(e)
        }

    private val deleteUser =
        delete("users" :: path[Uid]) { uid: Uid =>
            for {
                result <- userService.remove(uid)
            } yield result match {
                case response: StringResponse => Ok(response)
                case EmptyResponse            => NotFound(NoUserException(uid))
            }
        } handle { case e: Exception => InternalServerError(e) }

    val authFilter = new AuthFilter

    val api =
        getUsers :+: getUser :+: register :+: login :+: refresh :+: updateUser :+: deleteUser
}
