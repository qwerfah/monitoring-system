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
import com.qwerfah.common.exceptions._
import com.qwerfah.common.models.Token
import com.qwerfah.common.resources.Credentials

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
class DefaultHttpClient(ss: ServiceTag, creds: Credentials, dest: String)
  extends HttpClient[Future] {
    var token: Option[Token] = None

    /** Attempt to authorize in destination service using provided credentials.
      * @return
      *   Access-refresh token pair in case of success, otherwise unauthorized
      *   error response.
      */
    def authorizeInDest: Future[ServiceResponse[Token]] = {
        val request = Request(TwitterMethod.Post, "/auth/login")
        request.setContentType("application/json")
        request.setContentString(creds.asJson.toString)

        service(request) map { response =>
            response.status match {
                case Status.Ok => decodeJson[Token](response.contentString)
                case _ => BadAuthResponse(InterserviceAuthFailed(ss.value))
            }
        }
    }

    /** Attempt to refresh tokens in destination service using existing refresh
      * token.
      * @return
      *   Access-refresh token pair in case of success, otherwise unauthorized
      *   error response.
      */
    def refreshTokenInDest: Future[ServiceResponse[Token]] = {
        val request = Request(TwitterMethod.Post, "/auth/refresh")
        request.headerMap.add("Authorization", token.get.refresh)

        service(request) map { response =>
            response.status match {
                case Status.Ok => decodeJson[Token](response.contentString)
                case _ => BadAuthResponse(InterserviceAuthFailed(ss.value))
            }
        }
    }

    /** Attempt to get token from destination service in case of its absence or
      * refresh it.
      * @return
      *   Access-refresh token pair in case of success, otherwise unauthorized
      *   error response.
      */
    def getTokenFromDest: Future[ServiceResponse[Token]] = token match {
        case None => authorizeInDest
        case Some(_) =>
            refreshTokenInDest flatMap {
                case e: ErrorResponse => authorizeInDest
                case other            => FuturePool.immediatePool { other }
            }
    }

    def sendWithRefresh(request: Request): Future[Response] =
        service(request) flatMap { response =>
            response.status match {
                case Status.Unauthorized =>
                    getTokenFromDest flatMap {
                        case ObjectResponse(token) => {
                            this.token = Some(token)
                            request.headerMap += ("Authorization" -> this.token.get.access)
                            service(request)
                        }
                        case _ => FuturePool.immediatePool { response }
                    }
                case _ => FuturePool.immediatePool { response }
            }
        }

    val monitor: Monitor = new Monitor {
        def handle(t: Throwable): Boolean = {
            println(t.getMessage)
            true
        }
    }

    val service: Service[Request, Response] =
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

    private def decodeJson[A](body: String)(implicit decoder: Decoder[A]) = {
        decode[A](body) match {
            case Right(value) => ObjectResponse(value)
            case Left(error) =>
                UnprocessableResponse(BadServiceResult(ss.value))
        }
    }

    private implicit class methodToTwitterMethod(m: Method) {
        def asTwitter = m match {
            case Get    => TwitterMethod.Get
            case Post   => TwitterMethod.Post
            case Patch  => TwitterMethod.Patch
            case Delete => TwitterMethod.Delete
        }
    }

    override def sendAndDecode[A](
      method: Method = Get,
      url: String = "/",
      contentType: Option[String] = None,
      contentString: Option[String] = None
    )(implicit decoder: Decoder[A]): Future[ServiceResponse[A]] = {
        val request = Request(method.asTwitter, url)
        request.headerMap += ("Authorization" -> this.token
            .getOrElse(Token())
            .access)
        (contentType, contentString) match {
            case (Some(ct), Some(cs)) => {
                request.setContentType(ct)
                request.setContentString(cs)
            }
            case _ => ()
        }

        println(s"Send: $request")

        sendWithRefresh(request) map { response =>
            response.status match {
                case Status.Ok => decodeJson[A](response.contentString)
                case Status.Unauthorized =>
                    BadAuthResponse(InterserviceAuthFailed(ss.value))
                case Status.InternalServerError =>
                    InternalErrorResponse(ServiceInternalError(ss.value))
                case Status.ServiceUnavailable =>
                    BadGatewayResponse(ServiceUnavailable(ss.value))
                case _ =>
                    UnknownErrorResponse(UnknownServiceResponse(ss.value))
            }
        }
    }
}
