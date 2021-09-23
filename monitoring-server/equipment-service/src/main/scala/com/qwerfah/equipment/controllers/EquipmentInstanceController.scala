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
import com.qwerfah.common.exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.common.services._
import com.qwerfah.common.controllers.Controller

object EquipmentInstanceController extends Controller {
    import Startup._
    import Decoders._

    private val instanceService = implicitly[EquipmentInstanceService[Future]]

    private val getInstances = get("instances") {
        for { result <- instanceService.getAll } yield result.asOutput
    }

    private val getInstance = get("instances" :: path[Uid]) { uid: Uid =>
        for { result <- instanceService.get(uid) } yield result.asOutput
    }

    private val addInstance = post(
      "instances" :: jsonBody[AddInstanceRequest]
    ) { request: AddInstanceRequest =>
        for {
            result <- instanceService.add(request)
        } yield result.asOutput
    }

    private val updateInstance =
        patch("instances" :: path[Uid] :: jsonBody[UpdateInstanceRequest]) {
            (uid: Uid, request: UpdateInstanceRequest) =>
                for {
                    result <- instanceService.update(uid, request)
                } yield result.asOutput
        }

    private val deleteInstance = delete("instances" :: path[Uid]) { uid: Uid =>
        for {
            result <- instanceService.remove(uid)
        } yield result.asOutput
    }

    val api =
        (getInstances :+: getInstance :+: addInstance :+: updateInstance :+: deleteInstance)
            .handle(errorHandler)
}
