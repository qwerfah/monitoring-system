package com.qwerfah.common.services.response

import com.qwerfah.common.exceptions._

/** Represent result of any service implementation method. */
sealed trait ServiceResponse[+T]

/** Represent successfull result of service method invocation with payload.
  * @param result
  *   Payload of result of service method invocation.
  */
final case class ObjectResponse[T](result: T) extends ServiceResponse[T]

/** Represent service method invocation which ends with failure.
  * @param error
  *   Error instance.
  */
sealed trait ErrorResponse extends Exception with ServiceResponse[Nothing]

final case class NotFoundResponse(message: ErrorMessage) extends ErrorResponse {
    override def getMessage = message.getMessage
}

final case class BadAuthResponse(message: ErrorMessage) extends ErrorResponse {
    override def getMessage = message.getMessage
}
