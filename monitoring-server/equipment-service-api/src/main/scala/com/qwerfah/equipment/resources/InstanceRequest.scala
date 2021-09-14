package com.qwerfah.equipment.resources

final case class InstanceRequest(
  modelUid: Uid,
  name: String,
  description: Option[String],
  status: EquipmentStatus
)
