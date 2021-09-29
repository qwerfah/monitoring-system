package com.qwerfah.common.repos

import scala.util.Try

import com.qwerfah.common.Uid

/** Provide jwt token repository finctionality. */
trait TokenRepo[DB[_]] {

    /** Get all tokens in repository.
      * @return
      *   Sequence of pairs (subject_id, token).
      */
    def get: DB[Seq[(Uid, String)]]

    /** Add new token for spicified subject.
      * @param pair
      *   Pair of subject id and token.
      * @return
      *   Action.
      */
    def add(pair: (Uid, String)): DB[Boolean]

    /** Remove all tokens for specified subject.
      * @param id
      *   Subject id.
      * @return
      *   Action.
      */
    def removeByUid(uid: Uid): DB[Unit]

    /** Remove token by value.
      * @param pair
      *   Pair of subject id and token.
      * @return
      *   Success if removed, otherwise Failure.
      */
    def removeByValue(pair: (Uid, String)): DB[Boolean]

    def removeByToken(token: String): DB[Boolean]

    /** Check if token is presented in repository.
      * @param token
      *   Token.
      * @return
      *   True if token is in repository, otherwise false.
      */
    def contains(token: String): DB[Boolean]
}
