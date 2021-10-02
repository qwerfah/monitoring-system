package com.qwerfah.monitoring.repos

import com.qwerfah.monitoring.models.Monitor
import com.qwerfah.common.Uid

trait MonitorRepo[DB[_]] {
    def get: DB[Seq[Monitor]]
    def get(id: Int): DB[Option[Monitor]]
    def get(uid: Uid): DB[Option[Monitor]]
    def getByInstanceUid(instanceUid: Uid): DB[Seq[Monitor]]
    def getInstances: DB[Seq[Uid]]

    def add(monitor: Monitor): DB[Monitor]
    def update(monitor: Monitor): DB[Int]
    def removeById(id: Int): DB[Int]
    def removeByUid(uid: Uid): DB[Int]
}
