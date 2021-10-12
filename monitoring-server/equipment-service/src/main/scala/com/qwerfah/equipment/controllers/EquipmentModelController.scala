package com.qwerfah.equipment.controllers

import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response, Status}
import com.twitter.util.{Future, FuturePool}
import com.twitter.finagle.http.exp.Multipart

import io.finch.catsEffect._
import io.finch._
import io.finch.circe._

import io.catbird.util._

import com.qwerfah.equipment.services._
import com.qwerfah.equipment.repos.slick._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.Startup
import com.qwerfah.equipment.json.Decoders
import com.qwerfah.common.exceptions._
import com.qwerfah.common.json.Encoders
import com.qwerfah.common.Uid
import com.qwerfah.common.controllers.Controller
import com.qwerfah.common.services.TokenService

object EquipmentModelController extends Controller {
    import Startup._
    import Encoders._
    import Decoders._

    private val modelService = implicitly[EquipmentModelService[Future]]
    private val instanceService = implicitly[EquipmentInstanceService[Future]]
    private val paramService = implicitly[ParamService[Future]]
    private implicit val tokenService = implicitly[TokenService[Future]]

    private val getModels = get("models" :: headerOption("Authorization")) {
        header: Option[String] =>
            authorize(header, readRoles, _ => modelService.getAll)
    }

    private val getModel = get(
      "models" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => modelService.get(uid))
    }

    private val getModelInstances = get(
      "models" :: path[Uid] :: "instances" :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => instanceService.getAll(Some(uid)))
    }

    private val getActiveModelInstances = get(
      "models" :: path[Uid] :: "instances" :: "active" :: headerOption(
        "Authorization"
      )
    ) { (uid: Uid, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => instanceService.getAll(Some(uid), Some(EquipmentStatus.Active))
        )
    }

    private val getModelParams = get(
      "models" :: path[Uid] :: "params" :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => paramService.getByModelUid(uid))
    }

    private val getModelFiles = get(
      "models" :: path[Uid] :: "files" :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => modelService.getFiles(uid))
    }

    private val addModel = post(
      "models" :: jsonBody[ModelRequest] :: headerOption("Authorization")
    ) { (request: ModelRequest, header: Option[String]) =>
        authorize(header, serviceRoles, _ => modelService.add(request))
    }

    private val addModelFile = post(
      root ::
          "models" :: path[Uid] :: "files" :: headerOption(
            "Authorization"
          ) :: multipartFileUploadOption("file")
    ) {
        (
          request: Request,
          modelUid: Uid,
          header: Option[String],
          _: Option[Multipart.FileUpload]
        ) =>
            authorizeRaw(
              header,
              serviceRoles,
              _ => modelService.addFile(modelUid, request)
            )
    }

    private val updateModel = patch(
      "models" :: path[Uid] :: jsonBody[ModelRequest] :: headerOption(
        "Authorization"
      )
    ) { (uid: Uid, request: ModelRequest, header: Option[String]) =>
        authorize(header, serviceRoles, _ => modelService.update(uid, request))
    }

    private val deleteModel = delete(
      "models" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => modelService.remove(uid))
    }

    private val restoreModel = patch(
      "models" :: path[Uid] :: "restore" :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => modelService.restore(uid))
    }

    val api = "api" :: getModels
        .:+:(getModel)
        .:+:(getModelInstances)
        .:+:(getActiveModelInstances)
        .:+:(getModelParams)
        .:+:(getModelFiles)
        .:+:(addModel)
        .:+:(addModelFile)
        .:+:(updateModel)
        .:+:(deleteModel)
        .:+:(restoreModel)
        .handle(errorHandler)
}
