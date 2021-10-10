package com.qwerfah.generator.services.default

import java.time.LocalDateTime

import cats.Monad
import cats.implicits._

import com.qwerfah.generator.repos.ParamValueRepo
import com.qwerfah.generator.services.GeneratorService

import com.qwerfah.equipment.resources._
import com.qwerfah.equipment.json.Decoders

import com.qwerfah.common.http._
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.{Uid, randomUid}
import com.qwerfah.common.util.Conversions._
import com.qwerfah.common.services.response._
import com.qwerfah.generator.models.ParamValue
import com.qwerfah.common.exceptions.NonexistentInstance

class DefaultGeneratorService[F[_]: Monad, DB[_]: Monad](
  monitoringClient: HttpClient[F],
  equipmentClient: HttpClient[F]
)(implicit paramValueRepo: ParamValueRepo[DB], dbManager: DbManager[F, DB])
  extends GeneratorService[F] {
    import Decoders._

    private def addParamValues: F[ServiceResponse[ResponseMessage]] =
        monitoringClient
            .sendAndDecode[Seq[Uid]](
              HttpMethod.Get,
              "/api/monitors/instances"
            ) flatMap {
            case sr: SuccessResponse[Seq[Uid]] =>
                sr.result.map(uid => addParamValues(uid)).sequence map { seq =>
                    seq.collect { case r: SuccessResponse[ResponseMessage] =>
                        r
                    }.headOption
                        .getOrElse(NonexistentInstance.as409)
                }
            case e: ErrorResponse => Monad[F].pure(e)
        }

    private def addParamValues(
      uid: Uid
    ): F[ServiceResponse[ResponseMessage]] = equipmentClient
        .sendAndDecode[Seq[ParamResponse]](
          HttpMethod.Get,
          s"/api/instances/$uid/params"
        ) flatMap {
        case sr: SuccessResponse[Seq[ParamResponse]] =>
            addParamValues(sr.result, uid)
        case e: ErrorResponse => Monad[F].pure(e)
    }

    private def addParamValues(
      params: Seq[ParamResponse],
      instanceUid: Uid
    ): F[ServiceResponse[ResponseMessage]] = {
        val rand = new scala.util.Random
        dbManager.execute(dbManager.sequence(params map { param =>
            paramValueRepo.add(
              ParamValue(
                None,
                randomUid,
                param.uid,
                instanceUid,
                rand.between(0, 300).toString,
                LocalDateTime.now,
                false
              )
            )
        })) map { _ => ParamValuesAdded.as201 }
    }

    def generate: F[ServiceResponse[ResponseMessage]] = addParamValues
}
