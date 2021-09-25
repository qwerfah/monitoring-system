package com.qwerfah.gateway.services

import com.qwerfah.common.services.response.ServiceResponse
import com.qwerfah.common.resources.Credentials
import com.qwerfah.common.models.Token

/** Provide methods for interaction with remote session service. */
trait SessionService[F[_]] {

    /** Validate jwt token at the remote session service.
      * @param token
      *   Jwt token.
      * @return
      *   Successfull service response with subject identifier, otherwise error
      *   response.
      */
    def validate(token: String): F[ServiceResponse[String]]

    /** Login with given credentials at the remote session service.
      * @param creds
      *   User credentials.
      * @return
      *   Successfull service response with authorized user tokens, otherwise
      *   error response.
      */
    def login(creds: Credentials): F[ServiceResponse[Token]]

    /** Refresh tokens with given refresh token at the remote session service.
      * @param token
      *   Refresh token.
      * @return
      *   Successfull service response with new user tokens, otherwise error
      *   response.
      */
    def refresh(token: String): F[ServiceResponse[Token]]
}
