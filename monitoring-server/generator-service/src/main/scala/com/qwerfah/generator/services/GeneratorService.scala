package com.qwerfah.generator.services

import com.qwerfah.common.services.response.{ServiceResponse, ResponseMessage}

/** Generator service trait. Performs random param value generating for all
  * equipment instances that are monitored from monitoring service.
  */
trait GeneratorService[F[_]] {
    def generate: F[ServiceResponse[ResponseMessage]]
}
