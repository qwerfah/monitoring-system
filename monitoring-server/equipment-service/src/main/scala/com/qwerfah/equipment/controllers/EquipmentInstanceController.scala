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

object EquipmentInstanceController extends Controller {
    import Startup._
    import Decoders._

    private val instanceService = implicitly[EquipmentInstanceService[Future]]
    private implicit val tokenService = implicitly[TokenService[Future]]

    private val getInstances =
        get("instances" :: headerOption("Authorization")) {
            header: Option[String] =>
                authorize(header, _ => instanceService.getAll)
        }

    private val getInstance = get(
      "instances" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, _ => instanceService.get(uid))
    }

    private val addInstance = post(
      "instances" :: jsonBody[AddInstanceRequest] :: headerOption(
        "Authorization"
      )
    ) { (request: AddInstanceRequest, header: Option[String]) =>
        authorize(header, _ => instanceService.add(request))
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
        ) => authorize(header, _ => instanceService.update(uid, request))
    }

    private val deleteInstance = delete(
      "instances" :: path[Uid] :: headerOption("Authorization")
    ) { (uid: Uid, header: Option[String]) =>
        authorize(header, _ => instanceService.remove(uid))
    }

    val api = getInstances
        .:+:(getInstance)
        .:+:(addInstance)
        .:+:(updateInstance)
        .:+:(deleteInstance)
        .handle(errorHandler)
}
