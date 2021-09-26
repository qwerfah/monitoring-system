package com.qwerfah.common.http

import io.circe.{Decoder, Encoder}

import com.qwerfah.common.services.response.ServiceResponse

trait HttpClient[F[_]] {
    def sendAndDecode[A](
      method: Method = Get,
      url: String = "/",
      content: Option[String] = None,
      token: Option[String] = None
    )(implicit
      decoder: Decoder[A]
    ): F[ServiceResponse[A]]
}
