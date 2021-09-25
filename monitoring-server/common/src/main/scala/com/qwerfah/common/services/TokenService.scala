package com.qwerfah.common.services

import scala.util.Try

import com.qwerfah.common.models.Token
import com.qwerfah.common.services.response._
import com.qwerfah.common.resources.Credentials
import com.qwerfah.common.Uid

/** Provide basic jwt tokens service functionality. */
trait TokenService[F[_]] {

    /** Validate jwt token.
      * @param token
      *   Jwt token string representation.
      * @return
      *   Subject uid if token is valid, otherwise error.
      */
    def verify(token: String): F[ServiceResponse[Uid]]

    /** Authorize user with given credentials.
      * @param credentials
      *   User credentials.
      * @return
      *   User tokens.
      */
    def login(credentials: Credentials): F[ServiceResponse[Token]]

    /** Refresh access-refresh token pair for given user.
      * @param uid
      *   User uid.
      * @return
      *   New access-refresh token pair or error in case of failure.
      */
    def refresh(uid: Uid): F[ServiceResponse[Token]]
}
