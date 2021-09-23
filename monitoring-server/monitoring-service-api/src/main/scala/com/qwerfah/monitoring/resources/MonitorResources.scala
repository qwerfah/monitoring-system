package com.qwerfah.monitoring.resources

import com.qwerfah.common.Uid

final case class MonitorRequest(
  instanceUid: Uid,
  name: String,
  description: Option[String]
)

final case class MonitorResponse(
  uid: Uid,
  instanceUid: Uid,
  name: String,
  description: Option[String]
)
