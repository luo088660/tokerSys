package com.toker.sys.utils.network.bean

class PhoneIdBean {
    data class PhoneBean(
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