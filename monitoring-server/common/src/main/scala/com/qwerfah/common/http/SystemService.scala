package com.qwerfah.common.http

sealed trait SystemService { val value: String }

case object Session extends SystemService { val value = "Session" }
case object Gateway extends SystemService { val value = "Gateway" }
case object Equipment extends SystemService { val value = "Equipment" }
case object Monitoring extends SystemService { val value = "Monitoring" }
case object Documentation extends SystemService { val value = "Documentation" }
case object Generator extends SystemService { val value = "Generator" }
case object Reporting extends SystemService { val value = "Reporting" }
