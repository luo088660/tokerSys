package com.toker.sys.view.home.activity.custom.bean

import java.io.Serializable

class VisiPlaceBean {
    data class VisiPBean(
        val `data`: MutableList<Data>,
        val code: String,
        val desc: String
    ){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }


    data class Data(
        val arriveArea: String,
        val arriveAreaId: String
    ):Serializable
}