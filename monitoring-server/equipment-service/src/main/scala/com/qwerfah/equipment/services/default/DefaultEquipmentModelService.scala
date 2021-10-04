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

    override def getAll: F[ServiceResponse[Seq[ModelResponse]]] =
        dbManager.execute(repo.get) map { _.asResponse.as200 }

    override def get(uid: Uid): F[ServiceResponse[ModelResponse]] =
        dbManager.execute(repo.getByUid(uid)) map {
            case Some(model) => model.asResponse.as200
            case None        => NoModel(uid).as404
        }

    override def add(
      request: ModelRequest
    ): F[ServiceResponse[ModelResponse]] =
        dbManager.execute(repo.add(request.asModel)) map { _.asResponse.as201 }

    override def update(
      uid: Uid,
      request: ModelRequest
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(repo.update(request.asModel.copy(uid = uid))) map {
            case 1 => ModelUpdated(uid).as200
            case _ => NoModel(uid).as404
        }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(repo.removeByUid(uid)) map {
            case 1 => ModelRemoved(uid).as200
            case _ => NoModel(uid).as404
        }
}
