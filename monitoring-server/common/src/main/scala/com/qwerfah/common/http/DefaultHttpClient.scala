package com.qwerfah.common.http

import com.twitter.finagle.{Service, ServiceFactory}
import com.twitter.finagle.Http
import com.twitter.finagle.http.{
    Request,
    Response,
    Status,
    Method => TwitterMethod
}
import com.twitter.util.{Future, Monitor, FuturePool}
import com.twitter.finagle.liveness.{
    FailureAccrualFactory,
    FailureAccrualPolicy
}
import com.twitter.conversions.DurationOps._
import com.twitter.finagle.Backoff

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import com.qwerfah.common.http.HttpClient
import com.qwerfah.common.services.response._
import com.qwerfah.common.services.response.SuccessResponse
import com.qwerfah.common.exceptions._
import com.qwerfah.common.models.Token
import com.qwerfah.common.resources.{Credentials, UserResponse}
import com.qwerfah.common.util.Conversions._
import com.twitter.finagle.http.exp.Multipart
import com.twitter.io.BufInputStream
import com.twitter.finagle.param.Stats

/** Default http client implementation based on finagle client and twitter
  * future. Contain also access-refresh token pair for authorization in
  * destination service. Tokens are received automaticly during client
  * instantiation and refreshed each time client receives unauthorized response.
  * @param ss
  *   Destination service tag.
  * @param creds
  *   Credentials for authorization in destination service.
  * @param dest
  *   Destination service url.
  */
class DefaultHttpClient(
  tag: ServiceTag,
  creds: Credentials,
  dest: String
) extends HttpClient[Future] {
    var token: Option[Token] = None
    val loginUrl = "/api/session/service/login"
    val refreshUrl = "/api/session/refresh"

    /** Attempt to authorize in destination service using provided credentials.
      * @return
      *   Access-refresh token pair in case of success, otherwise unauthorized
      *   error response.
      */
    private def authorizeInDest: Future[ServiceResponse[Token]] = {
        val request = Request(TwitterMethod.Post, loginUrl)
        request.setContentType("application/json")
        request.setContentString(creds.asJson.toString)

        service(request) map { response =>
            response.status match {
                case Status.Ok =>
                    decodeJson[Token](response.contentString) match {
                        case s: SuccessResponse[Token] => s.result.as200
                        case e: ErrorResponse          => e
                    }
                case _ => InterserviceAuthFailed(tag).as401
            }
        }
    }

    /** Attempt to refresh tokens in destination service using existing refresh
      * token.
      * @return
      *   Access-refresh token pair in case of success, otherwise unauthorized
      *   error response.
      */
    private def refreshTokenInDest: Future[ServiceResponse[Token]] = {
        val request = Request(TwitterMethod.Post, refreshUrl)
        request.headerMap.add(
          "Authorization",
          "Bearer ".concat(token.get.refresh)
        )

        service(request) map { response =>
            response.status match {
                case Status.Ok => decodeJson[Token](response.contentString)
                case _         => InterserviceAuthFailed(tag).as401
            }
        }
    }

    /** Attempt to get token from destination service in case of its absence or
      * refresh it.
      * @return
      *   Access-refresh token pair in case of success, otherwise unauthorized
      *   error response.
      */
    private def getTokenFromDest: Future[ServiceResponse[Token]] = token match {
        case None => authorizeInDest
        case Some(_) =>
            refreshTokenInDest flatMap {
                case e: ErrorResponse => authorizeInDest
                case other            => FuturePool.immediatePool { other }
            }
    }

    /** Send request to the remote server with authorization. If unauthorized
      * error recived, try to authorize and retry request.
      * @param request
      *   Request to the remote server.
      * @return
      *   Response of the remote server that represents response to the initial
      *   request or authorization error in case of failure.
      */
    private def sendWithRefresh(request: Request): Future[Response] =
        service(request) flatMap { response =>
            response.status match {
                case Status.Unauthorized =>
                    getTokenFromDest flatMap {
                        case OkResponse(token) => {
                            this.token = Some(token)
                            request.headerMap += ("Authorization" -> "Bearer "
                                .concat(this.token.get.access))
                            service(request)
                        }
                        case _ => FuturePool.immediatePool { response }
                    }
                case _ => FuturePool.immediatePool { response }
            }
        }

    private def matchStatus[A](
      response: Response
    )(implicit decoder: Decoder[A]) = response.status match {
        case status if status == Status.Ok || status == Status.Created =>
            decodeJson(response.contentString)
        case Status.Unauthorized =>
            if (token.isEmpty) InterserviceAuthFailed(tag).as401
            else InvalidToken.as401
        case Status.NotFound =>
            decode[NotFoundResponse](response.contentString) match {
                case Right(value) => value
                case _            => UnknownServiceResponse(tag).as520
            }
        case Status.Conflict =>
            decode[ConflictResponse](response.contentString) match {
                case Right(value) => value
                case _            => UnknownServiceResponse(tag).as520
            }
        case Status.UnprocessableEntity =>
            decode[UnprocessableResponse](response.contentString) match {
                case Right(value) => value
                case _            => UnknownServiceResponse(tag).as520
            }
        case Status.InternalServerError => ServiceInternalError(tag).as500
        case Status.ServiceUnavailable  => ServiceUnavailable(tag).as502
        case _                          => UnknownServiceResponse(tag).as520
    }

    /** Finagle service with circuit breaker. */
    private val service: Service[Request, Response] =
        Http.client.withSessionQualifier.noFailFast
            .configured(
              FailureAccrualFactory.Param(() =>
                  FailureAccrualPolicy.consecutiveFailures(
                    numFailures = 5,
                    markDeadFor = Backoff.const(10.seconds)
                  )
              )
            )
            .newService(dest)

    /** Decode string body of the response as json.
      * @param body
      *   Response json body.
      * @param decoder
      *   Json decoder for appropriate return type.
      * @return
      *   Service response instance with decoded body.
      */
    private def decodeJson[A](body: String)(implicit decoder: Decoder[A]) = {
        decode[A](body) match {
            case Right(value) => value.as200
            case Left(error)  => BadServiceResult(tag).as422
        }
    }

    override def send(request: Request): Future[Response] = {
        sendWithRefresh(request) map { response =>
            response.status match {
                case Status.ServiceUnavailable =>
                    response.status = Status.BadGateway
                case _ => ()
            }

            response
        }
    }

    override def send(
      method: HttpMethod = HttpMethod.Get,
      url: String = "/",
      content: Option[String] = None,
      token: Option[String] = None
    ): Future[Response] = {
        val request = Request(method.asTwitter, url)
        request.headerMap += ("Authorization" -> "Bearer ".concat(
          token.getOrElse(this.token.getOrElse(Token()).access)
        ))
        content match {
            case Some(value) => {
                request.setContentType("application/json")
                request.setContentString(value)
            }
            case _ => ()
        }

        val response = token match {
            case Some(_) => service(request)
            case None    => sendWithRefresh(request)
        }

        response map { response =>
            response.status match {
                case Status.ServiceUnavailable =>
                    response.status = Status.BadGateway
                case _ => ()
            }
        }

        response
    }

    override def sendAndDecode[A](
      method: HttpMethod = HttpMethod.Get,
      url: String = "/",
      content: Option[String] = None,
      token: Option[String] = None
    )(implicit
      decoder: Decoder[A]
    ): Future[ServiceResponse[A]] = {
        val request = Request(method.asTwitter, url)
        request.headerMap += ("Authorization" -> "Bearer ".concat(
          token.getOrElse(this.token.getOrElse(Token()).access)
        ))
        content match {
            case Some(value) => {
                request.setContentType("application/json")
                request.setContentString(value)
            }
            case _ => ()
        }

        val response = token match {
            case Some(_) => service(request)
            case None    => sendWithRefresh(request)
        }

        response map { response => matchStatus(response) }
    }
}
