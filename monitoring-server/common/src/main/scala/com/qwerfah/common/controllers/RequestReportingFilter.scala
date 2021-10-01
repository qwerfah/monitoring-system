package com.qwerfah.common.controllers

import java.time.LocalDateTime

import com.twitter.util._
import com.twitter.finagle.{Service, SimpleFilter}
import com.twitter.finagle.http.{Request, Response}

import akka.actor.{ActorRef}

import com.spingo.op_rabbit._
import com.spingo.op_rabbit.CirceSupport._

import io.circe._
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.auto._

import cats.Monad
import cats.implicits._

import com.typesafe.config.Config

import com.qwerfah.common.randomUid
import com.qwerfah.common.http.HttpMethod
import com.qwerfah.common.util.Conversions._
import com.qwerfah.common.services.TokenService
import com.qwerfah.common.resources.RecordRequest
import com.qwerfah.common.services.response.OkResponse

abstract class RequestReportingFilter[REQ <: Request, F[_]: Monad](implicit
  tokenService: TokenService[F],
  rabbitControl: ActorRef,
  config: Config
) extends SimpleFilter[REQ, Response] {

    def apply(
      request: REQ,
      service: Service[REQ, Response]
    ): Future[Response] = {
        val elapsed = Stopwatch.start()
        val future = service(request)

        future.respond {
            case Return(reply) => {
                val f = request.authorization match {
                    case Some(value) =>
                        tokenService.verify(value.drop(7)) map {
                            case OkResponse(payload) => Some(payload.login)
                            case _                   => None
                        }
                    case None => Monad[F].pure(None)
                }

                f map { userName =>
                    rabbitControl ! Message.queue(
                      RecordRequest(
                        userName,
                        config.getString("serviceId"),
                        request.uri,
                        request.method.asMethod,
                        reply.statusCode,
                        elapsed().inMilliseconds,
                        LocalDateTime.now
                      ),
                      queue = config.getString("reportingQueue")
                    )
                }
            }
        }
        future
    }
}
