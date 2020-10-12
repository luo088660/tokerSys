package com.toker.sys.view.home.activity.my.bean

class ViewAttenTeamBean {
    data class ViewATeamBean(
        val `data`: Data,
        val code: String,
        val desc: String
    ){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }


    data class Data(
        val page: String,
        val pageData: MutableList<PageData>,
        val pageSize: String,
        val pageTotal: String,
        val rowTotal: String
    )

    data class PageData(
        val checkInNum: Int,
        val checkOutNum: Int,
        val dueWorkDay: Int,
        val exceptionDay: Any,
        val exceptionNum: Int,
        val groupId: String,
        val id: String,
        val month: String,
        val personalLeaveDay: Any,
        val phone: String,
        val projectId: String,
        val regularNum: Int,
        val sickLeaveDay: Int,
        val userId: String,
        val userName: String,
        val workDay: Int,
        val year: String
    )
}