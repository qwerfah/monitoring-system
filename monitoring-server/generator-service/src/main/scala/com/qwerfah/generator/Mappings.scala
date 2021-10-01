package com.qwerfah.generator

import java.time.LocalDateTime

import com.qwerfah.generator.models._
import com.qwerfah.generator.resources._

import com.qwerfah.common.randomUid

object Mappings {
    implicit class RequestToParamValueMapping(request: ParamValueRequest) {
        def asValue = ParamValue(
          None,
          randomUid,
          request.paramUid,
          request.instanceUid,
          request.value,
          LocalDateTime.now
        )
    }

    implicit class ParamValueToResponseMapping(value: ParamValue) {
        def asResponse = ParamValueResponse(
          value.uid,
          value.paramUid,
          value.instanceUid,
          value.value,
          value.time
        )
    }

    implicit class ParamValueSeqToResponseSeqMapping(values: Seq[ParamValue]) {
        def asResponse = for { value <- values } yield ParamValueResponse(
          value.uid,
          value.paramUid,
          value.instanceUid,
          value.value,
          value.time
        )
    }
}
