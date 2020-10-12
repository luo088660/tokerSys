package com.toker.sys.view.home.activity.my.bean

import java.io.Serializable


class ViewAttenListBean1 {
    data class ViewAttenBean(
        val `data`: MutableList<Data>,
        val code: String,
        val desc: String
    ){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val address: String,
        val allowDeviateDistance: Any,
        val checkInAddress: Any,
        val checkInDeclare: Any,
        val checkInLatitude: Any,
        val checkInLongitude: Any,
        val checkInRemark: String,
        val checkInStatus: String,
        val checkInTime: String,
        val checkOutAddress: Any,
        val checkOutDeclare: Any,
        val checkOutLatitude: Any,
        val checkOutLongitude: Any,
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
        val locusLocationNum: Any,
        val longitude: String,
        val overDistance: Any,
        val phone: String,
        val projectId: String,
        val projectName: Any,
        val remark: Any,
        val rule: String,
        val standardCheckInTime: String,
        val standardCheckOutTime: String,
        val startDate: Any,
        val status: Any,
        val tableTag: String,
        val traceStatus: Any,
        val traceVOList: Any,
        val userId: String,
        val userName: String,
        val workHours: Any
    )
}