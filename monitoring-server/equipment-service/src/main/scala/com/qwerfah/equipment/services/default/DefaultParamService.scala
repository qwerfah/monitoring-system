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

class DefaultParamService[F[_]: Monad, DB[_]: Monad](implicit
  modelRepo: EquipmentModelRepo[DB],
  paramRepo: ParamRepo[DB],
  dbManager: DbManager[F, DB]
) extends ParamService[F] {
    import Mappings._

    override def getAll: F[ServiceResponse[Seq[ParamResponse]]] = for {
        params <- dbManager.execute(paramRepo.get)
    } yield ObjectResponse(params)

    override def get(uid: Uid): F[ServiceResponse[ParamResponse]] =
        for {
            result <- dbManager.execute(paramRepo.getByUid(uid))
        } yield result match {
            case Some(param) => ObjectResponse(param)
            case None        => NotFoundResponse(NoParam(uid))
        }

    override def getByModelUid(
      modelUid: Uid
    ): F[ServiceResponse[Seq[ParamResponse]]] = for {
        params <- dbManager.execute(paramRepo.getByModelUid(modelUid))
    } yield ObjectResponse(params)

    override def add(
      request: AddParamRequest
    ): F[ServiceResponse[ParamResponse]] = {
        dbManager.execute(modelRepo.getByUid(request.modelUid)) flatMap {
            case Some(model) =>
                dbManager.execute(paramRepo.add(request)) map { param =>
                    ObjectResponse(param)
                }
            case None =>
                Monad[F].pure(NotFoundResponse(NoModel(request.modelUid)))
        }
    }

    override def update(
      uid: Uid,
      request: UpdateParamRequest
    ): F[ServiceResponse[ResponseMessage]] = {
        val param: Param = request
        for {
            result <- dbManager.execute(
              paramRepo.update(param.copy(uid = uid))
            )
        } yield result match {
            case 1 => ObjectResponse(ParamUpdated(uid))
            case _ => NotFoundResponse(NoParam(uid))
        }
    }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] = for {
        result <- dbManager.execute(paramRepo.removeByUid(uid))
    } yield result match {
        case 1 => ObjectResponse(ParamRemoved(uid))
        case _ => NotFoundResponse(NoParam(uid))
    }
}
