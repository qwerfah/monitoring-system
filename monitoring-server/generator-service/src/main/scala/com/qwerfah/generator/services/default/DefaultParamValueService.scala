package com.qwerfah.generator.services.default

import cats.Monad
import cats.implicits._

import com.qwerfah.generator.Mappings
import com.qwerfah.generator.resources._
import com.qwerfah.generator.repos.ParamValueRepo
import com.qwerfah.generator.services.ParamValueService

import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.json.Decoders

import com.qwerfah.common.Uid
import com.qwerfah.common.exceptions._
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.http.HttpClient
import com.qwerfah.common.util.Conversions._
import com.qwerfah.common.services.response._

class DefaultParamValueService[F[_]: Monad, DB[_]: Monad](
  client: HttpClient[F]
)(implicit
  paramValueRepo: ParamValueRepo[DB],
  dbManager: DbManager[F, DB]
) extends ParamValueService[F] {
    import Mappings._
    import Decoders._

    override def get: F[ServiceResponse[Seq[ParamValueResponse]]] =
        dbManager.execute(paramValueRepo.get) map { _.asResponse.as200 }

    override def get(uid: Uid): F[ServiceResponse[ParamValueResponse]] =
        dbManager.execute(paramValueRepo.get(uid)) map {
            case Some(value) => value.asResponse.as200
            case None        => NoParamValue(uid).as404
        }

    override def get(
      paramUid: Option[Uid],
      instanceUid: Option[Uid]
    ): F[ServiceResponse[Seq[ParamValueResponse]]] =
        dbManager.execute(paramValueRepo.get(paramUid, instanceUid)) map {
            _.asResponse.as200
        }

    override def getLast(
      paramUid: Uid,
      instanceUid: Uid
    ): F[ServiceResponse[ParamValueResponse]] =
        dbManager.execute(paramValueRepo.getLast(paramUid, instanceUid)) map {
            case Some(value) => value.asResponse.as200
            case None        => NoParamValues(paramUid, instanceUid).as404
        }

    override def add(
      request: ParamValueRequest
    ): F[ServiceResponse[ParamValueResponse]] =
        (for {
            req1 <- client.sendAndDecode[InstanceResponse](
              url = s"/api/instances/${request.instanceUid}"
            )
            req2 <- client.sendAndDecode[ParamResponse](
              url = s"/api/params/${request.paramUid}"
            )
        } yield (req1, req2)) flatMap {
            case (OkResponse(_), OkResponse(_)) =>
                dbManager.execute(paramValueRepo.add(request.asValue)) map {
                    _.asResponse.as201
                }
            case (e: ErrorResponse, _) => Monad[F].pure(e)
            case (_, e: ErrorResponse) => Monad[F].pure(e)
        }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(paramValueRepo.remove(uid)) map {
            case 1 => ParamValueRemoved(uid).as200
            case _ => NoParamValue(uid).as404
        }

    override def removeByParamUid(
      paramUid: Uid
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(paramValueRepo.removeByParamUid(paramUid)) map { _ =>
            ParamValuesRemoved(paramUid).as200
        }

    override def removeByInstanceUid(
      instanceUid: Uid
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(paramValueRepo.removeByInstanceUid(instanceUid)) map {
            _ => InstanceParamValuesRemoved(instanceUid).as200
        }

    override def restore(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(paramValueRepo.restore(uid)) map {
            case 1 => ParamValueRestored(uid).as200
            case _ => NoParamValue(uid).as404
        }

    override def restoreByParamUid(
      paramUid: Uid
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(paramValueRepo.restoreByParamUid(paramUid)) map { _ =>
            ParamValuesRestored(paramUid).as200
        }

    override def restoreByInstanceUid(
      instanceUid: Uid
    ): F[ServiceResponse[ResponseMessage]] = dbManager.execute(
      paramValueRepo.restoreByInstanceUid(instanceUid)
    ) map { _ =>
        InstanceParamValuesRestored(instanceUid).as200
    }
}
