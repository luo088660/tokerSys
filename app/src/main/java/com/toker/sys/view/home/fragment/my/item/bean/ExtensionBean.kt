package com.toker.sys.view.home.fragment.my.item.bean

class ExtensionBean {
    data class ExtensiBean(
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
        val groupName: String,
        val position: String,
        val userId: String,
        val userName: String,
        val userPhone: String
    )
}