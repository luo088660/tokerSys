package com.toker.sys.view.home.activity.my.bean

import java.io.Serializable


class ViewAttenListBean {
    data class ViewAttenBean(
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
        val remark: String,
        val rule: String,
        val standardCheckInTime: String,
        val standardCheckOutTime: String,
        val startDate: Any,
        val status: String,
        val tableTag: String,
        val traceStatus: String,
        val traceVOList: Any,
        val userId: String,
        val userName: String,
        val workHours: Double
    )

    data class BeanEvent(val userName:String,val date:String,val userId:String):Serializable
}