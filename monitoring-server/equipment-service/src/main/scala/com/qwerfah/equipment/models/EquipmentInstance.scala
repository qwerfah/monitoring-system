package com.qwerfah.equipment.models

import slick.lifted.Tag
import slick.model.Table

case class EquipmentInstance(
  id: Option[Int],
  uid: Uid,
  modelId: Int,
  name: String,
  description: Option[String],
  status: EquipmentStatus
)
