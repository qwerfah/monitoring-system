package com.qwerfah.common.services

import scala.util.Try

import com.qwerfah.common.models.Token
import com.qwerfah.common.services.response._

/** Provide basic jwt tokens service functionality. */
trait TokenService[F[_]] {

    /** Validate jwt token.
      * @param token
      *   Jwt token string representation.
      * @return
      *   Subject uid if token is valid, otherwise error.
      */
    def validate(token: String): F[ServiceResponse[String]]

    /** Generate new token pair for given subject.
      *
      * @param id
      *   Subject uid.
      * @return
      *   New access-refresh token pair or error in case of failure.
      */
    def generate(id: String): F[ServiceResponse[Token]]

    /** Refresh access-refresh token pair using given jwt token.
      * @param token
      *   Jwt token string representation.
      * @return
      *   New access-refresh token pair or error in case of failure.
      */
    def refresh(token: String): F[ServiceResponse[Token]]
}
