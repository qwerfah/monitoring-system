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

import com.qwerfah.equipment.services._
import com.qwerfah.equipment.repos.slick._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.Startup
import com.qwerfah.equipment.json.Decoders
import com.qwerfah.common.Uid
import com.qwerfah.common.exceptions._
import com.qwerfah.common.controllers.Controller

object ParamController extends Controller {
    import Startup._
    import Decoders._

    private val paramService = implicitly[ParamService[Future]]

    private val getParams = get("params") {
        for { result <- paramService.getAll } yield result.asOutput
    }

    private val getParam = get("params" :: path[Uid]) { uid: Uid =>
        for { result <- paramService.get(uid) } yield result.asOutput
    }

    private val addParam = post("params" :: jsonBody[AddParamRequest]) {
        request: AddParamRequest =>
            for {
                result <- paramService.add(request)
            } yield result.asOutput
    }

    private val updateParam = patch(
      "params" :: path[Uid] :: jsonBody[UpdateParamRequest]
    ) { (uid: Uid, request: UpdateParamRequest) =>
        for {
            result <- paramService.update(uid, request)
        } yield result.asOutput
    }

    private val deleteParam = delete("params" :: path[Uid]) { uid: Uid =>
        for {
            result <- paramService.remove(uid)
        } yield result.asOutput
    }

    val api =
        (getParams :+: getParam :+: addParam :+: updateParam :+: deleteParam)
            .handle(errorHandler)
}
