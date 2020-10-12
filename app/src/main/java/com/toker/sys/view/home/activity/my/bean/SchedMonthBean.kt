package com.toker.sys.view.home.activity.my.bean

class SchedMonthBean {
    data class Bean(
        val `data`: Data,
        val code: String,
        val desc: String
    ){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val personalLeaveNum: Int,
        val restedNum: Int,
        val sickLeaveNum: Int,
        val workNum: Int
    )
}