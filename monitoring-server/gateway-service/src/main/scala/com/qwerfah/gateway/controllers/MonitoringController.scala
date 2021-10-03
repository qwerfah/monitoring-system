package com.qwerfah.gateway.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.util.Future
import com.twitter.server.TwitterServer
import com.twitter.finagle.{SimpleFilter, Service}
import com.twitter.finagle.http.{Request, Response, Status}

import io.finch._
import io.finch.circe._
import io.finch.catsEffect._

import io.circe.generic.auto._

import com.qwerfah.gateway.Startup
import com.qwerfah.gateway.services.MonitoringService

import com.qwerfah.monitoring.resources._
import com.qwerfah.monitoring.json.Decoders

import com.qwerfah.common.Uid
import com.qwerfah.common.services._
import com.qwerfah.common.exceptions._
import com.qwerfah.common.controllers.Controller

object MonitoringController extends Controller {
    import Startup._
    import Decoders._

    private def monitoringService = implicitly[MonitoringService[Future]]

    private def getMonitors = get("monitors" :: headerOption("Authorization")) {
        header: Option[String] => monitoringService.getMonitors
    }

    private def getInstanceMonitors = get(
      "instnaces" :: path[Uid] :: "monitors" :: headerOption("Authorization")
    ) { (instanceUid: Uid, header: Option[String]) =>
        monitoringService.getInstanceMonitors(instanceUid)
    }

    private def getMonitor = get(
      "monitors" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        monitoringService.getMonitor(uid)
    }

    private def addMonitor = post(
      "monitors" :: jsonBody[AddMonitorRequest] :: headerOption("Authorization")
    ) { (request: AddMonitorRequest, header: Option[String]) =>
        monitoringService.addMonitor(request)
    }

    private def updateMonitor = patch(
      "monitors" :: path[Uid] :: jsonBody[UpdateMonitorRequest] :: headerOption(
        "Authorization"
      )
    ) { (uid: Uid, request: UpdateMonitorRequest, header: Option[String]) =>
        monitoringService.updateMonitor(uid, request)
    }

    private def removeMonitor = delete(
      "monitors" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        monitoringService.removeMonitor(uid)
    }

    val api = "api" :: "monitoring" :: getMonitors
        .:+:(getInstanceMonitors)
        .:+:(getMonitor)
        .:+:(addMonitor)
        .:+:(updateMonitor)
        .:+:(removeMonitor)
        .handle(errorHandler)
}
