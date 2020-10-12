package com.toker.sys.view.home.fragment.custom.bean

import java.io.Serializable

class FollowCTBean {

    data class FollowCsTBean(
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
        val page: String,
        val pageData: MutableList<PageData>,
        val pageSize: String,
        val pageTotal: String,
        val rowTotal: String
    )

    data class PageData(
        val id: String,
        val tableTag: String

    ) : Serializable {
        var beeFlag: String = ""
        var createTime: Long = 0
        var customerAddress: String = ""
        var customerArea: String = ""
        var customerCity: String = ""
        var customerLatitude: String = ""
        var customerLongitude: String = ""
        var customerProvince: String = ""
        var customerStreet: String = ""
        var houseType: String = ""
        var intentionLevel: String = ""
        var name: String = ""
        var notFollowDays: String = ""
        var phone: String = ""
        var productAcreage: String = ""
        var productType: String = ""
        var purchaseFocus: String = ""
        var registerAddress: String = ""
        var registerArea: String = ""
        var registerCity: String = ""
        var registerProvince: String = ""
        var registerStreet: String = ""
        var remark: String = ""
        var sixNumbers: String = ""
        var status: String = ""
        var userName: String = ""
        var outRecommend: String = ""
        var outVisit: String = ""

       constructor(id: String, tableTag: String,outRecommend: String,outVisit: String ): this(id, tableTag) {
            this.outRecommend = outRecommend
            this.outVisit = outVisit
        }
        constructor(id: String, tableTag: String, notFollowDays: Int) : this(id, tableTag) {
            this.notFollowDays = notFollowDays.toString()
        }

        constructor(phone: String, id: String, tableTag: String) : this(id, tableTag) {
            this.phone = phone
        }


        constructor(
            id: String,
            tableTag: String,
            beeFlag: String,
            createTime: Long,
            customerAddress: String,
            customerArea: String,
            customerCity: String,
            customerLatitude: String,
            customerLongitude: String,
            customerProvince: String,
            customerStreet: String,
            houseType: String,
            intentionLevel: String,
            name: String,
            notFollowDays: String,
            phone: String,
            productAcreage: String,
            productType: String,
            purchaseFocus: String,
            registerAddress: String,
            registerArea: String,
            registerCity: String,
            registerProvince: String,
            registerStreet: String,
            remark: String,
            sixNumbers: String,
            status: String,
            userName: String
        ) : this(id, tableTag) {
            this.beeFlag = beeFlag
            this.createTime = createTime
            this.customerAddress = customerAddress
            this.customerArea = customerArea
            this.customerCity = customerCity
            this.customerLatitude = customerLatitude
            this.customerLongitude = customerLongitude
            this.customerProvince = customerProvince
            this.customerStreet = customerStreet
            this.houseType = houseType
            this.intentionLevel = intentionLevel
            this.name = name
            this.notFollowDays = notFollowDays
            this.phone = phone
            this.productAcreage = productAcreage
            this.productType = productType
            this.purchaseFocus = purchaseFocus
            this.registerAddress = registerAddress
            this.registerArea = registerArea
            this.registerCity = registerCity
            this.registerProvince = registerProvince
            this.registerStreet = registerStreet
            this.remark = remark
            this.sixNumbers = sixNumbers
            this.status = status
            this.userName = userName
        }

    }

}