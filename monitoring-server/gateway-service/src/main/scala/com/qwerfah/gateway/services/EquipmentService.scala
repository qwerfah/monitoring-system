package com.qwerfah.gateway.services

import com.qwerfah.equipment.resources._
import com.qwerfah.common.services.response._
import com.qwerfah.common.Uid

trait EquipmentService[F[_]] {
    def getAll: F[ServiceResponse[Seq[ModelResponse]]]
}