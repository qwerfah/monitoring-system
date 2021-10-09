package com.qwerfah.equipment

import com.qwerfah.equipment.models._
import com.qwerfah.equipment.resources._

import com.qwerfah.monitoring.resources.MonitorResponse

import com.qwerfah.common.{Uid, randomUid}

/** Provides implicit mappings for different resources. */
object Mappings {

    /** Equipment model request to equipment model mapping.
      * @param request
      *   Equipment model request.
      */
    implicit class RequestToModelMapping(request: ModelRequest) {
        def asModel = EquipmentModel(
          None,
          randomUid,
          request.name,
          request.description,
          false
        )
    }

    /** Equipment model to equipment model response mapping.
      * @param model
      *   Equipment model.
      */
    implicit class ModelToResponseMapping(model: EquipmentModel) {
        def asResponse =
            ModelResponse(model.uid, model.name, model.description)
    }

    /** Equipment model sequence to equipment model resource sequence mapping.
      * @param models
      *   Equipment model sequence.
      */
    implicit class ModelSeqToResponseSeq(models: Seq[EquipmentModel]) {
        def asResponse = for { model <- models } yield model.asResponse
    }

    /** Equipment instance adding request to equipment instance mapping.
      * @param request
      *   Equipment instance adding request.
      */
    implicit class AddRequestToInstanceMapping(request: AddInstanceRequest) {
        def asInstance(modelUid: Uid) = EquipmentInstance(
          None,
          randomUid,
          modelUid,
          request.name,
          request.description,
          request.status,
          false
        )
    }

    /** Equipment instance updating request to equipment instance mapping.
      * @param request
      *   Equipment instance updating request.
      */
    implicit class UpdateRequestToInstanceMapping(
      request: UpdateInstanceRequest
    ) {
        def asInstance = EquipmentInstance(
          None,
          randomUid,
          randomUid,
          request.name,
          request.description,
          request.status,
          false
        )
    }

    /** Equipment instance with model name to equipment instance response
      * mapping.
      * @param instance
      *   Equipment instance.
      */
    implicit class InstanceToResponseMapping(
      instance: (EquipmentInstance, String)
    ) {
        def asResponse = InstanceResponse(
          instance._1.uid,
          instance._1.modelUid,
          instance._1.name,
          instance._2,
          instance._1.description,
          instance._1.status
        )
    }

    /** Equipment instance with model name sequence to equipment instance
      * response sequence mapping.
      * @param instances
      *   Equipment instance sequence.
      * @return
      *   Equipment instance response sequence.
      */
    implicit class InstanceSeqToResponseSeqMapping(
      instances: Seq[(EquipmentInstance, String)]
    ) {
        def asResponse = for { instance <- instances } yield instance.asResponse
    }

    implicit class AddRequestToParamMapping(request: AddParamRequest) {
        def asParam(modelUid: Uid) = Param(
          None,
          randomUid,
          modelUid,
          request.name,
          request.measurmentUnits,
          false
        )
    }

    implicit class UpdateRequestToParamMapping(request: UpdateParamRequest) {
        def asParam = Param(
          None,
          randomUid,
          randomUid,
          request.name,
          request.measurmentUnits,
          false
        )
    }

    implicit class ParamToResponseMapping(param: Param) {
        def asResponse = ParamResponse(
          param.uid,
          param.modelUid,
          param.name,
          param.measurmentUnits
        )
    }

    implicit class ParamSeqToResponseSeqMapping(params: Seq[Param]) {
        def asResponse = for { param <- params } yield param.asResponse
    }

    implicit class MonitorToIstanceMonitorResponseMapping(
      monitor: MonitorResponse
    ) {
        def asResponse(model: EquipmentModel, instance: EquipmentInstance) =
            InstanceMonitorResponse(
              monitor.uid,
              model.uid,
              instance.uid,
              monitor.name,
              model.name,
              instance.name,
              monitor.description
            )
    }

    implicit class MonitorToIstanceMonitorResponseSeqMapping(
      monitors: Seq[MonitorResponse]
    ) {
        def asResponse(model: EquipmentModel, instance: EquipmentInstance) =
            for { monitor <- monitors } yield monitor.asResponse(
              model,
              instance
            )
    }
}
