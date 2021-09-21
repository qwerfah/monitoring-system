package com.qwerfah.equipment.controllers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}

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
import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.common.controllers.Controller

object EquipmentModelController extends Controller {
    import Startup._
    import Decoders._

    private val modelService = implicitly[EquipmentModelService[Future]]
    private val instanceService = implicitly[EquipmentInstanceService[Future]]
    private val paramService = implicitly[ParamService[Future]]

    private val getModels = get("models") {
        for { result <- modelService.getAll } yield result.asOutput
    }

    private val getModel = get("models" :: path[Uid]) { uid: Uid =>
        for { result <- modelService.get(uid) } yield result.asOutput
    }

    private val getModelInstances = get("models" :: path[Uid] :: "instances") {
        uid: Uid =>
            for {
                result <- instanceService.getByModelUid(uid)
            } yield result.asOutput
    }

    private val getModelParams = get("models" :: path[Uid] :: "params") {
        uid: Uid =>
            for {
                result <- paramService.getByModelUid(uid)
            } yield result.asOutput
    }

    private val addModel = post("models" :: jsonBody[ModelRequest]) {
        request: ModelRequest =>
            for { result <- modelService.add(request) } yield result.asOutput
    }

    private val updateModel = patch(
      "models" :: path[Uid] :: jsonBody[ModelRequest]
    ) { (uid: Uid, request: ModelRequest) =>
        for {
            result <- modelService.update(uid, request)
        } yield result.asOutput
    }

    private val deleteModel = delete("models" :: path[Uid]) { uid: Uid =>
        for {
            result <- modelService.remove(uid)
        } yield result.asOutput
    }

    val api =
        (getModels :+: getModel :+: getModelInstances :+: getModelParams :+: addModel :+: updateModel :+: deleteModel)
            .handle(errorHandler)
}
