package com.qwerfah.common.controllers

import com.twitter.finagle.filter.{LogFormatter => FinagleLogFormatter}
import com.twitter.finagle.http.Status.InternalServerError
import com.twitter.finagle.http.filter.{
    CommonLogFormatter => FinagleCommonLogFormatter
}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.util._

import com.qwerfah.common.util.Logger

abstract class RequestLoggingFilter[REQ <: Request](
  val log: Logger,
  val formatter: FinagleLogFormatter[REQ, Response]
) extends SimpleFilter[REQ, Response] {

    def apply(
      request: REQ,
      service: Service[REQ, Response]
    ): Future[Response] = {
        val elapsed = Stopwatch.start()
        val future = service(request)
        future.respond {
            case Return(reply)    => logSuccess(elapsed(), request, reply)
            case Throw(throwable) => logException(elapsed(), request, throwable)
        }
        future
    }

    def logSuccess(replyTime: Duration, request: REQ, reply: Response) = {
        val line = formatter.format(request, reply, replyTime)
        log.info(line + reply.contentString)
    }

    def logException(duration: Duration, request: REQ, throwable: Throwable) = {
        val response = Response(request.version, InternalServerError)
        val line = formatter.format(request, response, duration)
        log.info(line + response.contentString)
    }
}

object RequestLoggingFilter
  extends RequestLoggingFilter[Request](
    new Logger("access"),
    new FinagleCommonLogFormatter
  )
