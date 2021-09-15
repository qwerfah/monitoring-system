package com.qwerfah.equipment.services.default

import cats.Monad
import cats.data.EitherT
import cats.implicits._

import com.qwerfah.equipment.repos._
import com.qwerfah.equipment.models._
import com.qwerfah.equipment.services._
import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.Mappings

class DefaultEquipmentInstanceService[F[_]: Monad, DB[_]: Monad](implicit
  repo: EquipmentInstanceRepo[DB],
  dbManager: DbManager[F, DB]
) extends EquipmentInstanceService[F] {
    import Mappings._

    override def get: F[ServiceResponse[Seq[InstanceResponse]]] = for {
        instances <- dbManager.execute(repo.get)
    } yield ServiceResult(instances)

    override def getById(id: Int): F[ServiceResponse[InstanceResponse]] = for {
        instance <- dbManager.execute(repo.getById(id))
    } yield instance match {
        case Some(instance) =>
            ServiceResult(instance)
        case None => ServiceEmpty
    }

    override def getByGuid(uid: Uid): F[ServiceResponse[InstanceResponse]] =
        for {
            instance <- dbManager.execute(repo.getByGuid(uid))
        } yield instance match {
            case Some(instance) =>
                ServiceResult(instance)
            case None => ServiceEmpty
        }

    override def add(
      request: AddInstanceRequest
    ): F[ServiceResponse[InstanceResponse]] = {
        dbManager.execute(repo.getByGuid(request.modelUid)) flatMap {
            case Some(model) =>
                dbManager.execute(repo.add(request)) map { instance =>
                    ServiceResult(instance)
                }
            case None => Monad[F].pure(ServiceEmpty)
        }
        /*
        for {
            model <- dbManager.execute(repo.getByGuid(request.modelUid))
            result <- EitherT.cond[F](
              model.isEmpty,
              Monad[F].pure(ServiceEmpty),
              dbManager.execute(repo.add(request))
            )
        } yield result match {
            case ServiceEmpty => ServiceEmpty

        }
         */
    }

    override def update(
      uuid: Uid,
      request: UpdateInstanceRequest
    ): F[ServiceResponse[String]] = {
        val instance: EquipmentInstance = request
        for {
            result <- dbManager.execute(repo.update(instance.copy(uid = uuid)))
        } yield result match {
            case 1 => ServiceResult("Instance updated")
            case _ => ServiceEmpty
        }
    }

    override def removeById(id: Int): F[ServiceResponse[String]] = for {
        result <- dbManager.execute(repo.removeById(id))
    } yield result match {
        case 1 => ServiceResult("Instance removed")
        case _ => ServiceEmpty
    }

    override def removeByGuid(uid: Uid): F[ServiceResponse[String]] = for {
        result <- dbManager.execute(repo.removeByGuid(uid))
    } yield result match {
        case 1 => ServiceResult("Instance removed")
        case _ => ServiceEmpty
    }
}