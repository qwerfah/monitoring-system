package com.qwerfah.equipment.resources

final case class ModelRequest(
  name: String,
  description: Option[String]
)

final case class ModelResponse(
  uid: Uid,
  name: String,
  description: Option[String]
)
