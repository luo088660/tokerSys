package com.toker.sys.view.home.fragment.my.item.bean

import java.io.Serializable

class ProjectListBean {
    data class ProjectBean(
        val `data`: MutableList<Data>,
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
        val id: String,
        val projectId: String,
        val projectName: String,
        val userId: String
    ):Serializable{
        override fun toString(): String {
            return "Data(id='$id', projectId='$projectId', projectName='$projectName', userId='$userId')"
        }
    }
}