package com.toker.sys.view.home.activity.custom.bean

class b{
    data class a(
    val `data`: Data,
    val code: String,
    val desc: String
)

data class Data(
    val customerRecordsVOList: Any,
    val customerStateChangeVOList: List<Any>,
    val intentionCustomerVO: Any,
    val reCustomerVO: ReCustomerVO,
    val recommendVO: RecommendVO
)

data class RecommendVO(
    val createTime: String,
    val name: String,
    val phone: String,
    val registerAddress: String,
    val remark: Any,
    val status: String,
    val userName: String
)

data class ReCustomerVO(
    val arriveAddr: Any,
    val arriveArea: Any,
    val houseType: String,
    val intentionLevel: Any,
    val productAcreage: String,
    val productType: String,
    val province: String,
    val purchaseFocus: String,
    val recommendProject: String,
    val sixNumbers: String,
    val visitTime: String
)
}
