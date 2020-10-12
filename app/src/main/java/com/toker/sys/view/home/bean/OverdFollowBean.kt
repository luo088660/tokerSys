package com.toker.sys.view.home.bean

/**
 * packageName: com.toker.sys.view.home.bean
 * author: star
 * created on: 2019/11/13 17:24
 * description:
 */
class OverdFollowBean {
    data class OverdBean(
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
        val current: Int,
        val pages: Int,
        val records: MutableList<Record>,
        val searchCount: Boolean,
        val size: Int,
        val total: Int
    )

    data class Record(
        val count: Any,
        val createTime: Long,
        val createTimeStamp: Long,
        val customerId: String,
        val customerName: String,
        val phone: String,
        val recordTime: Long,
        val status: String,
        val tableTag: String,
        val userName: String
    )
}