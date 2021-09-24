package com.qwerfah.common.http

sealed trait Method

case object Get extends Method
case object Post extends Method
case object Patch extends Method
case object Delete extends Method
