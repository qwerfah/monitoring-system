package com.qwerfah.reporting.services.default

import java.time.LocalDateTime

import cats.Monad, cats.implicits._

import com.qwerfah.reporting.models._
import com.qwerfah.reporting.Mappings
import com.qwerfah.reporting.repos.OperationRecordRepo
import com.qwerfah.reporting.services.OperationRecordService

import com.qwerfah.common.Uid
import com.qwerfah.common.exceptions._
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.http.HttpMethod
import com.qwerfah.common.util.Conversions._
import com.qwerfah.common.services.response._
import com.qwerfah.common.resources.{RecordRequest, RecordResponse}

class DefaultOperationRecordService[F[_]: Monad, DB[_]: Monad](implicit
  recordRepo: OperationRecordRepo[DB],
  dbManager: DbManager[F, DB]
) extends OperationRecordService[F] {
    import Mappings._

    override def get: F[ServiceResponse[Seq[RecordResponse]]] =
        dbManager.execute(recordRepo.get) map { _.asResponse.as200 }

    override def get(uid: Uid): F[ServiceResponse[RecordResponse]] =
        dbManager.execute(recordRepo.get(uid)) map {
            case Some(value) => value.asResponse.as200
            case None        => NoOperationRecord(uid).as404
        }

    override def get(
      serviceId: Option[String],
      method: Option[HttpMethod],
      status: Option[Int],
      fromDate: Option[LocalDateTime],
      toDate: Option[LocalDateTime]
    ): F[ServiceResponse[Seq[RecordResponse]]] =
        dbManager.execute(
          recordRepo.get(serviceId, method, status, fromDate, toDate)
        ) map { _.asResponse.as200 }

    override def add(
      request: RecordRequest
    ): F[ServiceResponse[RecordResponse]] =
        dbManager.execute(recordRepo.add(request.asRecord)) map {
            _.asResponse.as201
        }

    override def remove(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(recordRepo.remove(uid)) map {
            case 1 => RecordRemoved(uid).as200
            case _ => NoOperationRecord(uid).as404
        }

    override def remove(
      serviceId: String
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(recordRepo.remove(serviceId)) map {
            case 1 => RecordsRemoved(serviceId).as200
            case _ => NoOperationRecords(serviceId).as404
        }

    override def restore(uid: Uid): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(recordRepo.restore(uid)) map {
            case 1 => RecordRestored(uid).as200
            case _ => NoOperationRecord(uid).as404
        }

    override def restore(
      serviceId: String
    ): F[ServiceResponse[ResponseMessage]] =
        dbManager.execute(recordRepo.restore(serviceId)) map {
            case 1 => RecordsRestored(serviceId).as200
            case _ => NoOperationRecords(serviceId).as404
        }
}
