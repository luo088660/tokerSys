package com.toker.sys.view.home.activity.custom.bean

/**
 * packageName: com.toker.sys.view.home.activity.custom.bean
 * author: star
 * created on: 2019/12/2 9:58
 * description:
 */
class CustomerRecBean {

    data class CuRecBean(
        val `data`: Data,
        val code: String,
        val desc: String
    ) {
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val activateSum: Any,
        val arriveAddr: Any,
        val arriveArea: Any,
        val createTime: Long,
        val customerId: String,
        val delFlag: String,
        val firstRecommendTime: Long,
        val groupList: String,
        val houseType: Any,
        val id: String,
        val isUnionSale: String,
        val isUserProject: String,
        val name: String,
        val onlineFlag: Any,
        val outRecommend: String,
        val outRecommendTime: Any,
        val outVisit: String,
        val outVisitTime: Any,
        val phone: String,
        val productAcreage: Any,
        val productType: Any,
        val projectCity: Any,
        val projectId: String,
        val province: Any,
        val purchaseFocus: Any,
        val recommendChannels: String,
        val recommendId: String,
        val recommendProject: String,
        val recommendTime: Long,
        val recommendUserId: String,
        val registerAddress: String,
        val remark: String,
        val sixNumbers: String,
        val status: String,
        val tableTag: String,
        val tradingMoney: Any,
        val tradingVolume: Int,
        val userName: String,
        val visitFlag: String,
        val visitTime: Long
    )
}