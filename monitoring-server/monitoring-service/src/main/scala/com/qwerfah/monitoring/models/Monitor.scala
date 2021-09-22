package com.qwerfah.monitoring.models

import com.qwerfah.common.Uid

/** Represent equipment instance state monitor.
  * @param id
  *   Internal monitor identifier.
  * @param uid
  *   External monitor identifier.
  * @param instanceUid
  *   Related equipment instance id.
  * @param name
  *   Monitor name.
  * @param description
  *   Monitor description (optional).
  */
final case class Monitor(
  id: Option[Int],
  uid: Uid,
  instanceUid: Uid,
  name: String,
  description: Option[String]
)
