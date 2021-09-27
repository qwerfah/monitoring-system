package com.qwerfah.reporting

import com.qwerfah.reporting.resources._
import com.qwerfah.reporting.models._
import com.qwerfah.common.randomUid

object Mappings {
    implicit class RequestToRecordMapping(request: RecordRequest) {
        def asRecord =
            OperationRecord(
              None,
              randomUid,
              request.serviceId,
              request.route,
              request.method,
              request.status,
              request.time
            )
    }

    implicit class MonitorToResponseMapping(record: OperationRecord) {
        def asResponse = RecordResponse(
          record.uid,
          record.serviceId,
          record.route,
          record.method,
          record.status,
          record.time
        )
    }

    implicit class RecordSeqToResponseSeqMapping(
      records: Seq[OperationRecord]
    ) {
        def asResponse =
            for { record <- records } yield RecordResponse(
              record.uid,
              record.serviceId,
              record.route,
              record.method,
              record.status,
              record.time
            )
    }
}
