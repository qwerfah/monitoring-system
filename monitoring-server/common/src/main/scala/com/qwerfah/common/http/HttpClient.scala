package com.qwerfah.common.http

import com.twitter.finagle.http.{Request, Response}

import io.circe.{Decoder, Encoder}

import com.qwerfah.common.services.response.ServiceResponse

trait HttpClient[F[_]] {

    /** Send raw finagle [[Request]] and return raw [[Response]] instance.
      * @param request
      *   Placeholder with raw finagle [[Request]] instance.
      */
    def send(request: Request): F[Response]

    /** Send json request with specified parameters and return raw finalge
      * [[Response]].
      * @param method
      *   Http method of request.
      * @param url
      *   Request URL.
      * @param content
      *   Request content in json format.
      * @param token
      *   Access token if custom authorization requires.
      * @return
      *   Placeholder that produces raw finalge [[Response]] instance.
      */
    def send(
      method: HttpMethod = HttpMethod.Get,
      url: String = "/",
      content: Option[String] = None,
      token: Option[String] = None
    ): F[Response]

    /** Send json request, decode json response adn wrap it with
      * [[ServiceResponse]] instance.
      * @param method
      *   Http method of request.
      * @param url
      *   Request URL.
      * @param content
      *   Request content in json format.
      * @param token
      *   Access token if custom authorization requires.
      * @return
      *   Placeholder that produces [[ServiceResponse]] instance that
      *   incapsulates result of the request.
      */
    def sendAndDecode[A](
      method: HttpMethod = HttpMethod.Get,
      url: String = "/",
      content: Option[String] = None,
      token: Option[String] = None
    )(implicit
      decoder: Decoder[A]
    ): F[ServiceResponse[A]]
}
