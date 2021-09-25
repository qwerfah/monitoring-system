package com.qwerfah.gateway.json

import io.circe.literal._
import io.circe.schema.Schema

object JsonSchemas {
    val tokenSchema: Schema = Schema.load(
      json""" {
        "type": "object",
        "required": ["access", "refresh"],
        "properties": {
            "access": {
                "type": "string",
                "minLength": 1,
                "pattern": "^(?!\\s*$$).+"
            },
            "refresh": {
                "type": "string",
                "minLength": 1,
                "pattern": "^(?!\\s*$$).+"
            }
        }  
    }"""
    )
}
