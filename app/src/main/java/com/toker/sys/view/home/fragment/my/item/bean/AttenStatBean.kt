package com.toker.sys.view.home.fragment.my.item.bean


class AttenStatBean {
    data class AListBean(
        val `data`: MutableList<Data>,
        val code: String,
        val desc: String
    ) {
        /**
         * 请求是否成功
         */
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }


    data class Data(
        val address: String,
        val checkInAddress: String,
        val checkInDeclare: String,
        val checkInLatitude: String,
        val checkInLongitude: String,
        val checkInRemark: String,
        val checkInStatus: String,
        val checkInTime: String,
        val checkOutAddress: String,
        val checkOutDeclare: String,
        val checkOutLatitude: String,
        val checkOutLongitude: String,
        val checkOutRemark: String,
        val checkOutStatus: String,
        val checkOutTime: String,
        val condition: Any,
        val date: String,
        val dateStr: String,
        val efficientRange: Int,
        val endDate: Any,
        val groupId: Any,
        val hasApproveBtn: Any,
        val id: String,
        val isApprove: String,
        val latitude: String,
        val longitude: String,
        val overDistance: Any,
        val projectId: String,
        val projectName: Any,
        val remark: Any,
        val rule: String,
        val standardCheckInTime: String,
        val standardCheckOutTime: String,
        val startDate: Any,
        val status: String,
        val tableTag: String,
        val traceStatus: Any,
        val traceVOList: Any,
        val userId: String,
        val userName: String,
        val workHours: Double
    )
}