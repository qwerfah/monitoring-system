package com.qwerfah.common

package object exceptions {
    implicit def throwableToException(error: Throwable) = new Exception(
      error.getMessage
    )
}
