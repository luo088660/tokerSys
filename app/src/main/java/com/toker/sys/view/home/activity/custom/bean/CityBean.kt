package com.toker.sys.view.home.activity.custom.bean

class CityBean {
    data class CityTBean(
        val `data`: MutableList<Data>,
        val code: String,
        val desc: String
    ) {
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val provinceId: String,
        val provinceName: String
    )
}