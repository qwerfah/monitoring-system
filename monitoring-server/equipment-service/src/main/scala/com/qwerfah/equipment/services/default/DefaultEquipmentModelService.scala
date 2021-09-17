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
import com.qwerfah.common.services._

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

    override def get: F[ServiceResponse[Seq[ModelResponse]]] = for {
        models <- dbManager.execute(repo.get)
    } yield ServiceResult(models)

    override def getById(id: Int): F[ServiceResponse[ModelResponse]] = for {
        model <- dbManager.execute(repo.getById(id))
    } yield model match {
        case Some(model) =>
            ServiceResult(model)
        case None => ServiceEmpty
    }

    override def getByUid(uid: Uid): F[ServiceResponse[ModelResponse]] =
        for {
            model <- dbManager.execute(repo.getByUid(uid))
        } yield model match {
            case Some(model) =>
                ServiceResult(model)
            case None => ServiceEmpty
        }

    override def add(
      request: ModelRequest
    ): F[ServiceResponse[ModelResponse]] = {
        for {
            result <- dbManager.execute(repo.add(request))
        } yield ServiceResult(result)
    }

    override def update(
      uuid: Uid,
      request: ModelRequest
    ): F[ServiceResponse[String]] = {
        val model: EquipmentModel = request
        for {
            result <- dbManager.execute(repo.update(model.copy(uid = uuid)))
        } yield result match {
            case 1 => ServiceResult("Model updated")
            case _ => ServiceEmpty
        }
    }

    override def removeById(id: Int): F[ServiceResponse[String]] = for {
        result <- dbManager.execute(repo.removeById(id))
    } yield result match {
        case 1 => ServiceResult("Model removed")
        case _ => ServiceEmpty
    }

    override def removeByUid(uid: Uid): F[ServiceResponse[String]] = for {
        result <- dbManager.execute(repo.removeByUid(uid))
    } yield result match {
        case 1 => ServiceResult("Model removed")
        case _ => ServiceEmpty
    }
}
