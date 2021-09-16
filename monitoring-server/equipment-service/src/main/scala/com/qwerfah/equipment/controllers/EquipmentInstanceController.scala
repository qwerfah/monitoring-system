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
import com.qwerfah.common.Exceptions._
import com.qwerfah.common.Uid
import com.qwerfah.equipment.Startup
import com.qwerfah.equipment.json.Decoders

object EquipmentInstanceController {
    import Startup._
    import Decoders._

    private val instanceService = implicitly[EquipmentInstanceService[Future]]

    private val getInstances = get("instances") {
        for { result <- instanceService.get } yield result match {
            case ServiceResult(instances) => Ok(instances)
            case ServiceEmpty => NotFound(new Exception("Instance not found"))
        }
    } handle { case e: Exception =>
        InternalServerError(e)
    }

    private val getInstance = get("instances" :: path[Uid]) { uid: Uid =>
        for { result <- instanceService.getByUid(uid) } yield result match {
            case ServiceResult(instance) => Ok(instance)
            case ServiceEmpty => NotFound(new Exception("Instance not found"))
        }
    } handle { case e: Exception =>
        InternalServerError(e)
    }

    private val addInstance =
        post("instances" :: jsonBody[AddInstanceRequest]) {
            request: AddInstanceRequest =>
                for {
                    result <- instanceService.add(request)
                } yield result match {
                    case ServiceResult(instance) => Ok(instance)
                    case ServiceEmpty =>
                        NotFound(new Exception("Model not found"))
                }
        } handle {
            case e: InvalidJsonBodyException => BadRequest(e)
            case e: Exception =>
                InternalServerError(e)
        }

    private val updateInstance =
        patch("instances" :: path[Uid] :: jsonBody[UpdateInstanceRequest]) {
            (uid: Uid, request: UpdateInstanceRequest) =>
                for {
                    result <- instanceService.update(uid, request)
                } yield result match {
                    case ServiceResult(message) => Ok(message)
                    case ServiceEmpty =>
                        NotFound(new Exception("Instance not found"))
                }
        } handle {
            case e: InvalidJsonBodyException => BadRequest(e)
            case e: Exception =>
                InternalServerError(e)
        }

    private val deleteInstance = delete("instances" :: path[Uid]) { uid: Uid =>
        for {
            result <- instanceService.removeByUid(uid)
        } yield result match {
            case ServiceResult(message) => Ok(message)
            case ServiceEmpty =>
                NotFound(new Exception("Instance not found"))
        }
    } handle { case e: Exception =>
        InternalServerError(e)
    }

    val api =
        getInstances :+: getInstance :+: addInstance :+: updateInstance :+: deleteInstance
}
