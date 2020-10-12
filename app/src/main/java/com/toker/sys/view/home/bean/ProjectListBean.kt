package com.toker.sys.view.home.bean

import java.io.Serializable

data class ProjectListBean(
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
) : Serializable {
    var stuta = 0

    constructor(
        id: String,
        projectId: String,
        projectName: String,
        userId: String,
        stuta: Int
    ) : this(
        id,
        projectId,
        projectName,
        userId
    ) {
        this.stuta = stuta
    }
}