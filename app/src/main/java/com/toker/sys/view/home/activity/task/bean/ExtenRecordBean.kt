package com.toker.sys.view.home.activity.task.bean

class ExtenRecordBean {
    data class ExtenRBean(
        val `data`: Data,
        val code: String,
        val desc: String
    ) {
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
        val beeFlag: String,
        val createTime: Long,
        val customerAddress: String,
        val customerArea: String,
        val customerCity: String,
        val customerLatitude: String,
        val customerLongitude: String,
        val customerProvince: String,
        val customerStreet: Any,
        val houseType: String,
        val id: String,
        val intentionLevel: String,
        val invalidReason: Any,
        val name: String,
        val notFollowDays: String,
        val phone: String,
        val productAcreage: Any,
        val productType: Any,
        val purchaseFocus: Any,
        val recordTime: Any,
        val registerAddress: String,
        val registerArea: String,
        val registerCity: String,
        val registerProvince: String,
        val registerStreet: Any,
        val remark: String,
        val sixNumbers: String,
        val status: String,
        val tableTag: String,
        val userName: String
    )
}