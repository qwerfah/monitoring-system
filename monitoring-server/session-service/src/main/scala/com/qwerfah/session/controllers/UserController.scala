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
import io.finch.internal.Mapper
import cats.effect.IOApp

object UserController {
    import Startup._
    import Decoders._

    private val userService = implicitly[UserService[Future]]
    private val tokenService = implicitly[TokenService[Future]]

    private def authorize[A](
      token: Option[String],
      action: String => Future[Output[A]]
    ) = token match {
        case Some(value) =>
            tokenService.validate(value) flatMap {
                case ObjectResponse(result) => action(result)
                case ErrorResponse(error) =>
                    Future.successful(
                      Unauthorized(new Exception(error.getMessage))
                    )
            }
        case None => Future.successful(Unauthorized(NoTokenHeaderException))
    }

    private val getUsers = get("users" :: headerOption("Authorization")) {
        token: Option[String] =>
            authorize(
              token,
              _ =>
                  for { result <- userService.getAll } yield result match {
                      case ObjectResponse(users) => Ok(users)
                  }
            )
    } handle { case e: Exception => InternalServerError(e) }

    private val getUser =
        get("users" :: path[Uid] :: headerOption("Authorization")) {
            (uid: Uid, token: Option[String]) =>
                authorize(
                  token,
                  _ =>
                      for {
                          result <- userService.get(uid)
                      } yield result match {
                          case ObjectResponse(user) => Ok(user)
                          case EmptyResponse => NotFound(NoUserException(uid))
                      }
                )
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

    private val refresh =
        post("users" :: "refresh" :: headerOption("Authorization")) {
            token: Option[String] =>
                authorize(
                  token,
                  uid =>
                      userService.refresh(uid) map {
                          case ObjectResponse(token) => Ok(token)
                          case EmptyResponse => NotFound(NoTokenUserException)
                          case ErrorResponse(error) =>
                              Unauthorized(new Exception(error.getMessage))
                      }
                )
        } handle { case e: Exception => InternalServerError(e) }

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
                  } yield result match {
                      case response: StringResponse => Ok(response)
                      case EmptyResponse => NotFound(NoUserException(uid))
                  }
            )
        } handle {
            case e: InvalidJsonBodyException => BadRequest(e)
            case e: Exception                => InternalServerError(e)
        }

    private val deleteUser =
        delete("users" :: path[Uid] :: headerOption("Authorization")) {
            (uid: Uid, token: Option[String]) =>
                authorize(
                  token,
                  _ =>
                      for {
                          result <- userService.remove(uid)
                      } yield result match {
                          case response: StringResponse => Ok(response)
                          case EmptyResponse => NotFound(NoUserException(uid))
                      }
                )
        } handle { case e: Exception => InternalServerError(e) }

    val api =
        getUsers :+: getUser :+: register :+: login :+: refresh :+: updateUser :+: deleteUser
}
