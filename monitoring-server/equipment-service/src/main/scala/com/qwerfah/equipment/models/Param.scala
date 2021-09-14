package com.qwerfah.equipment.models

import com.qwerfah.equipment.resources._

case class Param(
  id: Option[Int],
  uid: Uid,
  modelId: Int,
  name: String,
  measurmentUnits: Option[String]
)
