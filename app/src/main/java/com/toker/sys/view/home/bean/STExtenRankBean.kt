package com.toker.sys.view.home.bean

class STExtenRankBean {

    data class STExtenBean(
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
        val curNum: String,//完成
        val groupName: String,
        val percent: String,//完成率
        val projectName: String,
        val rank: String,
        val targetNum: String,//目标
        val userName: String
    )

}