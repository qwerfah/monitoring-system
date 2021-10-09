package com.qwerfah.equipment.resources

import com.qwerfah.common.Uid

final case class InstanceMonitorResponse(
  uid: Uid,
  modelUid: Uid,
  instanceUid: Uid,
  name: String,
  modelName: String,
  instanceName: String,
  description: Option[String]
)
