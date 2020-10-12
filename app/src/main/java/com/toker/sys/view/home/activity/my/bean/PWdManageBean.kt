package com.toker.sys.view.home.activity.my.bean

class PWdManageBean {
    data class PWdManBean(
        val `data`: String,
        val code: String,
        val desc: String
    ){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

}