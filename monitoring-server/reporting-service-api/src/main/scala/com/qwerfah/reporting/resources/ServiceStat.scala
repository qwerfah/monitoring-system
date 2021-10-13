package com.qwerfah.reporting.resources

/** System service statistics.
  * @param serviceId
  *   String serivce identifier.
  * @param requestCount
  *   Total incoming requests count.
  * @param successCount
  *   Number of successfully finished requests (2xx status code).
  * @param userErrorCount
  *   Number of requests with 4xx status code in response.
  * @param serverErrorCount
  *   Number of requests with 5xx status code in response.
  * @param avgRequestTime
  *   Average single request processing time.
  */
final case class ServiceStat(
  serviceId: String,
  requestCount: Int,
  successCount: Int,
  userErrorCount: Int,
  serverErrorCount: Int,
  avgRequestTime: Float
)
