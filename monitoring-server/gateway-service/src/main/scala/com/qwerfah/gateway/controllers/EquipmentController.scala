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
import com.qwerfah.gateway.services._

import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.json.Decoders

import com.qwerfah.common.Uid
import com.qwerfah.common.services._
import com.qwerfah.common.exceptions._
import com.qwerfah.common.controllers.Controller

object EquipmentController extends Controller {
    import Startup._
    import Decoders._

    private def equipmentService = implicitly[EquipmentService[Future]]

    private def getModels = get("models" :: headerOption("Authorization")) {
        header: Option[String] =>
            equipmentService.getModels
    }

    private def getModel = get(
      "models" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        equipmentService.getModel(uid)
    }

    private def addModel = post(
      "models" :: jsonBody[ModelRequest] :: headerOption("Authorization")
    ) { (request: ModelRequest, header: Option[String]) =>
        equipmentService.addModel(request)
    }

    private def updateModel = patch(
      "models" :: path[Uid] :: jsonBody[ModelRequest] :: headerOption(
        "Authorization"
      )
    ) { (uid: Uid, request: ModelRequest, header: Option[String]) =>
        equipmentService.updateModel(uid, request)
    }

    private def removeModel = delete(
      "models" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        equipmentService.removeModel(uid)
    }

    private def getInstances = get(
      "instances" :: headerOption("Authorization")
    ) { header: Option[String] =>
        equipmentService.getInstances
    }

    private def getModelInstances = get(
      "models" :: path[Uid] :: "instances" :: headerOption("Authorization")
    ) { (modelUid: Uid, header: Option[String]) =>
        equipmentService.getModelInstances(modelUid)
    }

    private def getInstance = get(
      "instances" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        equipmentService.getInstance(uid)
    }

    private def addInstance = post(
      "instances" :: jsonBody[AddInstanceRequest] :: headerOption(
        "Authorization"
      )
    ) { (request: AddInstanceRequest, header: Option[String]) =>
        equipmentService.addInstance(request)
    }

    private def updateInstance = patch(
      "instances" :: path[Uid] :: jsonBody[
        UpdateInstanceRequest
      ] :: headerOption(
        "Authorization"
      )
    ) { (uid: Uid, request: UpdateInstanceRequest, header: Option[String]) =>
        equipmentService.updateInstance(uid, request)
    }

    private def removeInstance = delete(
      "instances" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        equipmentService.removeInstance(uid)
    }

    private def getParams = get(
      "params" :: headerOption("Authorization")
    ) { header: Option[String] =>
        equipmentService.getParams
    }

    private def getModelParams = get(
      "models" :: path[Uid] :: "params" :: headerOption("Authorization")
    ) { (modelUid: Uid, header: Option[String]) =>
        equipmentService.getModelParams(modelUid)
    }

    private def getInstanceParams = get(
      "instances" :: path[Uid] :: "params" :: headerOption("Authorization")
    ) { (instanceUid: Uid, header: Option[String]) =>
        equipmentService.getInstanceParams(instanceUid)
    }

    private def getParam = get(
      "params" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        equipmentService.getParam(uid)
    }

    private def addParam = post(
      "params" :: jsonBody[AddParamRequest] :: headerOption(
        "Authorization"
      )
    ) { (request: AddParamRequest, header: Option[String]) =>
        equipmentService.addParam(request)
    }

    private def updateParam = patch(
      "params" :: path[Uid] :: jsonBody[
        UpdateParamRequest
      ] :: headerOption(
        "Authorization"
      )
    ) { (uid: Uid, request: UpdateParamRequest, header: Option[String]) =>
        equipmentService.updateParam(uid, request)
    }

    private def removeParam = delete(
      "params" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        equipmentService.removeParam(uid)
    }

    def api = "api" :: "equipment" :: getModels
        .:+:(getModel)
        .:+:(addModel)
        .:+:(updateModel)
        .:+:(removeModel)
        .:+:(getInstances)
        .:+:(getModelInstances)
        .:+:(getInstance)
        .:+:(addInstance)
        .:+:(updateInstance)
        .:+:(removeInstance)
        .:+:(getParams)
        .:+:(getModelParams)
        .:+:(getInstanceParams)
        .:+:(getParam)
        .:+:(addParam)
        .:+:(updateParam)
        .:+:(removeParam)
        .handle(errorHandler)
}
