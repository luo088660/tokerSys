package com.toker.sys.view.home.fragment.my.item.myatten.bean

class MeAttenBean {
    data class MeAttenTBean(
        val `data`: Data,
        val code: String,
        val desc: String
    ) {
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }
    data class Data(
        val address: String,
        val date: String,
        val efficientRange: Int,
        val id: String,
        val isApprove: String,
        val latitude: String,
        val longitude: String,
        val projectId: String,
        val rule: String,
        val standardCheckInTime: String,
        val standardCheckOutTime: String,
        val tableTag: String,
        val userId: String,
        val userName: String,
        val allowDeviateDistance: String,

        val checkInAddress: String,
        val checkInLatitude: String,
        val checkInLongitude: String,
        val checkInRemark: String,
        val checkInStatus: String,
        val checkInTime: String,
        val checkOutAddress: String,
        val checkOutLatitude: String,
        val checkOutLongitude: String,
        val checkOutRemark: String,
        val checkOutStatus: String,
        val checkOutTime: String,
        val status: String,
        val workHours: Double,
        val checkOutDeclare: String,
        val checkInDeclare: String,


        val locationNum: String,
        val locusUnusualRule: String,
        val phone: String,
        val traceStatus: String
    )
}

