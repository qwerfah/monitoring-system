package com.qwerfah.session.resources

import com.qwerfah.common.Uid

/** Register new or update exisiting user request data.
  * @param login
  *   User login.
  * @param password
  *   User password.
  * @param role
  *   User role.
  */
final case class UserRequest(
  login: String,
  password: String,
  role: UserRole
)

/** User credentials.
  * @param login
  *   User login.
  * @param password
  *   User password.
  */
final case class Credentials(login: String, password: String)

/** User data returned by service.
  * @param uid
  *   User uid.
  * @param login
  *   User login.
  * @param role
  *   User role.
  */
final case class UserResponse(
  uid: Uid,
  login: String,
  role: UserRole
)
