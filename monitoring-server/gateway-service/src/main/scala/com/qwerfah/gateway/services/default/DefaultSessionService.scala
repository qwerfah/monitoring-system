package com.qwerfah.gateway.services.default

import cats.Monad
import cats.implicits._

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import com.qwerfah.gateway.json.Decoders

import com.qwerfah.common.services.TokenService
import com.qwerfah.common.http._
import com.qwerfah.common.services.response._
import com.qwerfah.common.resources.Credentials
import com.qwerfah.common.models._
import com.qwerfah.common.Uid

class DefaultSessionService[F[_]: Monad](client: HttpClient[F])
  extends TokenService[F] {
    import Decoders._

    override def verify(token: String): F[ServiceResponse[Payload]] =
        client.sendAndDecode(
          HttpMethod.Post,
          "/session/verify",
          None,
          Some(token)
        )

    override def login(credentials: Credentials): F[ServiceResponse[Token]] =
        client.sendAndDecode(
          HttpMethod.Post,
          "/session/login",
          Some(credentials.asJson.toString)
        )

    override def refresh(token: String): F[ServiceResponse[Token]] =
        client.sendAndDecode(
          HttpMethod.Post,
          "/session/refresh",
          None,
          Some(token)
        )

}
