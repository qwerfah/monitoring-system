package com.qwerfah.common.resources

import java.time.LocalDateTime

import com.qwerfah.common.Uid
import com.qwerfah.common.http.HttpMethod

final case class RecordRequest(
  userName: Option[String],
  serviceId: String,
  route: String,
  method: HttpMethod,
  status: Int,
  elapsed: Long,
  time: LocalDateTime
)

final case class RecordResponse(
  uid: Uid,
  userName: Option[String],
  serviceId: String,
  route: String,
  method: HttpMethod,
  status: Int,
  elapsed: Long,
  time: LocalDateTime
)
