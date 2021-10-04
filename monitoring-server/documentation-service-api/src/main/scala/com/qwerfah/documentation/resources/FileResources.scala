package com.qwerfah.documentation.resources

import com.qwerfah.common.Uid

final case class FileRequest(
  modelUid: Uid,
  filename: String,
  contentType: String,
  content: Array[Byte]
)

final case class FileMetaResponse(
  uid: Uid,
  modelUid: Uid,
  filename: String,
  contentType: String
)

final case class FileResponse(
  filename: String,
  contentType: String,
  content: Array[Byte]
)
