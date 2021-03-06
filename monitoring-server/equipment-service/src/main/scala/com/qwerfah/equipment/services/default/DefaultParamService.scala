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
import com.qwerfah.common.http.{HttpClient, HttpMethod}
import com.twitter.finagle.http.Status

class DefaultParamService[F[_]: Monad, DB[_]: Monad](
  monitoringClient: HttpClient[F],
  generatorClient: HttpClient[F]
)(implicit
  modelRepo: EquipmentModelRepo[DB],
  instanceRepo: EquipmentInstanceRepo[DB],
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

    override def getByInstanceUid(
      instanceUid: Uid
    ): F[ServiceResponse[Seq[ParamResponse]]] =
        dbManager.execute(instanceRepo.getByUid(instanceUid)) flatMap {
            case Some(instance) =>
                dbManager.execute(
                  paramRepo.getByModelUid(instance.modelUid)
                ) map {
                    _.asResponse.as200
                }
            case None => Monad[F].pure(NoInstance(instanceUid).as404)
        }

    override def add(
      modelUid: Uid,
      request: AddParamRequest
    ): F[ServiceResponse[ParamResponse]] =
        dbManager.execute(modelRepo.getByUid(modelUid)) flatMap {
            case Some(model) =>
                dbManager.execute(
                  paramRepo.add(request.asParam(modelUid))
                ) map {
                    _.asResponse.as201
                }
            case None => Monad[F].pure(NoModel(modelUid).as404)
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
        dbManager.execute(paramRepo.removeByUid(uid)) flatMap {
            case 1 =>
                monitoringClient.send(
                  HttpMethod.Delete,
                  s"/api/monitors/params/$uid"
                ) flatMap {
                    case response if response.status == Status.Ok =>
                        generatorClient.send(
                          HttpMethod.Delete,
                          s"/api/params/$uid/values"
                        ) map {
                            case response if response.status == Status.Ok =>
                                ParamRemoved(uid).as200
                            case _ =>
                                monitoringClient.send(
                                  HttpMethod.Patch,
                                  s"/api/monitors/params/$uid/restore"
                                )
                                dbManager.execute(
                                  paramRepo.restoreByUid(uid)
                                )
                                BadParamRemove(uid).as422
                        }

                    case _ =>
                        dbManager.execute(paramRepo.restoreByUid(uid)) map {
                            _ => BadParamRemove(uid).as422
                        }

                }
            case _ => Monad[F].pure(NoParam(uid).as404)
        }

    override def restore(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(paramRepo.restoreByUid(uid)) map {
            case 1 => ParamRestored(uid).as200
            case _ => NoParam(uid).as404
        }

}
