package com.toker.sys.view.home.bean

class CustoDatailBean {
    data class CustoDBean(
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
        val records: MutableList<Record>,
        val searchCount: Boolean,
        val size: Int,
        val total: Int
    )

    data class Record(
        val count: String,
        val createTime: Long,
        val custName: String,
        val customerId: String,
        var tableTag: String,
        val phone: String,
        val status: String,
        val usrName: String
    )

}