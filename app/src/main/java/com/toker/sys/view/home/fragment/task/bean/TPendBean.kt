package com.toker.sys.view.home.fragment.task.bean

import com.toker.sys.utils.network.bean.BaseArrayBean
import java.io.Serializable

data class TPendBean(
    val `data`: Int,
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

data class TEntireBean(
    val `data`: Data,
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
    val pageData: MutableList<PageData>,
    val page: String,
    val pageSize: String,
    val pageTotal: String,
    val rowTotal: String

)

data class PageData(
    val approvalStatus: String,
    val createTime: Long,
    val creator: String,
    val creatorId: String,
    val curDealNum: Int,
    val curPhoneNum: Int,
    val curTurnover: Double,
    val curVisitNum: Int,
    val dealNum: Int,
    val delFlag: String,
    val endTime: Long,
    val existDayTask: String,
    val existWeekTask: String,
    val id: String,
    val month: String,
    val objectType: String,
    val phoneNum: Int,
    val projectId: String,
    val startTime: Long,
    val status: String,
    val tableTag: String,
    val taskName: String,
    val projectName: String,
    val taskPath: String,
    val taskType: String,
    val turnover: Double,
    val updateTime: Long,
    val updator: String,
    val updatorId: String,
    val visitNum: Int,
    val objectList: MutableList<Object>

    /*val approvalStatus: String,
    val approve: String,
    val approveId: String,
    val approveTime: Long,
    val createTime: Long,
    val creator: String,
    val creatorId: String,
    val curDealNum: Int,
    val curPhoneNum: Int,
    val curTurnover: Double,
    val curVisitNum: Int,
    val dealNum: Int,
    val delFlag: String,
    val endTime: Long,
    val existDayTask: String,
    val existWeekTask: String,
    val id: String,
    val month: String,
    val objectList: List<TPenD_Bean.Object>,
    val objectType: String,
    val phoneNum: Int,
    val projectId: String,
    val projectName: String,
    val startTime: Long,
    val status: String,
    val tableTag: String,
    val taskName: String,
    val taskPath: String,
    val taskType: String,
    val turnover: Double,
    val updateTime: Long,
    val updator: String,
    val updatorId: String,
    val visitNum: Int*/

) : Serializable


data class Object(
    /*val curDealNum: Int,
    val curPhoneNum: Int,
    val curTurnover: Int,
    val curVisitNum: Int,
    val date: Any,
    val dealNum: Int,
    val delFlag: String,
    val enterTime: Long,
    val id: String,
    val leaveTime: Any,
    val objectId: String,
    val objectName: String,
    val phoneNum: Int,
    val tableTag: String,
    val taskId: String,
    val turnover: Int,
    val visitNum: Int*/

    val curDealNum: Int,
    val curPhoneNum: Int,
    val curTurnover: Int,
    val curVisitNum: Int,
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
):Serializable
