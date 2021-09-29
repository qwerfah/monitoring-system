package com.qwerfah.common.repos.local

import scala.util.Try
import scala.util.Failure
import scala.util.Success
import scala.collection.mutable.Set

import slick.dbio.DBIO

import com.qwerfah.common.repos.TokenRepo
import com.qwerfah.common.exceptions._
import com.qwerfah.common.models.Token
import com.qwerfah.common.Uid

class LocalTokenRepo extends TokenRepo[DBIO] {
    private val tokens = Set[(Uid, String)]()

    def get: DBIO[Seq[(Uid, String)]] = DBIO.successful(tokens.toSeq)

    def add(pair: (Uid, String)): DBIO[Boolean] =
        DBIO.successful(tokens.add(pair))

    def removeByUid(uid: Uid): DBIO[Unit] = {
        for (token <- tokens.filter(pair => pair._1 == uid))
            tokens.remove(token)
        DBIO.successful(Success(()))
    }

    def removeByValue(pair: (Uid, String)): DBIO[Boolean] =
        DBIO.successful(tokens.remove(pair))

    def removeByToken(token: String): DBIO[Boolean] = {
        tokens.find(pair => pair._2 == token) match {
            case Some(value) => {
                tokens.remove(value)
                DBIO.successful(true)
            }
            case None => DBIO.successful(false)
        }
    }

    def contains(token: String): DBIO[Boolean] =
        DBIO.successful(tokens.count(pair => pair._2 == token) > 0)
}
