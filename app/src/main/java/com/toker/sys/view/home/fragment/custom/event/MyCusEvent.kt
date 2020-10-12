package com.toker.sys.view.home.fragment.custom.event

import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import com.toker.sys.view.home.fragment.custom.bean.FollowCustBean
import com.toker.sys.view.home.fragment.custom.bean.Record

class MyCusEvent(val type: Int) {
    var bean: Record? = null
    var date: FollowCustBean.PageData? = null
    var mBeans: FollowCTBean.PageData? = null
    var mDBean: FCustomDetailTBean.Data? = null

    constructor(type: Int, bean: Record) : this(type) {
        this.bean = bean
    }

    constructor(type: Int, bean: FollowCustBean.PageData) : this(type) {
        this.date = bean
    }

    constructor(type: Int, bean: FollowCTBean.PageData) : this(type) {
        this.mBeans = bean
    }

    constructor(type: Int, bean: FCustomDetailTBean.Data) : this(type) {
        this.mDBean = bean
    }
}

class SelectUserEvent(val name: String) {
    var page:Int = 1
    constructor(name: String, page: Int) : this(name) {
        this.page = page
    }

}

