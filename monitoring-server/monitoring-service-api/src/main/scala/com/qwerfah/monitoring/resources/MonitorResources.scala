package com.qwerfah.monitoring.resources

import com.qwerfah.common.Uid

final case class AddMonitorRequest(
  name: String,
  description: Option[String],
  params: Seq[Uid]
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

final case class MonitorParamsRequest(params: Seq[Uid])
