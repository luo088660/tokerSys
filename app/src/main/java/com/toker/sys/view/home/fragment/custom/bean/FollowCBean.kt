package com.toker.sys.view.home.fragment.custom.bean

import java.io.Serializable

data class FollowCBean(
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

data class PublicPoCust(
    val id: String, val tableTag: String
) {
    var name: String? = ""
    var phone: String? = ""
    var tkId: String? = ""

    constructor(
        name: String,
        phone: String,
        id: String,
        tableTag: String
    ) : this(id, tableTag) {
        this.name = name
        this.phone = phone
    }

    constructor(
        tkId: String, id: String,
        tableTag: String
    ) : this(id, tableTag) {
        this.tkId = tkId
    }


}

data class Data(
    val current: Int,
    val pages: Int,
    val records: MutableList<Record>,
    val searchCount: Boolean,
    val size: Int,
    val total: Int
)

data class Record(
    val beeFlag: String,
    val bindingResult: Any,
    val condition: Any,
    val createTime: Long,
    val creator: Any,
    val creatorId: Any,
    val customerAddress: String,
    val customerArea: String,
    val customerCity: String,
    val customerLatitude: String,
    val customerLongitude: String,
    val customerProvince: String,
    val customerStreet: String,
    val delFlag: String,
    val endTime: Any,
    val followUser: String,
    val followUserId: String,
    val houseType: String,
    val id: String,
    val intentionLevel: String,
    val invalidDateFlag: Long,
    val invalidReason: String,
    val name: String,
    val page: Any,
    val pageSize: Any,
    val phone: String,
    val productAcreage: String,
    val productType: String,
    val projectId: Any,
    val projectName: String,
    val publicTime: Long,
    val purchaseFocus: String,
    val recommendTime: Long,
    val registerAddress: String,
    val registerArea: String,
    val registerCity: String,
    val registerLatitude: String,
    val registerLongitude: String,
    val registerProvince: String,
    val registerStreet: String,
    val remark: String,
    val sixNumbers: String,
    val startTime: Any,
    var status: String,
    val statusVal: String,
    val tableTag: String,
    val unfollowedTime: Any,
    val updateTime: Long,
    val updator: String,
    val updatorId: String,
    val userId: String,
    val userName: String,
    val validFlag: String
) : Serializable