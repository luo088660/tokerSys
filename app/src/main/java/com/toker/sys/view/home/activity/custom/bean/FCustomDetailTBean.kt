package com.toker.sys.view.home.activity.custom.bean

import java.io.Serializable

class FCustomDetailTBean {

    data class Bean(
        val `data`: Data,
        val code: String,
        val desc: String
    ) {
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(

        var intentionCustomerVO: IntentionCustomerVO,
        var reCustomerVO: ReCustomerVO

    ) : Serializable {
        var customerRecordsVOList: MutableList<CustomerRecordsVO> = mutableListOf()
        var customerStateChangeVOList: MutableList<beans> = mutableListOf()
        var recommendVO: RecommendVO? = null

        constructor(
            intentionCustomerVO: IntentionCustomerVO,
            reCustomerVO: ReCustomerVO,
            customerRecordsVOList: MutableList<CustomerRecordsVO>,
            customerStateChangeVOList: MutableList<beans>,
            recommendVO: RecommendVO?
        ) : this(
            intentionCustomerVO,
            reCustomerVO
        ) {
            this.customerRecordsVOList = customerRecordsVOList
            this.customerStateChangeVOList = customerStateChangeVOList
            this.recommendVO = recommendVO
        }

    }

    data class RecommendVO(
        val createTime: String,
        val name: String,
        val phone: String,
        val registerAddress: String,
        val remark: String,
        var status: String,
        val creator: String,
        val customerAddress: String,
        val userName: String,
        val arriveAddr: String,
        val arriveArea: String,
        val customerId: String,
        val customerLatitude: String,
        val customerLongitude: String,
        var id: String,
        val isUnionSale: String,
        val projectId: String,
        val projectName: String,
        val recommendId: String,
        val tableTag: String,
        val projectCity: String
    ) : Serializable

    data class CustomerRecordsVO(
        val createTime: String,
        val record: String,
        val tkName: String
    ) : Serializable

    data class ReCustomerVO(
        val arriveAddr: String,
        val arriveArea: String,
        val houseType: String,
        val intentionLevel: String,
        val productAcreage: String,
        val productType: String,
        val province: String,
        val purchaseFocus: String,
        val recommendProject: String,
        val customerProvince: String,
        val sixNumbers: String,
        val visitTime: String
    ) : Serializable

    data class IntentionCustomerVO(
        val tableTag: String,
        var id: String,
        val customerId: String,
        val createTime: String,
        val creator: String,
        val customerAddress: String,
        val name: String,
        val phone: String,
        val registerAddress: String,
        val remark: String,
        val status: String,
        val userName: String,
        val invalidDate: Long,
        val invalidReason: String,
        val invalidUser: String
    ) : Serializable

    data class beans(
        val changeTime: Long,
        val content: String,
        val recommendId: String,
        val status: String,
        val userName: String
    ): Serializable
}