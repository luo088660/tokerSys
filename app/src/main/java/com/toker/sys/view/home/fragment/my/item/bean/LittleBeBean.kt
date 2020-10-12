package com.toker.sys.view.home.fragment.my.item.bean

class LittleBeBean {
    data class LittleBean(
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
        val beeId: String,
        val beeName: String,
        val beePhone: String,
        val userName: String
    )
}