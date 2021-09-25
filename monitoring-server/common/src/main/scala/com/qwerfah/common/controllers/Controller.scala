package com.qwerfah.common.controllers

import com.twitter.util.Future

import io.finch._

import cats.Monad
import cats.implicits._

import com.qwerfah.common.services.response._
import com.qwerfah.common.exceptions._
import com.qwerfah.common.services.TokenService

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
    protected def errorHandler[A]: PartialFunction[Throwable, Output[A]] = {
        case e: InvalidJsonBodyException => BadRequest(e)
        case e: Exception                => InternalServerError(e)
    }

    /** Provide simple jwt validation of authorize header using token service.
      * Also wraps result of service action with finch Output instance.
      * @param header
      *   Content of Authorize header.
      * @param action
      *   Service action that must be performed after token validation.
      * @param tokenService
      *   Token service instance using for jwt token validation.
      * @return
      *   Future placeholder for action result wrapped with finch Output.
      */
    protected def authorize[F[_]: Monad, A](
      header: Option[String],
      action: String => F[ServiceResponse[A]]
    )(implicit tokenService: TokenService[F]): F[Output[A]] = header match {
        case Some(token) if token.toLowerCase.contains("bearer") =>
            tokenService.validate(token) flatMap {
                case ObjectResponse(result) => action(result) map { _.asOutput }
                case e: ErrorResponse       => Monad[F].pure(Unauthorized(e))
            }
        case Some(_) => Monad[F].pure(Unauthorized(InvalidTokenHeader))
        case None    => Monad[F].pure(Unauthorized(NoTokenHeader))
    }
}
