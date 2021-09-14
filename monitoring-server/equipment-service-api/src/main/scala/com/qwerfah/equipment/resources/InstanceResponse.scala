package com.qwerfah.equipment.resources

final case class InstanceResponse(
  uid: Uid,
  modelUid: Uid,
  name: String,
  description: Option[String],
  status: EquipmentStatus
)
