package com.qwerfah.equipment.models

import com.qwerfah.equipment.resources._

case class EquipmentInstance(
  id: Option[Int],
  uid: Uid,
  modelId: Int,
  name: String,
  description: Option[String],
  status: EquipmentStatus
)
