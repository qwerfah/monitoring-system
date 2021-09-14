package com.qwerfah.equipment.resources

final case class InstanceRequest(
  name: String,
  description: Option[String],
  status: EquipmentStatus
)
