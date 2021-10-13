package com.qwerfah.reporting.services

import com.qwerfah.reporting.resources.{ModelStat, ServiceStat}

import com.qwerfah.common.services.response.ServiceResponse

trait StatService[F[_]] {
    def getModelStats(): F[ServiceResponse[Seq[ModelStat]]]

    def getServiceStats(): F[ServiceResponse[Seq[ServiceStat]]]
}
