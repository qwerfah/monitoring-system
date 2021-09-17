package com.qwerfah.session.services

import com.qwerfah.session.models._
import com.qwerfah.session.resources._
import com.qwerfah.common.services.ServiceResponse
import com.qwerfah.common.Uid

trait UserService[F[_]] {
    def register(user: UserRequest): F[ServiceResponse[UserResponse]]
    def login(login: String, password: String): F[ServiceResponse[UserResponse]]
    def refresh()
    def getAll: F[ServiceResponse[Seq[UserResponse]]]
    def get(uid: Uid): F[ServiceResponse[UserResponse]]
    def update(uid: Uid, user: UserRequest): F[ServiceResponse[String]]
    def remove(uid: Uid): F[ServiceResponse[String]]
}
