package com.toker.sys.view.home.fragment.my.item.bean

class MsgBean {
    data class Bena(
        val `data`: String,
        val code: String,
        val desc: String
    ){
        /**
         * 请求是否成功
         */
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }
}