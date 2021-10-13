package com.qwerfah.reporting.controllers

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

import com.qwerfah.reporting.Startup
import com.qwerfah.reporting.json.Decoders
import com.qwerfah.reporting.models.ReportingContext
import com.qwerfah.reporting.services.StatService

import com.qwerfah.common.Uid
import com.qwerfah.common.http.HttpMethod
import com.qwerfah.common.services.TokenService
import com.qwerfah.common.controllers.Controller
import com.qwerfah.common.resources.{RecordRequest, RecordResponse}

object StatController extends Controller {
    import Startup._
    import Decoders._

    private val statService = implicitly[StatService[Future]]
    private implicit val tokenService = implicitly[TokenService[Future]]

    private val getModelStats = get(
      "models" :: "stats" :: headerOption[String](
        "Authorization"
      )
    ) { header: Option[String] =>
        authorize(header, serviceRoles, _ => statService.getModelStats())
    }

    private val getServiceStats = get(
      "models" :: "stats" :: headerOption[String](
        "Authorization"
      )
    ) { header: Option[String] =>
        authorize(header, serviceRoles, _ => statService.getServiceStats())
    }
    
    val api = "api" :: "reports" :: getModelStats
        .:+:(getServiceStats)
        .handle(errorHandler)
}
