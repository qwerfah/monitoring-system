package com.qwerfah.common.repos.local

import scala.util.Try
import scala.util.Failure
import scala.util.Success
import scala.collection.mutable.Set

import slick.dbio.DBIO

import com.qwerfah.common.repos.TokenRepo
import com.qwerfah.common.exceptions._
import com.qwerfah.common.models.Token

class LocalTokenRepo extends TokenRepo[DBIO] {
    private val tokens = Set[(String, String)]()

    def get: DBIO[Seq[(String, String)]] = DBIO.successful(tokens.toSeq)

    def add(pair: (String, String)): DBIO[Unit] = tokens.add(pair) match {

        case true  => DBIO.successful(Success(()))
        case false => DBIO.successful(Failure(DuplicateTokenException))
    }

    def removeById(id: String): DBIO[Unit] = {
        for (token <- tokens.filter(pair => pair._1 == id)) tokens.remove(token)
        DBIO.successful(Success(()))
    }

    def remove(pair: (String, String)): DBIO[Try[Unit]] =
        tokens.remove(pair) match {
            case true  => DBIO.successful(Success(()))
            case false => DBIO.successful(Failure(NoTokenException))
        }

    def contains(token: String): DBIO[Boolean] =
        DBIO.successful(tokens.count(pair => pair._2 == token) > 0)
}
