package com.toker.sys.view.home.fragment.custom.bean

class PublicPoolBean {

    data class PublicBean(
        val code: String,
        val `data`: Data,
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
        val dto: Dto,
        val total: String
    )

    data class Dto(
        val page: String,
        val pageData: List<PageData>,
        val pageSize: String,
        val pageTotal: String,
        val rowTotal: String
    )

    data class PageData(
        val countNum: Int,
        val projectId: String,
        val projectName: String
    )
}

