package com.qwerfah.generator.models

import com.qwerfah.common.Uid

/** Equipment model parameter value associated with specific equipment instance.
  * @param id
  *   Parameter value internal identifier.
  * @param uid
  *   Parameter value external identifier.
  * @param paramUid
  *   Uid of associated parameter.
  * @param instanceUid
  *   Uid of associated equipment instance.
  * @param value
  *   Parameter value string representation.
  */
final case class ParamValue(
  id: Option[Int],
  uid: Uid,
  paramUid: Uid,
  instanceUid: Uid,
  value: String
)
