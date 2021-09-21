package com.qwerfah.equipment.services.default

import cats.Monad
import cats.implicits._

import com.qwerfah.equipment.repos._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.services._
import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.Mappings
import com.qwerfah.common.Uid
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.services.response._
import com.qwerfah.common.exceptions._

class DefaultEquipmentModelService[F[_]: Monad, DB[_]: Monad](implicit
  repo: EquipmentModelRepo[DB],
  dbManager: DbManager[F, DB]
) extends EquipmentModelService[F] {
    import Mappings._

    private def validateGuid(uid: Uid): Boolean =
        try {
            java.util.UUID.fromString(uid.toString)
            true
        } catch {
            case _: IllegalArgumentException => false
        }

    override def getAll: F[ServiceResponse[Seq[ModelResponse]]] = for {
        models <- dbManager.execute(repo.get)
    } yield ObjectResponse(models)

    override def get(uid: Uid): F[ServiceResponse[ModelResponse]] =
        for {
            model <- dbManager.execute(repo.getByUid(uid))
        } yield model match {
            case Some(model) => ObjectResponse(model)
            case None        => NotFoundResponse(NoModel(uid))
        }

    override def add(
      request: ModelRequest
    ): F[ServiceResponse[ModelResponse]] = {
        for {
            result <- dbManager.execute(repo.add(request))
        } yield ObjectResponse(result)
    }

    override def update(
      uid: Uid,
      request: ModelRequest
    ): F[ServiceResponse[ResponseMessage]] = {
        val model: EquipmentModel = request
        for {
            result <- dbManager.execute(repo.update(model.copy(uid = uid)))
        } yield result match {
            case 1 => ObjectResponse(ModelUpdated(uid))
            case _ => NotFoundResponse(NoModel(uid))
        }
    }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] = for {
        result <- dbManager.execute(repo.removeByUid(uid))
    } yield result match {
        case 1 => ObjectResponse(ModelRemoved(uid))
        case _ => NotFoundResponse(NoModel(uid))
    }
}
