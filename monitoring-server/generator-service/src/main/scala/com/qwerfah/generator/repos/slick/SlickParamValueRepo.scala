package com.qwerfah.generator.repos.slick

import slick.dbio._
import slick.jdbc.JdbcProfile

import com.qwerfah.generator.repos.ParamValueRepo
import com.qwerfah.generator.models.GeneratorContext
import com.qwerfah.generator.models.ParamValue
import com.qwerfah.common.Uid
import com.qwerfah.generator.models.ParamValue
import scala.annotation.meta.param

class SlickParamValueRepo(implicit val context: GeneratorContext)
  extends ParamValueRepo[DBIO] {
    import context.profile.api._

    override def get: DBIO[Seq[ParamValue]] = context.paramValues.result

    override def get(uid: Uid): DBIO[Option[ParamValue]] =
        context.paramValues.filter(_.uid === uid).result.headOption

    override def get(
      paramUid: Uid,
      instanceUid: Uid
    ): DBIO[Seq[ParamValue]] =
        context.paramValues
            .filter(v =>
                v.paramUid === paramUid && v.instanceUid === instanceUid
            )
            .result

    override def getLast(
      paramUid: Uid,
      instanceUid: Uid
    ): DBIO[Option[ParamValue]] = context.paramValues
        .filter(v => v.paramUid === paramUid && v.instanceUid === instanceUid)
        .sortBy(_.time.desc)
        .result
        .headOption

    override def add(value: ParamValue): DBIO[ParamValue] =
        (context.paramValues returning context.paramValues.map(_.id) into (
          (pv, id) => pv.copy(id = Some(id))
        )) += value

    override def remove(uid: Uid): DBIO[Int] =
        context.paramValues.filter(_.uid === uid).delete
    override def removeByParamUid(paramUid: Uid): DBIO[Int] =
        context.paramValues.filter(_.paramUid === paramUid).delete
    override def removeByInstanceUid(instanceUid: Uid): DBIO[Int] =
        context.paramValues.filter(_.instanceUid === instanceUid).delete

}
