package services.default

import cats.Monad
import cats.implicits._

import repos._
import models._
import services._
import cats.data.OptionT

class DefaultEquipmentModelService[F[_]: Monad, DB[_]: Monad](implicit
  repo: EquipmentModelRepo[DB],
  dbManager: DbManager[F, DB]
) extends EquipmentModelService[F] {

    private def validateGuid(uid: Uid): Boolean =
        try {
            java.util.UUID.fromString(uid.toString)
            true
        } catch {
            case _: IllegalArgumentException => false
        }

    override def get: F[ServiceResponse[Seq[EquipmentModel]]] = for {
        models <- dbManager.execute(repo.get)
    } yield ServiceResult(models)

    override def getById(id: Int): F[ServiceResponse[EquipmentModel]] = for {
        model <- dbManager.execute(repo.getById(id))
    } yield model match {
        case Some(model) =>
            ServiceResult(model)
        case None => ServiceEmpty
    }

    override def getByGuid(uid: Uid): F[ServiceResponse[EquipmentModel]] =
        for {
            model <- dbManager.execute(repo.getByGuid(uid))
        } yield model match {
            case Some(model) =>
                ServiceResult(model)
            case None => ServiceEmpty
        }

    override def add(
      model: EquipmentModel
    ): F[ServiceResponse[EquipmentModel]] = {
        val guid = randomUid
        val updated = model.copy(uid = guid)
        for {
            result <- dbManager.execute(repo.add(updated))
        } yield ServiceResult(result)
    }

    override def update(
      uuid: Uid,
      model: EquipmentModel
    ): F[ServiceResponse[String]] = {
        for {
            res <- dbManager.execute(repo.update(model.copy(uid = uuid)))
        } yield res match {
            case 1 => ServiceResult("Model updated")
            case _ => ServiceEmpty
        }
    }

    override def removeById(id: Int): F[ServiceResponse[String]] = for {
        res <- dbManager.execute(repo.removeById(id))
    } yield res match {
        case 1 => ServiceResult("Model removed")
        case _ => ServiceEmpty
    }

    override def removeByGuid(uid: Uid): F[ServiceResponse[String]] = for {
        res <- dbManager.execute(repo.removeByGuid(uid))
    } yield res match {
        case 1 => ServiceResult("Model removed")
        case _ => ServiceEmpty
    }
}
