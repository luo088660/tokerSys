package com.toker.sys.view.home.activity.task.bean

class TransactBean {
    data class TransacTBean(
        val `data`: List<Data>,
        val code: String,
        val desc: String
    ) {
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val attendanceId: String,
        val id: String,
        val locateAddress: String,
        val locateLatitude: Double,
        val locateLongitude: Double,
        val locateTime: Long,
        val order: Int,
        val remark: String,
        val status: String,
        val taskFlag: String,
        val userId: String,
        val address: String,
        val content: String,
        val createTime: Long,
        val creator: String,
        val creatorId: String,
        val delFlag: String,
        val efficientRange: Int,
        val endDate: Long,
        val endTime: String,
        val latitude: Double,
        val level: String,
        val longitude: Double,
        val objectList: List<Object>,
        val projectId: String,
        val projectName: String,
        val startDate: Long,
        val startTime: String,
        val tableTag: String,
        val taskName: String,
        val updateTime: Long,
        val updator: String,
        val updatorId: String
    )
    data class Object(
        val id: String,
        val objectId: String,
        val objectName: String,
        val tableTag: String,
        val taskId: String
    )
}
