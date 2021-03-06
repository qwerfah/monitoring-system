package com.qwerfah.common.services

import scala.util.Try

import com.qwerfah.common.models.{Token, Payload}
import com.qwerfah.common.services.response._
import com.qwerfah.common.resources.{Credentials, UserResponse}
import com.qwerfah.common.Uid

/** Provide basic jwt tokens service functionality. */
trait TokenService[F[_]] {

    /** Validate jwt token.
      * @param token
      *   Jwt token string representation.
      * @return
      *   Subject uid if token is valid, otherwise error.
      */
    def verify(token: String): F[ServiceResponse[Payload]]

    /** Authorize user with given credentials.
      * @param credentials
      *   User credentials.
      * @return
      *   User tokens.
      */
    def userLogin(credentials: Credentials): F[ServiceResponse[UserResponse]]

    /** Authorize external service with given credentials.
      * @param credentials
      *   Service credentials.
      * @return
      *   Service tokens.
      */
    def serviceLogin(credentials: Credentials): F[ServiceResponse[Token]]

    /** Refresh access-refresh token pair using given refresh token.
      * @param token
      *   Refresh token.
      * @return
      *   New access-refresh token pair or error in case of failure.
      */
    def refresh(token: String): F[ServiceResponse[Token]]
}
