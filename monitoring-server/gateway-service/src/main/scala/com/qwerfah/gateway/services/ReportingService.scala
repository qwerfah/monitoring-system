package com.qwerfah.gateway.services

import com.twitter.finagle.http.Response

trait ReportingService[F[_]] {
    def getModelStats: F[Response]

    def getServiceStats: F[Response]
}
