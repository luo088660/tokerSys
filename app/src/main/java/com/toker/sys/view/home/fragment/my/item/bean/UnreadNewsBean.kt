package com.toker.sys.view.home.fragment.my.item.bean

class UnreadNewsBean {
    data class NewsBean(
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
        val current: Int,
        val pages: Int,
        val records: List<Records>,
        val searchCount: Boolean,
        val size: Int,
        val total: Int
    )
    data class Records(
        val businessId: String,
        val businessTag: String,
        val checkTime: Long,
        val content: String,
        val addAttribute: String,
        val delFlag: String,
        val id: String,
        val level: String,
        val msgType: String,
        val receiveId: String,
        val sendId: String,
        val sendTime: Long,
        val status: String,
        val tableTag: String,
        val title: String,
        val executeUrl: String,
        var type:Int

    )

}