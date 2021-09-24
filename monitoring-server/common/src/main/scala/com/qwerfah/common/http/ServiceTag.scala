package com.qwerfah.common.http

sealed trait ServiceTag { val value: String }

case object Session extends ServiceTag { val value = "Session" }
case object Gateway extends ServiceTag { val value = "Gateway" }
case object Equipment extends ServiceTag { val value = "Equipment" }
case object Monitoring extends ServiceTag { val value = "Monitoring" }
case object Documentation extends ServiceTag { val value = "Documentation" }
case object Generator extends ServiceTag { val value = "Generator" }
case object Reporting extends ServiceTag { val value = "Reporting" }
