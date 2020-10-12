package com.toker.sys.view.home.fragment.my.item.bean

class ReadCountBean {

    data class Bean(
        val `data`: Data

    ){

        var code: String = ""
        var desc: String= ""

        constructor(`data`: Data,code: String,desc: String):this(`data`){
            this.code = code
            this.desc = desc
        }

        /**
         * 请求是否成功
         */
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val countNum: Int
    )
}