package com.qwerfah.monitoring

import com.qwerfah.equipment.resources.ParamResponse

import com.qwerfah.monitoring.resources._
import com.qwerfah.monitoring.models._

import com.qwerfah.common.randomUid

object Mappings {
    implicit class AddRequestToMonitorMapping(request: AddMonitorRequest) {
        def asMonitor =
            Monitor(
              None,
              randomUid,
              request.instanceUid,
              request.name,
              request.description,
              false
            )
    }

    implicit class UpdateRequestToMonitorMapping(
      request: UpdateMonitorRequest
    ) {
        def asMonitor =
            Monitor(
              None,
              randomUid,
              randomUid,
              request.name,
              request.description,
              false
            )
    }

    implicit class MonitorToResponseMapping(monitor: Monitor) {
        def asResponse = MonitorResponse(
          monitor.uid,
          monitor.instanceUid,
          monitor.name,
          monitor.description
        )
    }

    implicit class MonitorSeqToResponseSeqMapping(monitors: Seq[Monitor]) {
        def asResponse =
            for { monitor <- monitors } yield monitor.asResponse
    }

    implicit class RequestToMonitorParamMapping(request: MonitorParamRequest) {
        def asParam =
            MonitorParam(randomUid, request.paramUid, false)
    }

    implicit class MonitorParamToResponseMapping(mp: MonitorParam) {

        def asResponse = MonitorParamResponse(
          mp.monitorUid,
          mp.paramUid,
          None,
          None,
          None
        )

        def asResponse(param: ParamResponse) = MonitorParamResponse(
          mp.monitorUid,
          mp.paramUid,
          Some(param.modelUid),
          Some(param.name),
          param.measurmentUnits
        )
    }
}
