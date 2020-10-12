package com.toker.sys.view.home.activity.my.bean

class GroupProjectBean {
    data class GroupPjtBean(
        val `data`: MutableList<Data>,
        val code: String,
        val desc: String
    ){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val groupId: String,
        val groupName: String
    )
}