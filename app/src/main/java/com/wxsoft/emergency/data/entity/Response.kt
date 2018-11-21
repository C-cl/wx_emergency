package com.wxsoft.emergency.data.entity

data class Response<T>(var success: Boolean) {

    val msg:String=""
    var result:T?=null
}