package controllers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}

import io.finch._
import io.finch.catsEffect._
import io.finch.circe._

import io.circe.generic.auto._

import services._
import repos.slick._
import services.default._
import models._
import java.security.InvalidKeyException
import cats.effect.IO

object ModelsController {
    import main.Startup._

    private val modelsService = implicitly[EquipmentModelService[Future]]

    private def getModels = get("models") {
        for { result <- modelsService.get } yield result match {
            case ServiceResult(models) => Ok(models)
            case ServiceEmpty => NotFound(new Exception("Model not found"))
        }
    }

    private def getModel = get("models" :: path[Uid]) { uid: Uid =>
        for { result <- modelsService.getByGuid(uid) } yield result match {
            case ServiceResult(model) => Ok(model)
            case ServiceEmpty => NotFound(new Exception("Model not found"))
        }
    }

    private def addModel =
        post("models" :: jsonBody[EquipmentModel]) { model: EquipmentModel =>
            for { result <- modelsService.add(model) } yield result match {
                case ServiceResult(model) => Ok(model)
                case ServiceEmpty =>
                    NotFound(new Exception("Model not found"))
            }
        }

    private def updateModel =
        patch("models" :: path[Uid] :: jsonBody[EquipmentModel]) {
            (uid: Uid, model: EquipmentModel) =>
                for {
                    result <- modelsService.update(uid, model)
                } yield result match {
                    case ServiceResult(message) => Ok(message)
                    case ServiceEmpty =>
                        NotFound(new InvalidKeyException("Model not found"))
                }
        } handle { case e: Exception =>
            BadRequest(e)
        }

    private def deleteModel = delete("models" :: path[Uid]) { guid: Uid =>
        for {
            result <- modelsService.removeByGuid(guid)
        } yield result match {
            case ServiceResult(message) => Ok(message)
            case ServiceEmpty =>
                NotFound(new Exception("Model not found"))
        }
    }

    def api = Bootstrap
        .serve[Application.Json](getModels)
        .serve[Application.Json](getModel)
        .serve[Application.Json](addModel)
        .serve[Text.Plain](updateModel)
        .serve[Text.Plain](deleteModel)
}
