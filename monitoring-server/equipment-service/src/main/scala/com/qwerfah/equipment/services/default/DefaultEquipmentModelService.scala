package com.qwerfah.equipment.services.default

import com.twitter.finagle.http.{Request, Response, Method, Status}

import cats.Monad
import cats.implicits._

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import com.qwerfah.equipment.repos._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.services._
import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.Mappings

import com.qwerfah.documentation.resources.FileMetaResponse
import com.qwerfah.documentation.json.{Decoders => DocumentationDecoders}

import com.qwerfah.common.Uid
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.services.response._
import com.qwerfah.common.exceptions._
import com.qwerfah.common.util.Conversions._
import com.qwerfah.common.http.HttpClient
import com.qwerfah.common.http.HttpMethod

class DefaultEquipmentModelService[F[_]: Monad, DB[_]: Monad](
  documentationClient: HttpClient[F]
)(implicit
  modelRepo: EquipmentModelRepo[DB],
  instnaceRepo: EquipmentInstanceRepo[DB],
  paramRepo: ParamRepo[DB],
  dbManager: DbManager[F, DB],
  instanceService: EquipmentInstanceService[F]
) extends EquipmentModelService[F] {
    import Mappings._
    import DocumentationDecoders._

    override def getAll: F[ServiceResponse[Seq[ModelResponse]]] =
        dbManager.execute(modelRepo.get) map { _.asResponse.as200 }

    override def get(uid: Uid): F[ServiceResponse[ModelResponse]] =
        dbManager.execute(modelRepo.getByUid(uid)) map {
            case Some(model) => model.asResponse.as200
            case None        => NoModel(uid).as404
        }

    override def getFiles(uid: Uid): F[ServiceResponse[FileMetaResponse]] =
        dbManager.execute(modelRepo.getByUid(uid)) flatMap {
            case Some(model) =>
                documentationClient.sendAndDecode[FileMetaResponse](
                  HttpMethod.Get,
                  s"/api/models/$uid/files"
                )
            case None => Monad[F].pure(NoModel(uid).as404)
        }

    override def add(
      request: ModelRequest
    ): F[ServiceResponse[ModelResponse]] = {
        val model = request.asModel
        val actions = Seq(
          modelRepo.add(model),
          dbManager.sequence(
            request.params
                .getOrElse(Seq())
                .map(p => paramRepo.add(p.asParam(model.uid)))
          ) map { _ => model }
        )

        dbManager.executeTransactionally(dbManager.sequence(actions)) map {
            _.head.asResponse.as201
        }
    }

    override def addFile(uid: Uid, request: Request): F[Response] =
        dbManager.execute(modelRepo.getByUid(uid)) flatMap {
            case Some(model) => {
                request.uri_=(s"/api/models/$uid/files")
                request.method_=(Method.Post)
                documentationClient.send(request)
            }
            case None => {
                val response = Response(Status.NotFound)
                response.setContentType("application/json")
                response.setContentString(NoModel(uid).asJson.toString)
                Monad[F].pure(response)
            }
        }

    override def update(
      uid: Uid,
      request: ModelRequest
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(
          modelRepo.update(request.asModel.copy(uid = uid))
        ) map {
            case 1 => ModelUpdated(uid).as200
            case _ => NoModel(uid).as404
        }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(modelRepo.removeByUid(uid)) flatMap {
            case 1 =>
                for {
                    _ <- dbManager.execute(instnaceRepo.removeByModelUid(uid))
                    _ <- dbManager.execute(paramRepo.removeByModelUid(uid))
                } yield ModelRemoved(uid).as200
            case _ => Monad[F].pure(NoModel(uid).as404)
        }

    override def restore(uid: Uid): F[ServiceResponse[ResponseMessage]] = for {
        result <- dbManager.execute(modelRepo.restoreByUid(uid))
        _ <- dbManager.execute(instnaceRepo.restoreByModelUid(uid))
        _ <- dbManager.execute(paramRepo.restoreByModelUid(uid))
    } yield result match {
        case 1 => ModelRestored(uid).as200
        case _ => NoModel(uid).as404
    }

}
