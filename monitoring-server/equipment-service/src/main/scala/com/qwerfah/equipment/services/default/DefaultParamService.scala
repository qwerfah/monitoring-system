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

class DefaultParamService[F[_]: Monad, DB[_]: Monad](implicit
  modelRepo: EquipmentModelRepo[DB],
  paramRepo: ParamRepo[DB],
  dbManager: DbManager[F, DB]
) extends ParamService[F] {
    import Mappings._

    override def getAll: F[ServiceResponse[Seq[ParamResponse]]] =
        dbManager.execute(paramRepo.get) map { _.asResponse.as200 }

    override def get(uid: Uid): F[ServiceResponse[ParamResponse]] =
        dbManager.execute(paramRepo.getByUid(uid)) map {
            case Some(param) => param.asResponse.as200
            case None        => NoParam(uid).as404
        }

    override def getByModelUid(
      modelUid: Uid
    ): F[ServiceResponse[Seq[ParamResponse]]] =
        dbManager.execute(paramRepo.getByModelUid(modelUid)) map {
            _.asResponse.as200
        }

    override def add(
      request: AddParamRequest
    ): F[ServiceResponse[ParamResponse]] =
        dbManager.execute(modelRepo.getByUid(request.modelUid)) flatMap {
            case Some(model) =>
                dbManager.execute(paramRepo.add(request.asParam)) map {
                    _.asResponse.as201
                }
            case None => Monad[F].pure(NoModel(request.modelUid).as404)
        }

    override def update(
      uid: Uid,
      request: UpdateParamRequest
    ): F[ServiceResponse[ResponseMessage]] = dbManager.execute(
      paramRepo.update(request.asParam.copy(uid = uid))
    ) map {
        case 1 => ParamUpdated(uid).as200
        case _ => NoParam(uid).as404
    }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(paramRepo.removeByUid(uid)) map {
            case 1 => ParamRemoved(uid).as200
            case _ => NoParam(uid).as404
        }
}
