package com.qwerfah.monitoring

import com.qwerfah.monitoring.resources._
import com.qwerfah.monitoring.models._
import com.qwerfah.common.randomUid

object Mappings {
    implicit def requestToMonitor(request: MonitorRequest) =
        Monitor(
          None,
          randomUid,
          request.instanceUid,
          request.name,
          request.description
        )

    implicit def monitorToResponse(monitor: Monitor) =
        MonitorResponse(
          monitor.uid,
          monitor.instanceUid,
          monitor.name,
          monitor.description
        )

    implicit def monitorsToResponses(monitors: Seq[Monitor]) =
        for { monitor <- monitors } yield MonitorResponse(
          monitor.uid,
          monitor.instanceUid,
          monitor.name,
          monitor.description
        )

    implicit def requestToMonitorParam(request: MonitorParamRequest) =
        MonitorParam(randomUid, request.paramUid)
}
