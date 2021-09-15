package com.qwerfah.equipment.resources

final case class UpdateInstanceRequest(
  name: String,
  description: Option[String],
  status: EquipmentStatus
)
