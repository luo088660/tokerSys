package com.toker.sys.view.home.fragment.custom.bean

import java.io.Serializable

class FollowCustBean {
    data class FollowCtBean(
        val `data`: Data,
        val code: String,
        val desc: String
    ){
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
        val beeFlag: String,
        val createTime: Long,
        val id: String,
        val name: String,
        val notFollowDays: String,
        val phone: String,
        val status: String,
        val tableTag: String,
        val invalidReason: String,
        val userName: String
    ):Serializable
}