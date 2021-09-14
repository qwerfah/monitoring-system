package com.qwerfah.equipment.models

import com.qwerfah.equipment.resources._

case class Param(
  id: Option[Int],
  uid: Uid,
  modelUid: Uid,
  name: String,
  measurmentUnits: Option[String]
)
