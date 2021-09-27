package com.qwerfah.reporting.services

import java.time.LocalDateTime

import com.qwerfah.common.Uid
import com.qwerfah.common.http.HttpMethod
import com.qwerfah.common.services.response.{ServiceResponse, ResponseMessage}
import com.qwerfah.reporting.resources._

/** Provide operation records service operations. */
trait OperationRecordService[F[_]] {

    /** Get all operation records from storage.
      * @return
      *   All operation records in storage.
      */
    def get: F[ServiceResponse[Seq[RecordResponse]]]

    /** Get a single operation record by its uid.
      * @param uid
      *   Operation record uid.
      * @return
      *   Operation record if found, otherwise error.
      */
    def get(uid: Uid): F[ServiceResponse[RecordResponse]]

    /** Get all operation records that meets the request parameters.
      * @param serviceId
      *   Identifier of service that performed operation.
      * @param method
      *   Http method of operation.
      * @param status
      *   Status code of service response.
      * @param interval
      *   Time interval during which operation occurred.
      * @return
      *   All records that meets the request parameters.
      */
    def get(
      serviceId: Option[String],
      method: Option[HttpMethod],
      status: Option[Int],
      interval: Option[(LocalDateTime, LocalDateTime)]
    ): F[ServiceResponse[Seq[RecordResponse]]]

    /** Add new operation record to the storage.
      * @param record
      *   Operation record data.
      * @return
      *   New operation record instance that was added to the storage.
      */
    def add(record: RecordRequest): F[ServiceResponse[RecordResponse]]

    /** Remove operation record from storage by its uid.
      * @param uid
      *   Operation record uid.
      * @return
      *   Message describing result of the operation.
      */
    def remove(uid: Uid): F[ServiceResponse[ResponseMessage]]

    /** Remove all records of operations that was performed by specified
      * service.
      * @param serviceId
      *   Service identifier.
      * @return
      *   Message describing result of the operation.
      */
    def remove(serviceId: String): F[ServiceResponse[ResponseMessage]]
}
