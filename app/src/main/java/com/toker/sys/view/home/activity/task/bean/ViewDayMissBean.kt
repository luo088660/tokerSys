package com.toker.sys.view.home.activity.task.bean

class ViewDayMissBean {
    data class ViewDaYMissBean(
    val `data`: MutableList<Data>,
    val code: String,
    val desc: String
){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

data class Data(
    val objectId: String,
    val objectName: String,
    val tbTaskTargetObjectRefs: MutableList<TbTaskTargetObjectRef>
)

data class TbTaskTargetObjectRef(
    val curDealNum: Int,
    val curPhoneNum: Int,
    val curTurnover: Int,
    val curVisitNum: Int,
    val date: Long,
    val dealNum: Int,
    val delFlag: String,
    val enterTime: Long,
    val id: String,
    val objectId: String,
    val objectName: String,
    val phoneNum: Int,
    val tableTag: String,
    val taskId: String,
    val turnover: Int,
    val visitNum: Int
)
}