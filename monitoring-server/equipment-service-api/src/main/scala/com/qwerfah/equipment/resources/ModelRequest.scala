package com.qwerfah.equipment.resources

import com.qwerfah.equipment.models._

final case class ModelRequest(
  name: String,
  description: Option[String]
)
