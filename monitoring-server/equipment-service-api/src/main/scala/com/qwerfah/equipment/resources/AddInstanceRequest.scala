package com.qwerfah.equipment.resources

final case class AddInstanceRequest(
  modelUid: Uid,
  name: String,
  description: Option[String],
  status: EquipmentStatus
)
