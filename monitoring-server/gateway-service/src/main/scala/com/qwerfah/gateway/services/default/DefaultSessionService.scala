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
import com.qwerfah.common.resources.{Credentials, UserResponse}
import com.qwerfah.common.models._
import com.qwerfah.common.Uid
import com.qwerfah.common.exceptions.InsufficientRole
import com.qwerfah.common.util.Conversions._

class DefaultSessionService[F[_]: Monad](client: HttpClient[F])
  extends TokenService[F] {
    import Decoders._

    override def verify(token: String): F[ServiceResponse[Payload]] =
        client.sendAndDecode(
          HttpMethod.Post,
          "/api/session/verify",
          None,
          Some(token)
        )

    override def userLogin(
      credentials: Credentials
    ): F[ServiceResponse[UserResponse]] =
        client.sendAndDecode(
          HttpMethod.Post,
          "/api/session/login",
          Some(credentials.asJson.toString)
        )

    override def serviceLogin(
      credentials: Credentials
    ): F[ServiceResponse[Token]] = Monad[F].pure(InsufficientRole.as403)

    override def refresh(token: String): F[ServiceResponse[Token]] =
        client.sendAndDecode(
          HttpMethod.Post,
          "/api/session/refresh",
          None,
          Some(token)
        )

}
