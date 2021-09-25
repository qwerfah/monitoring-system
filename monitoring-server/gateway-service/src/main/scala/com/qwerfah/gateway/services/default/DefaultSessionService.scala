package com.qwerfah.gateway.services.default

import cats.Monad
import cats.implicits._

import com.qwerfah.gateway.services.SessionService
import com.qwerfah.common.http._
import com.qwerfah.common.services.response.ServiceResponse
import com.qwerfah.common.models.Token
import com.qwerfah.common.resources.Credentials
import com.qwerfah.common.services.response.ServiceResponse
import com.qwerfah.common.models.Token
import com.qwerfah.common.services.response.ServiceResponse
import com.qwerfah.gateway.json.Decoders

class DefaultSessionService[F[_]: Monad](client: HttpClient[F])
  extends SessionService[F] {
    import Decoders._

    override def validate(token: String): F[ServiceResponse[String]] =
        client.sendAndDecode(Post, "/token/validate")

    override def login(creds: Credentials): F[ServiceResponse[Token]] =
        client.sendAndDecode(Post, "/users/login")

    override def refresh(token: String): F[ServiceResponse[Token]] =
        client.sendAndDecode(Post, "/tokens/refresh")

}
