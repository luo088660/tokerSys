package com.toker.sys.view.home.activity.custom.event

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class ChCustStatuEvent : Serializable {
    var name: String? = null
    var type: Int = 0

    constructor(type: Int, name: String) {
        this.type = type
        this.name = name
    }

    override fun toString(): String {
        return "ChCustStatuEvent(name=$name, type=$type)"
    }


}

data class ChCustStatuBean(var type: Int) : Serializable {
    var productType: String? = ""
    var productAcreage: String? =  ""
    var houseType: String? =  ""
    var purchaseFocus: String? =  ""


    constructor(
        type: Int,
        productType: String?,//产品类型
        productAcreage: String?,//面积段
        houseType: String?,//户型
        purchaseFocus: String?//购房关注点
    ) : this(type) {
        this.productType = productType
        this.productAcreage = productAcreage
        this.houseType = houseType
        this.purchaseFocus = purchaseFocus
    }


    override fun toString(): String {
        return "ChCustStatuEvent(type=$type, productType=$productType, productAcreage=$productAcreage, houseType=$houseType, purchaseFocus=$purchaseFocus)"
    }
}

data class CustomerInvalid(val type:Int ,var context:String){
    var isType:Int = 0
    constructor( type:Int ,context:String,isType:Int):this(type,context){
        this.isType = isType
    }
}
