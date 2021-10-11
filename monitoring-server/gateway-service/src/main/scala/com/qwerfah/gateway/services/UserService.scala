package com.qwerfah.gateway.services

import com.twitter.finagle.http.Response

import com.qwerfah.common.Uid
import com.qwerfah.common.resources.UserRequest

trait UserService[F[_]] {
    def getUsers(): F[Response]

    def addUser(request: UserRequest): F[Response]

    def registerUser(request: UserRequest): F[Response]

    def removeUser(userUid: Uid): F[Response]
}