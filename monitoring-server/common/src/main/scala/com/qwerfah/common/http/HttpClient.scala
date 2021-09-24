package com.qwerfah.common.http

import io.circe.Decoder

import com.qwerfah.common.services.response.ServiceResponse

trait HttpClient[F[_]] {
    def sendAndDecode[A](
      method: Method = Get,
      url: String = "/",
      contentType: Option[String] = None,
      contentString: Option[String] = None
    )(implicit
      decoder: Decoder[A]
    ): F[ServiceResponse[A]]
}
