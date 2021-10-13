package com.qwerfah.reporting.resources

import com.qwerfah.common.Uid

/** Equipment model statistics.
  * @param modelUid
  *   Model identifier.
  * @param modelName
  *   Model name.
  * @param paramCount
  *   Total number of model params.
  * @param instanceCount
  *   Total number of model deployed instances.
  * @param activeInstanceCount
  *   Number of active model instances.
  * @param inactiveInstanceCount
  *   Number of inactive model instances.
  * @param decommissionedInstanceCount
  *   Number of decommissioned model instances.
  * @param monitorCount
  *   Totla number of monitors for all model instances.
  */
final case class ModelStat(
  modelUid: Uid,
  modelName: String,
  paramCount: Int,
  instanceCount: Int,
  activeInstanceCount: Int,
  inactiveInstanceCount: Int,
  decommissionedInstanceCount: Int,
  monitorCount: Int
)
