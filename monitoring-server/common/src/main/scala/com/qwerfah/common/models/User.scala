package com.qwerfah.common.models

import com.qwerfah.common.Uid
import com.qwerfah.common.resources.UserRole

/** Represent system authorization unit (may be both external user and service).
  * @param id
  *   User internal identifier.
  * @param uid
  *   User external identifier.
  * @param login
  *   User login (service name).
  * @param password
  *   User password (service secret) hash.
  * @param role
  *   User (service) role.
  */
final case class User(
  id: Option[Int],
  uid: Uid,
  login: String,
  password: Array[Byte],
  role: UserRole
)
