package com.qwerfah.session.services

import com.qwerfah.common.models._
import com.qwerfah.common.resources._
import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid

/** Describe user service funcionality. */
trait UserService[F[_]] {

    /** Register new user (only EquipmentUser role accepted).
      * @param request
      *   User data.
      * @return
      *   Registered user data.
      */
    def register(request: UserRequest): F[ServiceResponse[UserResponse]]

    /** Add new user (admin action).
      * @param request
      *   User data.
      * @return
      *   Registered user data.
      */
    def add(request: UserRequest): F[ServiceResponse[UserResponse]]

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

    /** Remove user by its uid.
      * @param uid
      *   User identifier.
      * @return
      *   Description of the operation result.
      */
    def remove(uid: Uid): F[ServiceResponse[ResponseMessage]]
}
