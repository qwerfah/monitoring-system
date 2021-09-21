package com.qwerfah.common.services.response

import com.qwerfah.common.exceptions._

/** Represent result of any service implementation method. */
sealed trait ServiceResponse[+T]

/** Represent successfull result of service method invocation with payload.
  * @param result
  *   Payload of result of service method invocation.
  */
final case class ObjectResponse[T](result: T) extends ServiceResponse[T]

/** Represent result of service method invocation which ends with failure.
  * @param error
  *   Error instance.
  */
sealed abstract class ErrorResponse(message: ErrorMessage)
  extends Exception
  with ServiceResponse[Nothing] {
    override def getMessage = message.getMessage
}

/** Returns when service operation can't can't get the required resource.
  * Corresponds to 404 status code.
  * @param message
  *   Error description.
  */
final case class NotFoundResponse(message: ErrorMessage)
  extends ErrorResponse(message)

/** Returns when authorization operation results in error. Corresponds to 400
  * status code.
  * @param message
  *   Error description.
  */
final case class BadAuthResponse(message: ErrorMessage)
  extends ErrorResponse(message)

/** Returns when the request to the remote server inside of service method
  * invocation ends with 503 status code. Corresponds to 502 status code.
  * @param message
  *   Error description.
  */
final case class BadGatewayResponse(message: ErrorMessage)
  extends ErrorResponse(message)

/** Returns when service operation ends with unprocessed error. Corresponds to
  * 500 status code.
  * @param message
  *   Error description.
  */
final case class InternalErrorResponse(message: ErrorMessage)
  extends ErrorResponse(message)

/** Returns when the successful request to the remote server inside of service
  * method invocation returns result that does not corresponds to expected.
  * Corresponds to 422 status code.
  * @param message
  *   Error description.
  */
final case class UnprocessableResponse(message: ErrorMessage)
  extends ErrorResponse(message)

/** Returns when the request to the remote server inside of service method
  * invocation returns unexpected status code. Corresponds to 520 status code.
  * @param message
  *   Error description.
  */
final case class UnknownErrorResponse(message: ErrorMessage)
  extends ErrorResponse(message)
