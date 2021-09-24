package com.qwerfah.common.http

import com.twitter.finagle.{Service, ServiceFactory}
import com.twitter.finagle.Http
import com.twitter.finagle.http.{
    Request,
    Response,
    Status,
    Method => TwitterMethod
}
import com.twitter.util.{Future, Monitor}
import com.twitter.finagle.liveness.{
    FailureAccrualFactory,
    FailureAccrualPolicy
}
import com.twitter.conversions.DurationOps._
import com.twitter.finagle.Backoff

import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

import com.qwerfah.common.http.HttpClient
import com.qwerfah.common.services.response._
import com.qwerfah.common.exceptions._

class DefaultHttpClient(ss: SystemService, dest: String)
  extends HttpClient[Future] {
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

    private implicit def methodToTwitterMethod(m: Method): TwitterMethod =
        m match {
            case Get    => TwitterMethod.Get
            case Post   => TwitterMethod.Post
            case Patch  => TwitterMethod.Patch
            case Delete => TwitterMethod.Delete
        }

    override def sendAndDecode[A](
      method: Method = Get,
      url: String = "/",
      contentType: Option[String] = None,
      contentString: Option[String] = None
    )(implicit decoder: Decoder[A]): Future[ServiceResponse[A]] = {

        val request = Request(method, url)

        (contentType, contentString) match {
            case (Some(ct), Some(cs)) => {
                request.setContentType(ct)
                request.setContentString(cs)
            }
            case _ => ()
        }

        service.apply(request) map { response =>
            response.status match {
                case Status.Ok => decodeJson(response.contentString)
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
