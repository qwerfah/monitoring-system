package com.qwerfah.common.models

/** User jwt tokens.
  * @param access
  *   Access token.
  * @param refresh
  *   Refresh token.
  */
final case class Token(access: String = "", refresh: String = "")
