package com.qwerfah.monitoring.models

import com.qwerfah.common.Uid

/** Represent connection between monitor and tracked equipment instance params.
  * @param monitorUid
  *   Monitor identifier.
  * @param paramUid
  *   Tracked param identifier.
  */
final case class MonitorParam(
  monitorUid: Uid,
  paramUid: Uid,
  isDeleted: Boolean
)
