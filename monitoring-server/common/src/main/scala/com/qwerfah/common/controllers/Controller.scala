package com.qwerfah.common.controllers

import io.finch._

import com.qwerfah.common.services.response._
import com.qwerfah.common.exceptions._

/** Provide basic endpoint controller functionality. */
trait Controller {

    /** Provide wrappers for service response.
      * @param response
      *   Service response instance.
      */
    implicit class ResponseWrapper[A](response: ServiceResponse[A]) {

        /** Wrap service response as finch endpoint Output instance.
          * @return
          *   Finch endpoint Output instance build from service response.
          */
        def asOutput: Output[A] =
            response match {
                case ObjectResponse(result) => Ok(result)
                case e: NotFoundResponse    => NotFound(e)
                case e: BadAuthResponse     => Unauthorized(e)
            }
    }

    /** Default handler for errors that occur during request processing. */
    def errorHandler[A]: PartialFunction[Throwable, Output[A]] = {
        case e: InvalidJsonBodyException => BadRequest(e)
        case e: Exception                => InternalServerError(e)
    }
}
