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

class DefaultEquipmentInstanceService[F[_]: Monad, DB[_]: Monad](implicit
  modelRepo: EquipmentModelRepo[DB],
  instanceRepo: EquipmentInstanceRepo[DB],
  dbManager: DbManager[F, DB]
) extends EquipmentInstanceService[F] {
    import Mappings._

    override def getAll: F[ServiceResponse[Seq[InstanceResponse]]] =
        dbManager.execute(instanceRepo.get) map { _.asResponse.as200 }

    override def get(uid: Uid): F[ServiceResponse[InstanceResponse]] =
        dbManager.execute(instanceRepo.getByUid(uid)) map {
            case Some(instance) => instance.asResponse.as200
            case None           => NoInstance(uid).as404
        }

    override def getByModelUid(
      modelUid: Uid
    ): F[ServiceResponse[Seq[InstanceResponse]]] =
        dbManager.execute(instanceRepo.getByModelUid(modelUid)) map {
            _.asResponse.as200
        }

    override def add(
      modelUid: Uid,
      request: AddInstanceRequest
    ): F[ServiceResponse[InstanceResponse]] =
        dbManager.execute(modelRepo.getByUid(modelUid)) flatMap {
            case Some(model) =>
                dbManager.execute(
                  instanceRepo.add(request.asInstance.copy(modelUid = modelUid))
                ) map {
                    _.asResponse.as201
                }
            case None => Monad[F].pure(NoModel(request.modelUid).as404)
        }

    override def update(
      uid: Uid,
      request: UpdateInstanceRequest
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(
          instanceRepo.update(request.asInstance.copy(uid = uid))
        ) map {
            case 1 => InstanceUpdated(uid).as200
            case _ => NoInstance(uid).as404
        }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(instanceRepo.removeByUid(uid)) map {
            case 1 => InstanceRemoved(uid).as200
            case _ => NoInstance(uid).as404
        }

    override def restore(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(instanceRepo.restoreByUid(uid)) map {
            case 1 => InstanceRestored(uid).as200
            case _ => NoInstance(uid).as404
        }
}
