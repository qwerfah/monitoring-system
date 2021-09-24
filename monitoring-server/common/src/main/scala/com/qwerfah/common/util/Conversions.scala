package com.qwerfah.common.util

import scala.concurrent.{Future => ScalaFuture}
import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

import com.twitter.finagle.http.{Method => TwitterMethod}
import com.twitter.util.{Future => TwitterFuture, Promise => TwitterPromise}

import com.qwerfah.common.http._

/** Provide implicit classes for different type conversions. */
object Conversions {

    /** Scala to twitter future convertor.
      * @param sf
      *   Scala future instance.
      */
    implicit class ScalaToTwitterFuture[A](val sf: ScalaFuture[A]) {

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

    implicit class methodToTwitterMethod(m: Method) {
        def asTwitter = m match {
            case Get    => TwitterMethod.Get
            case Post   => TwitterMethod.Post
            case Patch  => TwitterMethod.Patch
            case Delete => TwitterMethod.Delete
        }
    }
}
