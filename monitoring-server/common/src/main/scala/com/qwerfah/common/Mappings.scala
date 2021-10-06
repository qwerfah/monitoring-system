package com.qwerfah.common

import java.security.MessageDigest

import com.qwerfah.common.resources._
import com.qwerfah.common.randomUid
import com.qwerfah.common.models.User
import com.qwerfah.common.models.Token

object Mappings {
    implicit class RequestToUserMapping(request: UserRequest) {
        def asUser = User(
          None,
          randomUid,
          request.login,
          MessageDigest
              .getInstance("MD5")
              .digest(request.password.getBytes("UTF-8")),
          request.role,
          false
        )
    }

    implicit class UserToResponseMapping(user: User) {
        def asResponse = UserResponse(user.uid, user.login, user.role, None)
        def asResponse(token: Token) =
            UserResponse(user.uid, user.login, user.role, Some(token))
    }

    implicit class UserSeqToResponseSeqMapping(users: Seq[User]) {
        def asResponse = for { user <- users } yield user.asResponse
    }
}
