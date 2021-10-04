package com.qwerfah.equipment.models

import com.qwerfah.common.Uid

case class EquipmentModel(
  id: Option[Int],
  uid: Uid,
  name: String,
  description: Option[String],
  isDeleted: Boolean
)
