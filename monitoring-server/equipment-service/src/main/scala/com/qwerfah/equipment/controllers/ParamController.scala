package com.qwerfah.equipment.controllers

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

import com.qwerfah.equipment.services._
import com.qwerfah.equipment.repos.slick._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.Startup
import com.qwerfah.equipment.json.Decoders
import com.qwerfah.common.Uid
import com.qwerfah.common.exceptions._
import com.qwerfah.common.controllers.Controller
import com.qwerfah.common.services.TokenService

object ParamController extends Controller {
    import Startup._
    import Decoders._

    private val paramService = implicitly[ParamService[Future]]
    private implicit val tokenService = implicitly[TokenService[Future]]

    private val getParams = get("params" :: headerOption("Authorization")) {
        header: Option[String] =>
            authorize(header, serviceRoles, _ => paramService.getAll)
    }

    private val getParam = get(
      "params" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => paramService.get(uid))
    }

    private val addParam = post(
      "models" :: path[Uid] ::
          "params" :: jsonBody[AddParamRequest] :: headerOption("Authorization")
    ) { (modelUid: Uid, request: AddParamRequest, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => paramService.add(modelUid, request)
        )
    }

    private val updateParam = patch(
      "params" :: path[Uid] :: jsonBody[UpdateParamRequest] :: headerOption(
        "Authorization"
      )
    ) { (uid: Uid, request: UpdateParamRequest, header: Option[String]) =>
        authorize(header, serviceRoles, _ => paramService.update(uid, request))
    }

    private val deleteParam = delete(
      "params" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => paramService.remove(uid))
    }

    private val restoreParam = patch(
      "params" :: path[Uid] :: "restore" :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => paramService.restore(uid))
    }

    val api = "api" :: getParams
        .:+:(getParam)
        .:+:(addParam)
        .:+:(updateParam)
        .:+:(deleteParam)
        .:+:(restoreParam)
        .handle(errorHandler)
}
