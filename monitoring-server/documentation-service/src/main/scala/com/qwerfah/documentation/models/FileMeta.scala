package com.qwerfah.documentation.models

import com.qwerfah.common.Uid

/** Documentation repository file metadata.
  * @param id
  *   Internal file indentifier.
  * @param uid
  *   External file identifier.
  * @param filename
  *   File name.
  * @param content
  *   Binary file content.
  */
final case class FileMeta(
  id: Option[Int],
  uid: Uid,
  modelUid: Uid,
  filename: String,
  contentType: String
)
