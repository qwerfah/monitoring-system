package com.qwerfah.documentation.models

import com.qwerfah.common.Uid

/** Documentation repository file.
  * @param id
  *   Internal file indentifier.
  * @param uid
  *   External file identifier.
  * @param filename
  *   File name.
  * @param content
  *   Binary file content.
  */
final case class File(
  id: Option[Int],
  uid: Uid,
  modelUid: Uid,
  filename: String,
  contentType: String,
  content: Array[Byte],
  isDeleted: Boolean
)
