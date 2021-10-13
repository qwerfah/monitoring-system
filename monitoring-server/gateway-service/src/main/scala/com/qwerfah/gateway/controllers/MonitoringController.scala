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

import io.catbird.util._

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
        header: Option[String] =>
            authorizeRaw(header, readRoles, _ => monitoringService.getMonitors)
    }

    private def getInstanceMonitors = get(
      "instances" :: path[Uid] :: "monitors" :: headerOption("Authorization")
    ) { (instanceUid: Uid, header: Option[String]) =>
        authorizeRaw(
          header,
          readRoles,
          _ => monitoringService.getInstanceMonitors(instanceUid)
        )
    }

    private def getMonitor = get(
      "monitors" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorizeRaw(header, readRoles, _ => monitoringService.getMonitor(uid))
    }

    private def getMonitorParams = get(
      "monitors" :: path[Uid] :: "params" :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorizeRaw(
          header,
          readRoles,
          _ => monitoringService.getMonitorParams(uid)
        )
    }

    private def getMonitorParamValues = get(
      "monitors" :: path[Uid] :: "params" :: "values" :: headerOption(
        "Authorization"
      )
    ) { (uid: Uid, header: Option[String]) =>
        authorizeRaw(
          header,
          readRoles,
          _ => monitoringService.getMonitorParamValues(uid)
        )
    }

    private def addMonitor = post(
      "instances" :: path[Uid] :: "monitors" :: jsonBody[
        AddMonitorRequest
      ] :: headerOption("Authorization")
    ) {
        (
          instanceUid: Uid,
          request: AddMonitorRequest,
          header: Option[String]
        ) =>
            authorizeRaw(
              header,
              writeRoles,
              _ => monitoringService.addMonitor(instanceUid, request)
            )
    }

    private def updateMonitor = patch(
      "monitors" :: path[Uid] :: jsonBody[UpdateMonitorRequest] :: headerOption(
        "Authorization"
      )
    ) { (uid: Uid, request: UpdateMonitorRequest, header: Option[String]) =>
        authorizeRaw(
          header,
          writeRoles,
          _ => monitoringService.updateMonitor(uid, request)
        )
    }

    private def removeMonitor = delete(
      "monitors" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorizeRaw(
          header,
          writeRoles,
          _ => monitoringService.removeMonitor(uid)
        )
    }

    private def removeMonitorParam = delete(
      "monitors" :: path[Uid] :: "params" :: path[Uid] :: headerOption(
        "Authorization"
      )
    ) { (monitorUid: Uid, paramUid: Uid, header: Option[String]) =>
        authorizeRaw(
          header,
          writeRoles,
          _ => monitoringService.removeMonitorParam(monitorUid, paramUid)
        )
    }

    val api = "api" :: "monitoring" :: getMonitors
        .:+:(getInstanceMonitors)
        .:+:(getMonitor)
        .:+:(getMonitorParams)
        .:+:(getMonitorParamValues)
        .:+:(addMonitor)
        .:+:(updateMonitor)
        .:+:(removeMonitor)
        .handle(errorHandler)
}
