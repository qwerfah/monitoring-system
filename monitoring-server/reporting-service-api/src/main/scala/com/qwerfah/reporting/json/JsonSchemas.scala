package com.qwerfah.reporting.json

import io.circe.literal._
import io.circe.schema.Schema

/** Provide json schemas for different model resources. */
object JsonSchemas {
    val recordRequestSchema: Schema = Schema.load(
      json"""
            {
                "type": "object",
                "required": ["serviceId", "route", "method", "status", "elapsed", "time"],
                "properties": {
                    "userName": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "serviceId": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 30,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "route": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 200,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "method": {
                        "type": "string",
                        "enum":["Get", "Post", "Patch", "Delete"]
                    },
                    "status": {
                        "type": "integer",
                        "minimum": 100,
                        "maximum": 599
                    },
                    "elapsed": {
                        "type": "integer",
                        "minimum": 0
                    },
                    "time": {
                        "type": "string",
                        "format": "date-time"
                    }
                }
            }
            """
    )

    val recordResponseSchema: Schema = Schema.load(
      json"""
            {
                "type": "object",
                "required": ["uid", "serviceId", "route", "method", "status", "elapsed", "time"],
                "properties": {
                    "uid": {
                        "type": "string",
                        "pattern": "[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}"
                    },
                    "userName": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 100,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "serviceId": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 30,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "route": {
                        "type": "string",
                        "minLength": 1,
                        "maxLength": 200,
                        "pattern": "^(?!\\s*$$).+"
                    },
                    "method": {
                        "type": "string",
                        "enum":["Get", "Post", "Patch", "Delete"]
                    },
                    "status": {
                        "type": "integer",
                        "minimum": 100,
                        "maximum": 599
                    },
                    "elapsed": {
                        "type": "integer",
                        "minimum": 0
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
