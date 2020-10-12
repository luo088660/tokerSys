package com.toker.sys.view.home.activity.custom.bean

class RecomCustBean {
    data class RecomCBean(
        val `data`: Data,
        val code: String,
        val desc: String
    ){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val page: String,
        val pageData: List<PageData>,
        val pageSize: String,
        val pageTotal: String,
        val rowTotal: String
    )

    data class PageData(
        val createTime: String,
        val houseType: String,
        val id: String,
        val intentionLevel: String,
        val isUnionSale: String,
        val name: String,
        val outRecommend: String,
        val outVisit: String,
        val phone: String,
        val productAcreage: String,
        val productType: String,
        val projectName: String,
        val province: String,
        val purchaseFocus: String,
        val registerAddress: String,
        val remark: String,
        val sixNumbers: String,
        val status: String,
        val tableTag: String,
        val userName: String,
        val visitTime: String,

        val arriveAddr: String,
        val arriveArea: String,
        val customerAddress: String,
        val customerArea: String,
        val customerCity: String,
        val customerId: String,
        val customerLatitude: String,
        val customerLongitude: String,
        val customerProvince: String,
        val customerStreet: String,
        val projectCode: String,
        val projectId: String,
        val recommendProject: String,
        val registerArea: String,
        val registerCity: String,
        val registerProvince: String,
        val registerStreet: String
    )

}