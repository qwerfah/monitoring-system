package com.qwerfah.generator.json

import io.circe.literal._
import io.circe.schema.Schema

/** Provide json schemas for different model resources. */
object JsonSchemas {
    val paramValueRequestSchema: Schema = Schema.load(
      json"""
            {
                "type": "object",
                "required": ["paramUid", "instanceUid", "value"],
                "properties": {
                    "paramUid": {
                        "type": "string",
                        "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
                    },
                    "instanceUid": {
                        "type": "string",
                        "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
                    },
                    "value": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    }
                }
            }
            """
    )

    val paramValueResponseSchema: Schema = Schema.load(
      json"""
            {
                "type": "object",
                "required": ["uid", "paramUid", "instanceUid", "value", "time"],
                "properties": {
                    "uid": {
                        "type": "string",
                        "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
                    },
                    "paramUid": {
                        "type": "string",
                        "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
                    },
                    "instanceUid": {
                        "type": "string",
                        "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
                    },
                    "value": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "time": {
                        "type": "string",
                        "format": "date-time"
                    }
                }
            }
            """
    )
}
