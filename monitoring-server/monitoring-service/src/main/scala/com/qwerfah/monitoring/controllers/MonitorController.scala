package com.qwerfah.monitoring.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}
import com.twitter.util.Future

import io.finch.catsEffect._
import io.finch._
import io.finch.circe._

import io.circe.generic.auto._

import io.catbird.util._

import com.qwerfah.monitoring.Startup
import com.qwerfah.monitoring.services.MonitorService
import com.qwerfah.common.controllers.Controller
import com.qwerfah.common.services.TokenService
import com.qwerfah.common.Uid

object MonitorController extends Controller {
    import Startup._

    private val monitorService = implicitly[MonitorService[Future]]
    private implicit val tokenService = implicitly[TokenService[Future]]

    private val getMonitors = get("monitors" :: headerOption("Authorization")) {
        header: Option[String] =>
            authorize(header, _ => monitorService.get)
    }

    private val getMonitor =
        get("monitors" :: path[Uid] :: headerOption("Authorization")) {
            (uid: Uid, header: Option[String]) =>
                authorize(header, _ => monitorService.get(uid))
        }

    private val getMonitorParams = get(
      "monitors" :: path[Uid] :: "params" :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, _ => monitorService.getParams(uid))
    }

    private val getinstanceMonitors = get(
      "instances" :: path[Uid] :: "monitors" :: headerOption(
        "Authorization"
      )
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, _ => monitorService.getByInstanceUid(uid))
    }

    val api = getMonitors.handle(errorHandler)
}
