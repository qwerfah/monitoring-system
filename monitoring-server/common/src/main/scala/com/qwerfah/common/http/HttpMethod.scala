package com.qwerfah.common.http

import enumeratum._

sealed trait HttpMethod extends EnumEntry

case object HttpMethod extends Enum[HttpMethod] with CirceEnum[HttpMethod] {

    case object Get extends HttpMethod
    case object Post extends HttpMethod
    case object Patch extends HttpMethod
    case object Delete extends HttpMethod

    val values = findValues
}
