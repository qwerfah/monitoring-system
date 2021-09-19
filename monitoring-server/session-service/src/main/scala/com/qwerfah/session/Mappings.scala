package com.qwerfah.session

import com.qwerfah.session.resources._
import com.qwerfah.common.randomUid
import com.qwerfah.session.models.User
import java.security.MessageDigest

object Mappings {
    implicit def userRequestToUser(request: UserRequest) =
        User(
          None,
          randomUid,
          request.login,
          MessageDigest.getInstance("MD5").digest(request.password.getBytes("UTF-8")),
          request.role
        )

    implicit def userToResponse(user: User) =
        UserResponse(user.uid, user.login, user.role)

    implicit def usersToResponses(users: Seq[User]) =
        for { user <- users } yield UserResponse(
          user.uid,
          user.login,
          user.role
        )
}
