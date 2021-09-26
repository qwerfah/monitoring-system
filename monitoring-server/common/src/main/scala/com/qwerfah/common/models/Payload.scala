package com.qwerfah.common.models

import com.qwerfah.common.Uid
import com.qwerfah.common.resources.UserRole

/** Contain token payload, specifically subject identifier and subject role in
  * system.
  * @param uid
  *   Sublect identifier.
  * @param role
  *   Subject role.
  */
final case class Payload(uid: Uid, role: UserRole)
