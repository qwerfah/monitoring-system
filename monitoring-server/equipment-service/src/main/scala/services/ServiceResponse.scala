package services

/** Represent result of any service implementation method. */
sealed trait ServiceResponse[+T]

/** Represent successfull result of service method invocation with payload.
  * @param result
  *   Payload of result of service method invocation.
  */
final case class ServiceResult[T](result: T) extends ServiceResponse[T]

/** Represent successfull result of service method invocation without any
  * payload.
  */
case object ServiceEmpty extends ServiceResponse[Nothing]

/** Represent service method invocation which ends with failure.
  * @param error
  *   Error instance.
  */
final case class ServiceError[T](error: Exception) extends ServiceResponse[T]
