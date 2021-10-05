package com.qwerfah.monitoring.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.util.Future
import com.twitter.server.TwitterServer
import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.finagle.http.{Request, Response}

import io.finch._
import io.finch.circe._
import io.finch.catsEffect._

import io.circe.generic.auto._

import io.catbird.util._

import com.qwerfah.monitoring.Startup
import com.qwerfah.monitoring.resources._
import com.qwerfah.monitoring.json.Decoders
import com.qwerfah.monitoring.models.MonitorParam
import com.qwerfah.monitoring.services.MonitorService

import com.qwerfah.common.Uid
import com.qwerfah.common.services.TokenService
import com.qwerfah.common.controllers.Controller

object MonitorController extends Controller {
    import Startup._
    import Decoders._

    private val monitorService = implicitly[MonitorService[Future]]
    private implicit val tokenService = implicitly[TokenService[Future]]

    private val getMonitors = get("monitors" :: headerOption("Authorization")) {
        header: Option[String] =>
            authorize(header, serviceRoles, _ => monitorService.getMonitors)
    }

    private val getMonitor =
        get("monitors" :: path[Uid] :: headerOption("Authorization")) {
            (uid: Uid, header: Option[String]) =>
                authorize(
                  header,
                  serviceRoles,
                  _ => monitorService.getMonitor(uid)
                )
        }

    private val getMonitorParams = get(
      "monitors" :: path[Uid] :: "params" :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.getMonitorParams(uid)
        )
    }

    private val getInstanceMonitors = get(
      "instances" :: path[Uid] :: "monitors" :: headerOption(
        "Authorization"
      )
    ) { (uid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.getInstanceMonitors(uid)
        )
    }

    private val getMonitorIsntances = get(
      "monitors" :: "instances" :: headerOption(
        "Authorization"
      )
    ) { header: Option[String] =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.getMonitoringInstances
        )
    }

    private val addMonitor = post(
      "monitors" :: jsonBody[AddMonitorRequest] :: headerOption("Authorization")
    ) { (request: AddMonitorRequest, header: Option[String]) =>
        authorize(header, serviceRoles, _ => monitorService.addMonitor(request))
    }

    private val updateMonitor = patch(
      "monitors" :: path[Uid] :: jsonBody[UpdateMonitorRequest] :: headerOption(
        "Authorization"
      )
    ) { (uid: Uid, request: UpdateMonitorRequest, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.updateMonitor(uid, request)
        )
    }

    private val addMonitorParam = post(
      "monitors" :: path[Uid] :: "params" :: jsonBody[
        MonitorParamRequest
      ] :: headerOption(
        "Authorization"
      )
    ) { (uid: Uid, request: MonitorParamRequest, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.addMonitorParam(uid, request)
        )
    }

    private val removeMonitor = delete(
      "monitors" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => monitorService.removeMonitor(uid))
    }

    private val removeInstanceMonitors = delete(
      "instances" :: path[Uid] :: "monitors" :: headerOption("Authorization")
    ) { (instanceUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.removeInstanceMonitors(instanceUid)
        )
    }

    private val restoreMonitor = patch(
      "monitors" :: path[Uid] :: "restore" :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.restoreMonitor(uid)
        )
    }

    private val restoreInstanceMonitors = patch(
      "instances" :: path[Uid] :: "monitors" :: "restore" :: headerOption(
        "Authorization"
      )
    ) { (instanceUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.restoreInstanceMonitors(instanceUid)
        )
    }

    private val removeMonitorParam = delete(
      "monitors" :: path[Uid] :: "params" :: path[Uid] :: headerOption(
        "Authorization"
      )
    ) { (monitorUid: Uid, paramUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.removeMonitorParam(monitorUid, paramUid)
        )
    }

    private val removeMonitorParamsForMonitor = delete(
      "monitors" :: path[Uid] :: "params" :: headerOption(
        "Authorization"
      )
    ) { (monitorUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.removeMonitorParamsForMonitor(monitorUid)
        )
    }

    private val removeMonitorParamsForParam = delete(
      "params" :: path[Uid] :: "monitors" :: headerOption(
        "Authorization"
      )
    ) { (paramUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.removeMonitorParamsForParam(paramUid)
        )
    }

    private val restoreMonitorParam = patch(
      "monitors" :: path[Uid] :: "params" :: path[
        Uid
      ] :: "restore" :: headerOption(
        "Authorization"
      )
    ) { (monitorUid: Uid, paramUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.restoreMonitorParam(monitorUid, paramUid)
        )
    }

    private val restoreMonitorParamsForMonitor = patch(
      "monitors" :: path[Uid] :: "params" :: "restore" :: headerOption(
        "Authorization"
      )
    ) { (monitorUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.restoreMonitorParamsForMonitor(monitorUid)
        )
    }

    private val restoreMonitorParamsForParam = patch(
      "params" :: path[Uid] :: "monitors" :: "restore" :: headerOption(
        "Authorization"
      )
    ) { (paramUid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => monitorService.restoreMonitorParamsForParam(paramUid)
        )
    }

    val api = "api" :: getMonitors
        .:+:(getMonitor)
        .:+:(getMonitorParams)
        .:+:(getInstanceMonitors)
        .:+:(getMonitorIsntances)
        .:+:(addMonitor)
        .:+:(updateMonitor)
        .:+:(addMonitorParam)
        .:+:(removeMonitor)
        .:+:(removeInstanceMonitors)
        .:+:(restoreMonitor)
        .:+:(restoreInstanceMonitors)
        .:+:(removeMonitorParam)
        .:+:(removeMonitorParamsForMonitor)
        .:+:(removeMonitorParamsForParam)
        .:+:(restoreMonitorParam)
        .:+:(restoreMonitorParamsForMonitor)
        .:+:(restoreMonitorParamsForParam)
        .handle(errorHandler)
}
