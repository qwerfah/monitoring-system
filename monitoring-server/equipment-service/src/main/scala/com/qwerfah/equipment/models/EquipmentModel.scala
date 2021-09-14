package com.qwerfah.equipment.models

import com.qwerfah.equipment.resources._

case class EquipmentModel(
  id: Option[Int],
  uid: Uid,
  name: String,
  description: Option[String]
)
