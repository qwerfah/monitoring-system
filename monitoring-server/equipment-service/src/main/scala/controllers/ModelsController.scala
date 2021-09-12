package controllers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.server.TwitterServer
import com.twitter.finagle.http.{Request, Response}
import com.twitter.io.Buf

import io.finch.catsEffect._
import io.circe.{Encoder, Json}
import io.finch._
import io.circe.generic.auto._
import io.circe.syntax._
import io.finch.circe._

import services._
import repos.slick._
import services.default._
import models._

object ModelsController {
    import startup.Startup._

    def encodeErrorList(es: List[Exception]): Json = {
        val messages = es.map(x => Json.fromString(x.getMessage))
        Json.obj("errors" -> Json.arr(messages: _*))
    }

    implicit val encodeException: Encoder[Exception] = Encoder.instance({
        case e: io.finch.Errors => encodeErrorList(e.errors.toList)
        case e: io.finch.Error =>
            e.getCause match {
                case e: io.circe.Errors => encodeErrorList(e.errors.toList)
                case err => Json.obj("message" -> Json.fromString(e.getMessage))
            }
        case e: Exception =>
            Json.obj("message" -> Json.fromString(e.getMessage))
    })

    private val modelsService = implicitly[EquipmentModelService[Future]]

    private val getModels = get("models") {
        for { result <- modelsService.get } yield result match {
            case ServiceResult(models) => Ok(models)
            case ServiceEmpty => NotFound(new Exception("Model not found"))
        }
    }

    private val getModel = get("models" :: path[Uid]) { uid: Uid =>
        for { result <- modelsService.getByGuid(uid) } yield result match {
            case ServiceResult(model) => Ok(model)
            case ServiceEmpty => NotFound(new Exception("Model not found"))
        }
    }

    private val addModel =
        post("models" :: jsonBody[EquipmentModel]) { model: EquipmentModel =>
            for { result <- modelsService.add(model) } yield result match {
                case ServiceResult(model) => Ok(model)
                case ServiceEmpty =>
                    NotFound(new Exception("Model not found"))
            }
        }

    private val updateModel =
        patch("models" :: path[Uid] :: jsonBody[EquipmentModel]) {
            (uid: Uid, model: EquipmentModel) =>
                for {
                    result <- modelsService.update(uid, model)
                } yield result match {
                    case ServiceResult(message) => Ok(message)
                    case ServiceEmpty =>
                        NotFound(new Exception("Model not found"))
                }
        } handle { case e: Exception =>
            BadRequest(e)
        }

    private val deleteModel = delete("models" :: path[Uid]) { guid: Uid =>
        for {
            result <- modelsService.removeByGuid(guid)
        } yield result match {
            case ServiceResult(message) => Ok(message)
            case ServiceEmpty =>
                NotFound(new Exception("Model not found"))
        }
    }

    val api =
        (getModels :+: getModel :+: addModel :+: updateModel :+: deleteModel)
            .toServiceAs[Application.Json]
    /*
    val api = Bootstrap
        .serve[Application.Json](getModels)
        .serve[Application.Json](getModel)
        .serve[Application.Json](addModel)
        .serve[Text.Plain](updateModel)
        .serve[Text.Plain](deleteModel)
     */
}
