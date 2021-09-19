package com.qwerfah.common.services

import scala.util.Try

import com.qwerfah.common.models.Token

trait TokenService[F[_]] {
    def validate(token: String): F[ServiceResponse[String]]
    def generate(id: String): F[ServiceResponse[Token]]
    def removeExpired(): F[ServiceResponse[Unit]]
}
