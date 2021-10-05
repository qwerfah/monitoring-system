package com.qwerfah.reporting

import com.qwerfah.reporting.models._

import com.qwerfah.common.randomUid
import com.qwerfah.common.resources.{RecordRequest, RecordResponse}

object Mappings {
    implicit class RequestToRecordMapping(request: RecordRequest) {
        def asRecord =
            OperationRecord(
              None,
              randomUid,
              request.userName,
              request.serviceId,
              request.route,
              request.method,
              request.status,
              request.elapsed,
              request.time,
              false
            )
    }

    implicit class MonitorToResponseMapping(record: OperationRecord) {
        def asResponse = RecordResponse(
          record.uid,
          record.userName,
          record.serviceId,
          record.route,
          record.method,
          record.status,
          record.elapsed,
          record.time
        )
    }

    implicit class RecordSeqToResponseSeqMapping(
      records: Seq[OperationRecord]
    ) {
        def asResponse = for { record <- records } yield record.asResponse
    }
}
