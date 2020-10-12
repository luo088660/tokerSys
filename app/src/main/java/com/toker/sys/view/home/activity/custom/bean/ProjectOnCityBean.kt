package com.toker.sys.view.home.activity.custom.bean

class ProjectOnCityBean {
    data class ProjectCityBean(
        val `data`: MutableList<Data>,
        val code: String,
        val desc: String
    ) {
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val isUnionSale: String,
        val projectId: String,
        val projectName: String,
        val subProjectCode: String,
        val id: String
    )
}