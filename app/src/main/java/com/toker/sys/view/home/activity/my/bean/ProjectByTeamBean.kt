package com.toker.sys.view.home.activity.my.bean

class ProjectByTeamBean {
    data class ProjectTeamBean(
        val `data`: Data,
        val code: String,
        val desc: String
    ){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val exceptionNum: String,
        val regularNum: String,
        val totalCheckInNum: String,
        val totalCheckOutNum: String,
        val totalPeople: String
    )
}