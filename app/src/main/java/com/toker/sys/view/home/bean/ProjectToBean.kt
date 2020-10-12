package com.toker.sys.view.home.bean

import java.io.Serializable

class ProjectToBean {

    data class ProjectToABean(
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

    data class Summary(
        val createTime: Any,
        val id: Any,
        val overdueNum: Int,
        val projectCode: Any,
        val projectName: Any,
        val toPoolNum: Int,
        val updateTime: Any
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
        val id: String,
        val overdueNum: Int,
        val projectCode: String,
        val projectName: String,
        val toPoolNum: Int,
        val updateTime: Long
    ):Serializable
}
