package com.toker.sys.view.home.activity.task.bean

class TransactABean {
    data class TransactBBean(
        val `data`: MutableList<Data>,
        val code: String,
        val desc: String
    ){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val attendanceId: String,
        val id: String,
        val locateAddress: String,
        val locateLatitude: String,
        val locateLongitude: String,
        val locateTime: String,
        val order: Int,
        val remark: String,
        val status: String,
        val taskFlag: String,
        val userId: String
    )
}