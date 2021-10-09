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
import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.common.services._
import com.qwerfah.common.controllers.Controller
import com.qwerfah.common.resources.UserRole

object EquipmentInstanceController extends Controller {
    import Startup._
    import Decoders._

    private val instanceService = implicitly[EquipmentInstanceService[Future]]
    private val paramService = implicitly[ParamService[Future]]
    private implicit val tokenService = implicitly[TokenService[Future]]

    private val getInstances =
        get("instances" :: headerOption("Authorization")) {
            header: Option[String] =>
                authorize(header, serviceRoles, _ => instanceService.getAll)
        }

    private val getInstance = get(
      "instances" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => instanceService.get(uid))
    }

    private val getInstanceParams = get(
      "instances" :: path[Uid] :: "params" :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => paramService.getByInstanceUid(uid))
    }

    private val getAllInstanceMonitors = get(
      "instances" :: "monitors" :: headerOption("Authorization")
    ) { (header: Option[String]) =>
        authorize(header, serviceRoles, _ => instanceService.getMonitors)
    }

    private val getInstanceMonitors = get(
      "instances" :: path[Uid] :: "monitors" :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => instanceService.getMonitors(uid))
    }

    private val addInstance = post(
      "models" :: path[Uid] :: "instances" :: jsonBody[
        AddInstanceRequest
      ] :: headerOption(
        "Authorization"
      )
    ) { (modelUid: Uid, request: AddInstanceRequest, header: Option[String]) =>
        authorize(
          header,
          serviceRoles,
          _ => instanceService.add(modelUid, request)
        )
    }

    private val updateInstance = patch(
      "instances" :: path[Uid] :: jsonBody[
        UpdateInstanceRequest
      ] :: headerOption("Authorization")
    ) {
        (
          uid: Uid,
          request: UpdateInstanceRequest,
          header: Option[String]
        ) =>
            authorize(
              header,
              serviceRoles,
              _ => instanceService.update(uid, request)
            )
    }

    private val deleteInstance = delete(
      "instances" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => instanceService.remove(uid))
    }

    private val restoreInstance = patch(
      "instances" :: path[Uid] :: "restore" :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, serviceRoles, _ => instanceService.restore(uid))
    }

    val api = "api" :: getInstances
        .:+:(getInstance)
        .:+:(getInstanceParams)
        .:+:(getAllInstanceMonitors)
        .:+:(getInstanceMonitors)
        .:+:(addInstance)
        .:+:(updateInstance)
        .:+:(deleteInstance)
        .:+:(restoreInstance)
        .handle(errorHandler)
}
