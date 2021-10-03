package com.qwerfah.common.util

import scala.concurrent.{Future => ScalaFuture}
import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

import com.twitter.finagle.http.{Method => TwitterMethod}
import com.twitter.util.{Future => TwitterFuture, Promise => TwitterPromise}

import com.qwerfah.common.http._
import com.qwerfah.common.services.response._
import com.qwerfah.common.exceptions._

/** Provide implicit classes for different type conversions. */
object Conversions {

    /** Scala to twitter future convertor.
      * @param sf
      *   Scala future instance.
      */
    implicit class ScalaToTwitterFuture[A](sf: ScalaFuture[A]) {

        /** Convert scala future too twitter future.
          * @param e
          *   Future execution context.
          * @return
          *   Twitter future instance associated with the same task.
          */
        def asTwitter(implicit e: ExecutionContext): TwitterFuture[A] = {
            val promise: TwitterPromise[A] = new TwitterPromise[A]()
            sf onComplete {
                case Success(value)     => promise.setValue(value)
                case Failure(exception) => promise.setException(exception)
            }
            promise
        }
    }

    /** Implicit conversion of custom http client method enum to the Twitter
      * http method enum.
      * @param m
      *   Custom http method enum instance.
      */
    implicit class methodToTwitterMethod(m: HttpMethod) {
        def asTwitter = m match {
            case HttpMethod.Get    => TwitterMethod.Get
            case HttpMethod.Post   => TwitterMethod.Post
            case HttpMethod.Patch  => TwitterMethod.Patch
            case HttpMethod.Delete => TwitterMethod.Delete
        }
    }

    /** Implicit conversion of twitter http client method enum to the local http
      * method enum.
      * @param m
      *   Twitter http method enum instance.
      */
    implicit class twitterMethodToMethod(m: TwitterMethod) {
        def asMethod = m match {
            case TwitterMethod.Get    => HttpMethod.Get
            case TwitterMethod.Post   => HttpMethod.Post
            case TwitterMethod.Patch  => HttpMethod.Patch
            case TwitterMethod.Delete => HttpMethod.Delete
        }
    }

    /** Object conversion to the successfull service response with payload.
      * @param obj
      *   Service method result instance.
      */
    implicit class objectToServiceResponse[A](obj: A) {
        def as200 = OkResponse(obj)
        def as201 = CreatedResponse(obj)
    }

    /** Error message conversions to the corresponding error responses.
      * @param msg
      *   Error message instance.
      */
    implicit class errorToServiceResponse(msg: ErrorMessage) {
        def as401 = BadAuthResponse(msg.getMessage)
        def as404 = NotFoundResponse(msg.getMessage)
        def as409 = ConflictResponse(msg.getMessage)
        def as422 = UnprocessableResponse(msg.getMessage)
        def as500 = InternalErrorResponse(msg.getMessage)
        def as502 = BadGatewayResponse(msg.getMessage)
        def as520 = UnknownErrorResponse(msg.getMessage)
    }
}
