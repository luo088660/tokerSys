package com.toker.sys.view.home.activity.custom.bean

data class FCustomDetailBean(
    val `data`: Data,
    val code: String,
    val desc: String
){
    fun isSuccess(): Boolean {
        return code == "1"
    }
}


data class Data(
    val TbCustomerRecord: List<TbCustomerRecord>,
    val customer: Customer
)

data class Customer(
    val arriveAddr: Any,
    val arriveArea: Any,
    val beeFlag: String,
    val bindingResult: Any,
    val condition: Any,
    val createTime: Long,
    val creator: String,
    val creatorId: Any,
    val customerAddress: String,
    val customerArea: Any,
    val customerCity: Any,
    val customerLatitude: Any,
    val customerLongitude: Any,
    val customerProvince: String,
    val customerStreet: Any,
    val delFlag: String,
    val endTime: Any,
    val followUser: String,
    val followUserId: String,
    val houseType: Any,
    val id: String,
    val intentionLevel: String,
    val invalidDateFlag: Long,
    val invalidReason: String,
    val name: String,
    val page: Any,
    val pageSize: Any,
    val phone: String,
    val productAcreage: Any,
    val productType: Any,
    val projectId: Any,
    val projectName: Any,
    val publicTime: Long,
    val purchaseFocus: Any,
    val recommendProject: String,
    val recommendTime: Any,
    val registerAddress: String,
    val registerArea: Any,
    val registerCity: Any,
    val registerLatitude: Any,
    val registerLongitude: Any,
    val registerProvince: Any,
    val registerStreet: Any,
    val remark: String,
    val sixNumbers: String,
    val startTime: Any,
    val status: String,
    val statusVal: Any,
    val tableTag: String,
    val unfollowedTime: Any,
    val updateTime: Long,
    val updator: String,
    val updatorId: Any,
    val userId: String,
    val userName: String,
    val validFlag: String,
    val visitTime: Long
)

data class TbCustomerRecord(
    val createTime: Long,
    val customerId: String,
    val id: String,
    val record: String,
    val tableTag: String,
    val userId: Any,
    val userName: String
)