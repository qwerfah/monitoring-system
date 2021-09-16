package com.qwerfah.common

object ErrorMessages {
    sealed trait ErrorMessage {
        def getMessage: String
    }

    case object ValidationError extends ErrorMessage {
        override def getMessage = "One or more validation errors occured."
    }

    case object NoUid extends ErrorMessage {
        override def getMessage = "Uuid is not presented in request body."
    }

    case object NoModelUid extends ErrorMessage {
        override def getMessage = "Model uuid is not presented in request body."
    }

    case object NoName extends ErrorMessage {
        override def getMessage = "Name is not presented in request body."
    }

    case object NoEquipmentStatus extends ErrorMessage {
        override def getMessage =
            "Equipment status is not presented in request body."
    }

    case object InvalidUid extends ErrorMessage {
        override def getMessage = "Invalid uuid format."
    }

    case object InvalidModelUid extends ErrorMessage {
        override def getMessage = "Invalid model uuid format."
    }

    case object NameIsBlankOrEmpty extends ErrorMessage {
        override def getMessage = "Name is blank or empty."
    }

    case object DescriptionIsBlankOrEmpty extends ErrorMessage {
        override def getMessage = "Description if blank or empty."
    }

    case object InvalidEquipmentStatus extends ErrorMessage {
        override def getMessage = "Invalid equipment instance status."
    }
}
