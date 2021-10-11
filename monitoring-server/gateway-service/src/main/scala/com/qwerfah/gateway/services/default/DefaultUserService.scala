package com.qwerfah.gateway.services.default

import com.twitter.finagle.http.Response

import cats.Monad
import cats.implicits._

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import com.qwerfah.gateway.services.UserService

import com.qwerfah.common.Uid
import com.qwerfah.common.http.HttpClient
import com.qwerfah.common.http.HttpMethod
import com.qwerfah.common.resources.UserRequest

class DefaultUserService[F[_]: Monad](sessionClient: HttpClient[F])
  extends UserService[F] {

    override def getUsers(): F[Response] =
        sessionClient.send(HttpMethod.Get, "/api/users")

    override def registerUser(request: UserRequest): F[Response] =
        sessionClient.send(
          HttpMethod.Post,
          "/api/users/register",
          Some(request.asJson.toString)
        )

    override def addUser(request: UserRequest): F[Response] = sessionClient
        .send(
          HttpMethod.Post,
          "/api/users",
          Some(request.asJson.toString)
        )

    override def removeUser(userUid: Uid): F[Response] =
        sessionClient.send(HttpMethod.Delete, s"/api/users/$userUid")

}
