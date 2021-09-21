package com.qwerfah.session.services

import com.qwerfah.session.models._
import com.qwerfah.session.resources._
import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid
import com.qwerfah.common.models.Token

/** Describe user service funcionality. */
trait UserService[F[_]] {

    /** Register new user.
      * @param request
      *   User data.
      * @return
      *   Registered user data.
      */
    def register(request: UserRequest): F[ServiceResponse[UserResponse]]

    /** Authorize user with given credentials.
      * @param credentials
      *   User credentials.
      * @return
      *   User tokens.
      */
    def login(credentials: Credentials): F[ServiceResponse[Token]]

    /** Refresh user tokens using refresh token.
      * @param token
      *   Refresh token (not expired).
      * @return
      *   New access and refresh tokens.
      */
    def refresh(token: String): F[ServiceResponse[Token]]

    /** Get all registered users.
      * @return
      *   All registered users.
      */
    def getAll: F[ServiceResponse[Seq[UserResponse]]]

    /** Get registered user by its uid.
      * @param uid
      *   User uid.
      * @return
      *   Registered user with given uid.
      */
    def get(uid: Uid): F[ServiceResponse[UserResponse]]

    /** Update registered user by its uid.
      * @param uid
      *   User uid.
      * @param request
      *   New user data.
      * @return
      */
    def update(
      uid: Uid,
      request: UserRequest
    ): F[ServiceResponse[ResponseMessage]]
    
    def remove(uid: Uid): F[ServiceResponse[ResponseMessage]]
}
