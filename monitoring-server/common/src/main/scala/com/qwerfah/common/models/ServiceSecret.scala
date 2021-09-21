package com.qwerfah.common.models

/** Service secret using for interservice authorization.
  * @param id
  *   Service identifier.
  * @param secret
  *   Service secret hash.
  */
final case class ServiceSecret(id: String, secret: Array[Byte])
