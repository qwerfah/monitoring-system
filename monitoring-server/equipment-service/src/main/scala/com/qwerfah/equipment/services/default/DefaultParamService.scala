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

class DefaultParamService[F[_]: Monad, DB[_]: Monad](implicit
  modelRepo: EquipmentModelRepo[DB],
  paramRepo: ParamRepo[DB],
  dbManager: DbManager[F, DB]
) extends ParamService[F] {
    import Mappings._

    override def get: F[ServiceResponse[Seq[ParamResponse]]] = for {
        params <- dbManager.execute(paramRepo.get)
    } yield ServiceResult(params)

    override def getById(id: Int): F[ServiceResponse[ParamResponse]] = for {
        result <- dbManager.execute(paramRepo.getById(id))
    } yield result match {
        case Some(param) =>
            ServiceResult(param)
        case None => ServiceEmpty
    }

    override def getByUid(uid: Uid): F[ServiceResponse[ParamResponse]] =
        for {
            result <- dbManager.execute(paramRepo.getByUid(uid))
        } yield result match {
            case Some(param) =>
                ServiceResult(param)
            case None => ServiceEmpty
        }

    override def getByModelUid(
      modelUid: Uid
    ): F[ServiceResponse[Seq[ParamResponse]]] = for {
        params <- dbManager.execute(paramRepo.getByModelUid(modelUid))
    } yield ServiceResult(params)

    override def add(
      request: AddParamRequest
    ): F[ServiceResponse[ParamResponse]] = {
        dbManager.execute(modelRepo.getByUid(request.modelUid)) flatMap {
            case Some(model) =>
                dbManager.execute(paramRepo.add(request)) map { param =>
                    ServiceResult(param)
                }
            case None => Monad[F].pure(ServiceEmpty)
        }
    }

    override def update(
      uuid: Uid,
      request: UpdateParamRequest
    ): F[ServiceResponse[String]] = {
        val param: Param = request
        for {
            result <- dbManager.execute(
              paramRepo.update(param.copy(uid = uuid))
            )
        } yield result match {
            case 1 => ServiceResult("Param updated")
            case _ => ServiceEmpty
        }
    }

    override def removeById(id: Int): F[ServiceResponse[String]] = for {
        result <- dbManager.execute(paramRepo.removeById(id))
    } yield result match {
        case 1 => ServiceResult("Param removed")
        case _ => ServiceEmpty
    }

    override def removeByUid(uid: Uid): F[ServiceResponse[String]] = for {
        result <- dbManager.execute(paramRepo.removeByUid(uid))
    } yield result match {
        case 1 => ServiceResult("Param removed")
        case _ => ServiceEmpty
    }
}
