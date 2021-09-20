package com.qwerfah.common.services.default

import scala.util.{Try, Success, Failure}

import java.time.Instant

import cats.Monad
import cats.implicits._

import pdi.jwt.{JwtCirce, JwtAlgorithm, JwtClaim}

import com.qwerfah.common.services._
import com.qwerfah.common.repos.TokenRepo
import com.qwerfah.common.models.Token
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.randomUid
import com.qwerfah.common.exceptions._

object JwtOptions {
    val key = "secretKey"
    val algorithm = JwtAlgorithm.HS256
    def accessExpiration = Some(
      Instant.now.plusSeconds(100).getEpochSecond
    )
    def refreshExpiration = Some(
      Instant.now.plusSeconds(200).getEpochSecond
    )
    val issuedAt = Some(Instant.now.getEpochSecond)
}

class DefaultTokenService[F[_]: Monad, DB[_]: Monad](implicit
  tokenRepo: TokenRepo[DB],
  dbManager: DbManager[F, DB]
) extends TokenService[F] {

    /** Generate jwt token for specified subject identifier with given
      * expiration time.
      * @param id
      *   Subject identifier.
      * @param expiration
      *   Expiration time.
      * @return
      *   Jwt token.
      */
    private def generateToken(id: String, expiration: Some[Long]): String = {
        val claim = JwtClaim(
          subject = Some(id),
          expiration = expiration,
          issuedAt = JwtOptions.issuedAt,
          jwtId = Some(randomUid.toString)
        )

        JwtCirce.encode(claim, JwtOptions.key, JwtOptions.algorithm)
    }

    /** Generate Token instance contains acess and refresh token for subject
      * with given identifier.
      * @param id
      *   Subject identifier.
      * @return
      *   Token instance.
      */
    private def generateToken(id: String): Token = Token(
      generateToken(id, JwtOptions.accessExpiration),
      generateToken(id, JwtOptions.refreshExpiration)
    )

    /** Decode string token representation into JwtClaim.
      * @param token
      *   String token representation.
      * @return
      *   JwtClaim instance if token is valid, otherwise error.
      */
    private def decodeToken(token: String) = JwtCirce.decode(
      token,
      JwtOptions.key,
      Seq(JwtOptions.algorithm)
    )

    def validate(token: String): F[ServiceResponse[String]] = {
        dbManager.execute(tokenRepo.contains(token)) map {
            case true => {
                decodeToken(token) match {
                    case Success(value) => ObjectResponse(value.subject.get)
                    case Failure(_) => {
                        dbManager.execute(tokenRepo.removeByToken(token))
                        ErrorResponse(InvalidTokenException)
                    }
                }
            }
            case false => ErrorResponse(InvalidTokenException)
        }
    }

    def generate(id: String): F[ServiceResponse[Token]] = {
        val token = generateToken(id)
        for {
            _ <- dbManager.execute(tokenRepo.removeById(id))
            _ <- dbManager.execute(tokenRepo.add(id -> token.access))
            _ <- dbManager.execute(tokenRepo.add(id -> token.refresh))
        } yield ObjectResponse(token)
    }

    def removeExpired(): F[ServiceResponse[Unit]] = {
        dbManager.execute(tokenRepo.get) flatMap { tokens =>
            {
                val expired =
                    tokens.filter(token => decodeToken(token._2).isSuccess)
                val seq = dbManager.sequence(expired map { pair =>
                    tokenRepo.removeByValue(pair)
                })
                dbManager.execute(seq) map { result =>
                    result.toList.sequence_ match {
                        case Success(())    => ObjectResponse(())
                        case Failure(error) => ErrorResponse(error)
                    }
                }
            }
        }
    }
}