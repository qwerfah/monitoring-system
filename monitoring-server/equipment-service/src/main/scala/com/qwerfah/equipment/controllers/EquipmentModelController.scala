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
import io.circe.syntax._
import io.circe.{Encoder, Json}

import com.qwerfah.equipment.services._
import com.qwerfah.equipment.repos.slick._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.Startup

object EquipmentModelController extends Controller {
    import Startup._

    private val modelService = implicitly[EquipmentModelService[Future]]

    private val getModels = get("models") {
        for { result <- modelService.get } yield result match {
            case ServiceResult(models) => Ok(models)
            case ServiceEmpty => NotFound(new Exception("Model not found"))
        }
    } handle { case e: Exception =>
        InternalServerError(e)
    }

    private val getModel = get("models" :: path[Uid]) { uid: Uid =>
        for { result <- modelService.getByGuid(uid) } yield result match {
            case ServiceResult(model) => Ok(model)
            case ServiceEmpty => NotFound(new Exception("Model not found"))
        }
    } handle { case e: Exception =>
        InternalServerError(e)
    }

    private val addModel =
        post("models" :: jsonBody[ModelRequest]) { request: ModelRequest =>
            for { result <- modelService.add(request) } yield result match {
                case ServiceResult(model) => Ok(model)
                case ServiceEmpty =>
                    NotFound(new Exception("Model not found"))
            }
        } handle { case e: Exception =>
            InternalServerError(e)
        }

    private val updateModel =
        patch("models" :: path[Uid] :: jsonBody[ModelRequest]) {
            (uid: Uid, request: ModelRequest) =>
                for {
                    result <- modelService.update(uid, request)
                } yield result match {
                    case ServiceResult(message) => Ok(message)
                    case ServiceEmpty =>
                        NotFound(new Exception("Model not found"))
                }
        } handle { case e: Exception =>
            InternalServerError(e)
        }

    private val deleteModel = delete("models" :: path[Uid]) { guid: Uid =>
        for {
            result <- modelService.removeByGuid(guid)
        } yield result match {
            case ServiceResult(message) => Ok(message)
            case ServiceEmpty =>
                NotFound(new Exception("Model not found"))
        }
    } handle { case e: Exception =>
        InternalServerError(e)
    }

    val api =
        getModels :+: getModel :+: addModel :+: updateModel :+: deleteModel
}
