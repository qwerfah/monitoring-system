package com.qwerfah.common.services

import scala.util.Try

import com.qwerfah.common.models.Token
import com.qwerfah.common.services.response._

trait TokenService[F[_]] {
    def validate(token: String): F[ServiceResponse[String]]
    def generate(id: String): F[ServiceResponse[Token]]
}
