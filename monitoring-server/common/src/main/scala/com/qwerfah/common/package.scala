package com.qwerfah

package object common {
    type Uid = java.util.UUID
    def randomUid = java.util.UUID.randomUUID
    def uidFromString = java.util.UUID.fromString(_)
}
