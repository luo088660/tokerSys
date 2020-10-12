package com.toker.sys.utils.network.bean

abstract class BaseArrayBean(

    //状态响应码
    val code: String,
    //响应描述
    val desc: String

) {
    /**
     * 请求是否成功
     */
    fun isSuccess(): Boolean {
        return code == "1"
    }
    var `data`: Any? = null
    constructor(`data`: Any,code: String,desc: String):this(code,desc){
        this.data = data
    }
}