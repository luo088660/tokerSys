package com.toker.sys.view.home.activity.task.bean

class PunchRecordBean {
    data class PunchRBean(
        val `data`: Data,
        val code: String,
        val desc: String
    ) {
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
        val address: Any,
        val attendanceRecordList: MutableList<AttendanceRecord>,
        val attendanceTime: Any,
        val beeId: String,
        val beeName: String,
        val beePhone: String,
        val createTime: String,
        val dailyAttendanceNum: Int,
        val id: Any,
        val latitude: Any,
        val longitude: Any,
        val pictureUrlList: Any,
        val pictureUrlString: Any
    )

    data class AttendanceRecord(
        val address: String,
        val attendanceRecordList: Any,
        val attendanceTime: Long,
        val beeId: Any,
        val beeName: Any,
        val beePhone: Any,
        val createTime: Any,
        val dailyAttendanceNum: Any,
        val id: Any,
        val latitude: Any,
        val longitude: Any,
        val pictureUrlList: MutableList<String>,
        val pictureUrlString: String
    )
}