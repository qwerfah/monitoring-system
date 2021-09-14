package com.qwerfah.equipment.repos

/** Database manager trait. Services are suppose to use dbManager to explicitly
  * separate repository concept from db mechanism. Also provides ability to
  * perform sequince of actions transactionaly.
  */
trait DbManager[F[_], DB[_]] {

    /** Execute db action and return result of effect type.
      * @param action
      *   Db action of higher-kinded type.
      * @return
      *   Result of effect type.
      */
    def execute[A](action: DB[A]): F[A]

    /** Execute db action transactionally and return result of effect type.
      * @param action
      *   Db action of higher-kinded type.
      * @return
      *   Result of effect type.
      */
    def executeTransactionally[A](action: DB[A]): F[A]

    /** Transform sequence of actions into single db action that can be execute
      * transactionally.
      * @param action
      *   Sequence of db actions of higher-kinded type.
      * @return
      *   New db action of higher-kinded type.
      */
    def sequence[A](action: Seq[DB[A]]): DB[Seq[A]]
}
