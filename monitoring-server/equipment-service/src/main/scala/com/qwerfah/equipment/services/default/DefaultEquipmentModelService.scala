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
import com.qwerfah.common.util.Conversions._

class DefaultEquipmentModelService[F[_]: Monad, DB[_]: Monad](implicit
  modelRepo: EquipmentModelRepo[DB],
  instnaceRepo: EquipmentInstanceRepo[DB],
  paramRepo: ParamRepo[DB],
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

    override def getAll: F[ServiceResponse[Seq[ModelResponse]]] =
        dbManager.execute(modelRepo.get) map { _.asResponse.as200 }

    override def get(uid: Uid): F[ServiceResponse[ModelResponse]] =
        dbManager.execute(modelRepo.getByUid(uid)) map {
            case Some(model) => model.asResponse.as200
            case None        => NoModel(uid).as404
        }

    override def add(
      request: ModelRequest
    ): F[ServiceResponse[ModelResponse]] =
        dbManager.execute(modelRepo.add(request.asModel)) map {
            _.asResponse.as201
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
        for {
            result <- dbManager.execute(modelRepo.removeByUid(uid))
            _ <- dbManager.execute(instnaceRepo.removeByModelUid(uid))
            _ <- dbManager.execute(paramRepo.removeByModelUid(uid))
        } yield result match {
            case 1 => ModelRemoved(uid).as200
            case _ => NoModel(uid).as404
        }

    override def restore(uid: Uid): F[ServiceResponse[ResponseMessage]] = for {
        result <- dbManager.execute(modelRepo.restoreByUid(uid))
        _ <- dbManager.execute(instnaceRepo.restoreByModelUid(uid))
        _ <- dbManager.execute(paramRepo.restoreByModelUid(uid))
    } yield result match {
        case 1 => ModelRemoved(uid).as200
        case _ => NoModel(uid).as404
    }

}
