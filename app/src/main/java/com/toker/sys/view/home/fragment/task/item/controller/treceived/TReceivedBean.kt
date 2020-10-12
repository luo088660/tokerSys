package com.toker.sys.view.home.fragment.task.item.controller.treceived

/**
 * 事务型任务
 */
data class TReceivedBean(
    val `data`: Data,
    val code: String,
    val desc: String
){
    /**
     * 请求是否成功
     */
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
    val content: String,
    val createTime: Long,
    val creator: String,
    val creatorId: String,
    val delFlag: String,
    val efficientRange: Int,
    val endDate: Long,
    val endTime: String,
    val handle: Any,
    val id: String,
    val latitude: Double,
    val level: String,
    val longitude: Double,
    val objectList: List<Object>,
    val projectId: String,
    val remark: String,
    val startDate: Long,
    val startTime: String,
    val status: String,
    val tableTag: String,
    val taskName: String,
    val projectName: String,
    val updateTime: Long,
    val updator: String,
    val updatorId: String,
    val hasRecord: String,

    val errorNum: Int,
    val recordNum: Int,
    val rightNum: Int,
    val unRecordNum: Int


)

data class Object(
    val id: String,
    val objectId: String,
    val objectName: String,
    val tableTag: String,
    val taskId: String
)

class bean{
    data class bab(
    val address: String,
    val content: String,
    val createTime: Long,
    val creator: String,
    val creatorId: String,
    val delFlag: String,
    val efficientRange: Int,
    val endDate: Long,
    val endTime: String,
    val errorNum: Int,
    val id: String,
    val latitude: Double,
    val level: String,
    val longitude: Double,
    val objectList: List<Object>,
    val projectId: String,
    val projectName: String,
    val recordNum: Int,
    val remark: String,
    val rightNum: Int,
    val startDate: Long,
    val startTime: String,
    val status: String,
    val tableTag: String,
    val taskName: String,
    val unRecordNum: Int,
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