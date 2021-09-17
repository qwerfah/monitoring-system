package com.qwerfah.equipment.resources

import com.qwerfah.common.Uid

final case class AddInstanceRequest(
  modelUid: Uid,
  name: String,
  description: Option[String],
  status: EquipmentStatus
)

final case class UpdateInstanceRequest(
  name: String,
  description: Option[String],
  status: EquipmentStatus
)

final case class InstanceResponse(
  uid: Uid,
  modelUid: Uid,
  name: String,
  description: Option[String],
  status: EquipmentStatus
)