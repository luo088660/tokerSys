package com.toker.sys.view.home.activity.task.bean

/**
 * 请求是否成功
 */


data class MonthIBean(
    val `data`: Data,
    val code: String,
    val desc: String
){
    fun isSuccess(): Boolean {
        return code == "1"
    }
}

data class Data(
    val approvalStatus: String,
    val approve: String,
    val approveId: String,
    val approveRemark: String,
    val approveTime: String,
    val createTime: Long,
    val creator: String,
    val creatorId: String,
    val curDealNum: Int,
    val curPhoneNum: Int,
    val curTurnover: Int,
    val curVisitNum: Int,
    val dealNum: Int,
    val delFlag: String,
    val endTime: Long,
    val existDayTask: String,
    val existWeekTask: String,
    val id: String,
    val month: String,
    val objectList: MutableList<Object>,
    val objectType: String,
    val phoneNum: Int,
    val projectId: String,
    val projectName: String,
    val startTime: Long,
    val status: String,
    val tableTag: String,
    val taskName: String,
    val taskPath: String,
    val taskTargets: MutableList<TaskTarget>,
    val taskType: String,
    val turnover: Int,
    val updateTime: String,
    val updator: String,
    val updatorId: String,
    val visitNum: Int
)

data class Object(
    val curDealNum: Int,
    val curPhoneNum: Int,
    val curTurnover: Int,
    val curVisitNum: Int,
    val date: String,
    val dealNum: Int,
    val delFlag: String,
    val enterTime: String,
    val id: String,
    val objectId: String,
    val objectName: String,
    val phoneNum: Int,
    val tableTag: String,
    val taskId: String,
    val turnover: Int,
    val visitNum: Int
)

data class TaskTarget(
    val approvalStatus: String,
    val createTime: Long,
    val creator: String,
    val creatorId: String,
    val curDealNum: Int,
    val curPhoneNum: Int,
    val curTurnover: Int,
    val curVisitNum: Int,
    val dealNum: Int,
    val delFlag: String,
    val endTime: Long,
    val existDayTask: String,
    val existWeekTask: String,
    val id: String,
    val month: String,
    val objectList: MutableList<Any>,
    val objectType: String,
    val parentId: String,
    val phoneNum: Int,
    val projectId: String,
    val projectName: String,
    val startTime: Long,
    val status: String,
    val tableTag: String,
    val taskName: String,
    val taskPath: String,
    val taskType: String,
    val turnover: Int,
    val updateTime: Long,
    val updator: String,
    val updatorId: String,
    val visitNum: Int
)