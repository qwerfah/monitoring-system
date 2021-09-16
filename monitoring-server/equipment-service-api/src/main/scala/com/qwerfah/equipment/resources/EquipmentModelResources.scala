package com.qwerfah.equipment.resources

import com.qwerfah.common.Uid

final case class ModelRequest(
  name: String,
  description: Option[String]
)

final case class ModelResponse(
  uid: Uid,
  name: String,
  description: Option[String]
)
