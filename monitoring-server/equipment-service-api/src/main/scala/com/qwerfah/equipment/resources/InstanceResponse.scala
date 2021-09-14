package com.qwerfah.equipment.resources

final case class InstanceResponse(
  uid: Uid,
  name: String,
  description: Option[String],
  status: EquipmentStatus
)
