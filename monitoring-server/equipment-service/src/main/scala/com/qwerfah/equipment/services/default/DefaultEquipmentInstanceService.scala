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

class DefaultEquipmentInstanceService[F[_]: Monad, DB[_]: Monad](implicit
  modelRepo: EquipmentModelRepo[DB],
  instanceRepo: EquipmentInstanceRepo[DB],
  dbManager: DbManager[F, DB]
) extends EquipmentInstanceService[F] {
    import Mappings._

    override def get: F[ServiceResponse[Seq[InstanceResponse]]] = for {
        instances <- dbManager.execute(instanceRepo.get)
    } yield ObjectResponse(instances)

    override def getById(id: Int): F[ServiceResponse[InstanceResponse]] = for {
        instance <- dbManager.execute(instanceRepo.getById(id))
    } yield instance match {
        case Some(instance) =>
            ObjectResponse(instance)
        case None => EmptyResponse
    }

    override def getByUid(uid: Uid): F[ServiceResponse[InstanceResponse]] =
        for {
            instance <- dbManager.execute(instanceRepo.getByUid(uid))
        } yield instance match {
            case Some(instance) =>
                ObjectResponse(instance)
            case None => EmptyResponse
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
            case None => Monad[F].pure(EmptyResponse)
        }
        /*
        for {
            model <- dbManager.execute(repo.getByGuid(request.modelUid))
            result <- EitherT.cond[F](
              model.isEmpty,
              Monad[F].pure(EmptyResponse),
              dbManager.execute(repo.add(request))
            )
        } yield result match {
            case EmptyResponse => EmptyResponse

        }
         */
    }

    override def update(
      uuid: Uid,
      request: UpdateInstanceRequest
    ): F[ServiceResponse[String]] = {
        val updated: EquipmentInstance = request

        dbManager.execute(instanceRepo.getByUid(uuid)) flatMap {
            case Some(instance) =>
                dbManager.execute(
                  instanceRepo.update(
                    updated.copy(
                      id = instance.id,
                      uid = uuid,
                      modelUid = instance.modelUid
                    )
                  )
                ) map {
                    case 1 => StringResponse("Instance updated")
                    case _ => EmptyResponse
                }
            case None => Monad[F].pure(EmptyResponse)
        }
    }

    override def removeById(id: Int): F[ServiceResponse[String]] = for {
        result <- dbManager.execute(instanceRepo.removeById(id))
    } yield result match {
        case 1 => StringResponse("Instance removed")
        case _ => EmptyResponse
    }

    override def removeByUid(uid: Uid): F[ServiceResponse[String]] = for {
        result <- dbManager.execute(instanceRepo.removeByUid(uid))
    } yield result match {
        case 1 => StringResponse("Instance removed")
        case _ => EmptyResponse
    }
}
