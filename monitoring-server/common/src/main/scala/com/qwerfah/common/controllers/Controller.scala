package com.qwerfah.common.controllers

import com.twitter.util.Future
import com.twitter.io.Buf

import io.finch._

import cats.Monad
import cats.implicits._

import com.qwerfah.common.services.response._
import com.qwerfah.common.exceptions._
import com.qwerfah.common.services.TokenService
import com.qwerfah.common.Uid
import com.qwerfah.common.resources.UserRole
import com.qwerfah.common.models.Payload

/** Provide basic endpoint controller functionality. */
trait Controller {

    /** Provide wrappers for service response.
      * @param response
      *   Service response instance.
      */
    implicit class ResponseWrapper[A](response: ServiceResponse[A]) {

        /** Wrap service response as finch endpoint Output instance.
          * @return
          *   Finch endpoint [[Output]] instance build from service response.
          */
        def asOutput[B >: A]: Output[A] = response match {
            case OkResponse(result)      => Ok(result)
            case CreatedResponse(result) => Created(result)
            case other                   => asOutputError
        }

        def asOutputError: Output[Nothing] = response match {
            case e: NotFoundResponse      => NotFound(e)
            case e: BadAuthResponse       => Unauthorized(e)
            case e: BadGatewayResponse    => BadGateway(e)
            case e: InternalErrorResponse => InternalServerError(e)
            case e: UnprocessableResponse => UnprocessableEntity(e)
            case e: ConflictResponse      => Conflict(e)
            case e: UnknownErrorResponse  => InternalServerError(e)
        }
    }

    /** Default handler for errors that occur during request processing. */
    protected def errorHandler[A]: PartialFunction[Throwable, Output[A]] = {
        case e: InvalidJsonBodyException => BadRequest(e)
        case e: Exception                => InternalServerError(e)
    }

    protected def validateAuthHeader[F[_]: Monad, A](
      header: Option[String],
      action: => F[ServiceResponse[A]]
    ): F[Output[A]] = header match {
        case Some(token) if token.toLowerCase.startsWith("bearer ") =>
            action map { _.asOutput }
        case Some(_) => Monad[F].pure(Unauthorized(InvalidTokenHeader))
        case None    => Monad[F].pure(Unauthorized(NoTokenHeader))
    }

    /** Provide simple jwt validation of authorize header using token service.
      * Also wraps [[ServiceResponse]] with finch [[Output]] instance.
      * @param header
      *   Content of Authorize header.
      * @param roles
      *   List of user roles that has permissions for action execution.
      * @param action
      *   Service action that must be performed after token validation.
      * @param tokenService
      *   Token service instance using for jwt token validation.
      * @return
      *   Future placeholder for action result wrapped with finch Output.
      */
    protected def authorize[F[_]: Monad, A](
      header: Option[String],
      roles: Seq[UserRole],
      action: Uid => F[ServiceResponse[A]]
    )(implicit tokenService: TokenService[F]): F[Output[A]] =
        authorizeNoWrap(header, roles, action) map { _.asOutput }

    /** Provide simple jwt validation of authorize header using token service.
      * Does not wrap [[ServiceResponse]] into finch endpoint [[Output]].
      * @param header
      *   Content of Authorize header.
      * @param roles
      *   List of user roles that has permissions for action execution.
      * @param action
      *   Service action that must be performed after token validation.
      * @param tokenService
      *   Token service instance using for jwt token validation.
      * @return
      *   Future placeholder for action result wrapped with finch Output.
      */
    protected def authorizeNoWrap[F[_]: Monad, A](
      header: Option[String],
      roles: Seq[UserRole],
      action: Uid => F[ServiceResponse[A]]
    )(implicit tokenService: TokenService[F]): F[ServiceResponse[A]] =
        header match {
            case Some(token) if token.toLowerCase.startsWith("bearer ") =>
                tokenService.verify(token.drop(7)) flatMap {
                    case OkResponse(payload)
                        if (roles.contains(payload.role)) =>
                        action(payload.uid)
                    case OkResponse(_) =>
                        Monad[F].pure(BadAuthResponse(InsufficientRole))
                    case e: ErrorResponse => Monad[F].pure(e)
                }
            case Some(_) => Monad[F].pure(BadAuthResponse(InvalidTokenHeader))
            case None    => Monad[F].pure(BadAuthResponse(NoTokenHeader))
        }

    /** User roles that has read access permissions. */
    protected val readRoles = UserRole.values

    /** User roles that has write access permissions. */
    protected val writeRoles =
        Seq(UserRole.SystemAdmin, UserRole.EquipmentAdmin)

    /** User roles that has administation access permissions (i.e. access to
      * users data).
      */
    protected val adminRoles = Seq(UserRole.SystemAdmin)

    /** Roles that has access to the interservice operations. */
    protected val serviceRoles = Seq(UserRole.Service)
}
