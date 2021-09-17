package com.qwerfah.common.services

/** Represent result of any service implementation method. */
sealed trait ServiceResponse[+T]

/** Represent successfull result of service method invocation with payload.
  * @param result
  *   Payload of result of service method invocation.
  */
final case class ObjectResponse[T](result: T) extends ServiceResponse[T]

/** Represent successfull result of service method invocation with string
  * message response.
  * @param message
  *   Response message.
  */
final case class StringResponse(message: String) extends ServiceResponse[String]

/** Represent successfull result of service method invocation without any
  * payload.
  */
case object EmptyResponse extends ServiceResponse[Nothing]

/** Represent service method invocation which ends with failure.
  * @param error
  *   Error instance.
  */
final case class ErrorResponse[T](error: Exception) extends ServiceResponse[T]
