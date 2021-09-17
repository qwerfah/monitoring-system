package com.qwerfah.session.models

import com.qwerfah.common.Uid
import com.qwerfah.session.resources.UserRole

final case class User(
  id: Option[Int],
  uid: Uid,
  login: String,
  password: Array[Byte],
  role: UserRole
)
