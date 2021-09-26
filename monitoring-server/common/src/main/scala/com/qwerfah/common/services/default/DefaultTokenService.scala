package com.qwerfah.common.services.default

import scala.util.{Try, Success, Failure}

import java.time.Instant
import java.security.MessageDigest

import cats.Monad
import cats.implicits._

import pdi.jwt.{JwtCirce, JwtAlgorithm, JwtClaim}

import com.qwerfah.common.services._
import com.qwerfah.common.services.response._
import com.qwerfah.common.repos._
import com.qwerfah.common.models.Token
import com.qwerfah.common.db.DbManager
import com.qwerfah.common.randomUid
import com.qwerfah.common.exceptions._
import com.qwerfah.common.resources.Credentials
import com.qwerfah.common.util.Conversions._
import com.qwerfah.common.{Uid, uidFromString}

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
  userRepo: UserRepo[DB],
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

    /** Generate access and refresh tokens for given subject identifier.
      * @param id
      *   Subject identifier.
      * @return
      *   New access-refresh token pair for given subject.
      */
    private def generate(uid: Uid): F[ServiceResponse[Token]] = {
        val token = generateToken(uid.toString)
        for {
            _ <- dbManager.execute(tokenRepo.removeById(uid))
            _ <- dbManager.execute(tokenRepo.add(uid -> token.access))
            _ <- dbManager.execute(tokenRepo.add(uid -> token.refresh))
        } yield token.as200
    }

    override def verify(token: String): F[ServiceResponse[Uid]] = {
        dbManager.execute(tokenRepo.contains(token)) flatMap {
            case true => {
                decodeToken(token) match {
                    case Success(value) =>
                        Monad[F].pure(uidFromString(value.subject.get).as200)
                    case Failure(_) => {
                        dbManager.execute(tokenRepo.removeByToken(token)) map {
                            case true  => ExpiredToken.as401
                            case false => NoExpiredToken.as401
                        }
                    }
                }
            }
            case false => Monad[F].pure(InvalidToken.as401)
        }
    }

    override def login(
      credentials: Credentials
    ): F[ServiceResponse[Token]] = {
        val passwordHash = MessageDigest
            .getInstance("MD5")
            .digest(credentials.password.getBytes("UTF-8"))

        dbManager.execute(userRepo.getByLogin(credentials.login)) flatMap {
            case Some(user) if user.password sameElements passwordHash =>
                generate(user.uid)
            case _ => Monad[F].pure(InvalidCredentials.as404)
        }
    }

    override def refresh(token: String): F[ServiceResponse[Token]] = {
        verify(token) flatMap {
            case ObjectResponse(uid) => generate(uid)
            case e: ErrorResponse    => Monad[F].pure(e)
        }
    }
}
