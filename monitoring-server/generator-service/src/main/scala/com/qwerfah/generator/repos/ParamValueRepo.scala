package com.qwerfah.generator.repos

import com.qwerfah.generator.models.ParamValue

import com.qwerfah.common.Uid

trait ParamValueRepo[DB[_]] {
    def get: DB[Seq[ParamValue]]
    def get(uid: Uid): DB[Option[ParamValue]]
    def get(
      paramUid: Option[Uid],
      instanceUid: Option[Uid]
    ): DB[Seq[ParamValue]]
    def getLast(paramUid: Uid, instanceUid: Uid): DB[Option[ParamValue]]

    def add(value: ParamValue): DB[ParamValue]

    def remove(uid: Uid): DB[Int]
    def removeByParamUid(paramUid: Uid): DB[Int]
    def removeByInstanceUid(instanceUid: Uid): DB[Int]
}
