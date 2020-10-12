package com.toker.sys.view.home.bean

class FollowUpBean {
    data class FollowBean(
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
        val page: Page,
        val summary: Summary
    )

    data class Page(
        val current: Int,
        val pages: Int,
        val records: MutableList<Record>,
        val searchCount: Boolean,
        val size: Int,
        val total: Int
    )

    data class Record(
        val createTime: Long,
        val day: String,
        val expiredNum: Int,
        val firstNum: Int,
        val id: String,
        val month: String,
        val noRecommendNum: Int,
        val projectCode: String,
        val projectName: String,
        val recommendNum: Int,
        val recruitNum: Int,
        val reportNum: Int,
        val secondNum: Int,
        val signingNum: Int,
        val subscriptionNum: Int,
        val tableTag: Long,
        val totalNum: Int,
        val updateTime: Long,
        val visitNum: Int,
        val year: String
    )

    data class Summary(
        val createTime: Any,
        val day: Any,
        val expiredNum: Int,
        val id: Any,
        val month: Any,
        val noRecommendNum: Int,
        val projectCode: Any,
        val projectName: Any,
        val recommendNum: Int,
        val recruitNum: Int,
        val reportNum: Int,
        val signingNum: Int,
        val subscriptionNum: Int,
        val tableTag: Any,
        val totalNum: Int,
        val updateTime: Any,
        val visitNum: Int,
        val year: Any
    )
}