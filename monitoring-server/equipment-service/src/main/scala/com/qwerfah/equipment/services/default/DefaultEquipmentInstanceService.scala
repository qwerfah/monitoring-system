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
import com.qwerfah.common.exceptions.NoModel
import com.qwerfah.common.exceptions.NoInstance

class DefaultEquipmentInstanceService[F[_]: Monad, DB[_]: Monad](implicit
  modelRepo: EquipmentModelRepo[DB],
  instanceRepo: EquipmentInstanceRepo[DB],
  dbManager: DbManager[F, DB]
) extends EquipmentInstanceService[F] {
    import Mappings._

    override def getAll: F[ServiceResponse[Seq[InstanceResponse]]] = for {
        instances <- dbManager.execute(instanceRepo.get)
    } yield ObjectResponse(instances)

    override def get(uid: Uid): F[ServiceResponse[InstanceResponse]] =
        for {
            instance <- dbManager.execute(instanceRepo.getByUid(uid))
        } yield instance match {
            case Some(instance) =>
                ObjectResponse(instance)
            case None => NotFoundResponse(NoInstance(uid))
        }

    override def getByModelUid(
      modelUid: Uid
    ): F[ServiceResponse[Seq[InstanceResponse]]] = for {
        instances <- dbManager.execute(instanceRepo.getByModelUid(modelUid))
    } yield ObjectResponse(instances)

    override def add(
      request: AddInstanceRequest
    ): F[ServiceResponse[InstanceResponse]] = {
        dbManager.execute(modelRepo.getByUid(request.modelUid)) flatMap {
            case Some(model) =>
                dbManager.execute(instanceRepo.add(request)) map { instance =>
                    ObjectResponse(instance)
                }
            case None =>
                Monad[F].pure(NotFoundResponse(NoModel(request.modelUid)))
        }
    }

    override def update(
      uid: Uid,
      request: UpdateInstanceRequest
    ): F[ServiceResponse[ResponseMessage]] = {
        val updated: EquipmentInstance = request

        for {
            result <- dbManager.execute(
              instanceRepo.update(updated.copy(uid = uid))
            )
        } yield result match {
            case 1 => ObjectResponse(InstanceUpdated(uid))
            case _ => NotFoundResponse(NoInstance(uid))
        }
    }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        for {
            result <- dbManager.execute(instanceRepo.removeByUid(uid))
        } yield result match {
            case 1 => ObjectResponse(InstanceRemoved(uid))
            case _ => NotFoundResponse(NoInstance(uid))
        }
}
