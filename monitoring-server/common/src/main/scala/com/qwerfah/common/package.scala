package com.qwerfah

import java.security.MessageDigest

package object common {
    type Uid = java.util.UUID
    def randomUid = java.util.UUID.randomUUID
    def uidFromString = java.util.UUID.fromString(_)
    def hashString(str: String) =
        MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"))
}
