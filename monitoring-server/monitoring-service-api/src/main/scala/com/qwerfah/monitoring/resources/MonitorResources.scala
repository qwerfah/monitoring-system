package com.qwerfah.monitoring.resources

import com.qwerfah.common.Uid

final case class AddMonitorRequest(
  instanceUid: Uid,
  name: String,
  description: Option[String]
)

final case class UpdateMonitorRequest(
  name: String,
  description: Option[String]
)

final case class MonitorResponse(
  uid: Uid,
  instanceUid: Uid,
  name: String,
  description: Option[String]
)

final case class MonitorParamRequest(paramUid: Uid)

final case class MonitorParamResponse(
  monitorUid: Uid,
  paramUid: Uid,
  modelUid: Option[Uid],
  name: Option[String],
  measurmentUnits: Option[String]
)
