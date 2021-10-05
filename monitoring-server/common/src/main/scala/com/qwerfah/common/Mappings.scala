package com.qwerfah.common

import com.qwerfah.common.resources._
import com.qwerfah.common.randomUid
import com.qwerfah.common.models.User
import java.security.MessageDigest

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
        def asResponse = UserResponse(user.uid, user.login, user.role)
    }

    implicit class UserSeqToResponseSeqMapping(users: Seq[User]) {
        def asResponse = for { user <- users } yield user.asResponse
    }
}
