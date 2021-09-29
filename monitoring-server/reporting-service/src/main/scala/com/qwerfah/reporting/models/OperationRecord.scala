package com.qwerfah.reporting.models

import java.time.LocalDateTime

import com.qwerfah.common.Uid
import com.qwerfah.common.http.HttpMethod

/** Record of system service operation.
  * @param id
  *   Internal record identifier.
  * @param uid
  *   External record identifier.
  * @param serviceId
  *   String identifier of system service that preformed operation.
  * @param route
  *   Service method route.
  * @param method
  *   Http method of request that was recived by system service that performed
  *   operation.
  * @param status
  *   Status code of system service response.
  * @param time
  *   Time of the operation.
  */
final case class OperationRecord(
  id: Option[Int],
  uid: Uid,
  serviceId: String,
  route: String,
  method: HttpMethod,
  status: Int,
  time: LocalDateTime
)
