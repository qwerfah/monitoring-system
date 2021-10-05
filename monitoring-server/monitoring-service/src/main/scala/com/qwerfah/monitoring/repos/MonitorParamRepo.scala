package com.qwerfah.monitoring.repos

import com.qwerfah.monitoring.models.MonitorParam
import com.qwerfah.common.Uid

trait MonitorParamRepo[DB[_]] {
    def get: DB[Seq[MonitorParam]]
    def getByMonitorUid(monitorUid: Uid): DB[Seq[MonitorParam]]

    def add(monitor: MonitorParam): DB[Int]

    def removeByParamUid(paramUid: Uid): DB[Int]
    def removeByMonitorUid(monitorUid: Uid): DB[Int]
    def removeByUid(monitorUid: Uid, paramUid: Uid): DB[Int]

    def restoreByParamUid(paramUid: Uid): DB[Int]
    def restoreByMonitorUid(monitorUid: Uid): DB[Int]
    def restoreByUid(monitorUid: Uid, paramUid: Uid): DB[Int]
}
