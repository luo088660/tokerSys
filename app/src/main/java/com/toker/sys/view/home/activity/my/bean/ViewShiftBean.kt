package com.toker.sys.view.home.activity.my.bean

class ViewShiftBean {
    data class ViewShiftDBean(
        val `data`: Data,
        val code: String,
        val desc: String
    ){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val address: String,
        val efficient_range: String,
        val latitude: String,
        val longitude: String,
        val personalLeaveList: MutableList<Work>,
        val standardCheckInTime: String,
        val standardCheckOutTime: String,
        val workList: MutableList<Work>
    )
    data class Work(
        val userId: String,
        val userName: String,
        val personalFlag: String,
        val pic: String
    )
}