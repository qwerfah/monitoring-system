package com.qwerfah.session.resources

import com.qwerfah.common.Uid

final case class UserRequest(login: String, password: String, role: UserRole)

final case class UserResponse(
  uid: Uid,
  login: String,
  role: UserRole
)
