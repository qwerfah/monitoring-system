package com.qwerfah.common.http

import enumeratum._

sealed trait ServiceTag extends EnumEntry

case object ServiceTag extends Enum[ServiceTag] with CirceEnum[ServiceTag] {

    case object Session extends ServiceTag
    case object Gateway extends ServiceTag
    case object Equipment extends ServiceTag
    case object Monitoring extends ServiceTag
    case object Documentation extends ServiceTag
    case object Generator extends ServiceTag
    case object Reporting extends ServiceTag

    val values = findValues
}
