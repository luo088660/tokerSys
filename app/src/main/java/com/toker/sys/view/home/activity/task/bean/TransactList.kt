package com.toker.sys.view.home.activity.task.bean

class TransactList {
    data class TransactListBean(
        val `data`: MutableList<Data>,
        val code: String,
        val desc: String
    ) {
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val content: String,
        val createTime: Long,
        val creator: String,
        val creatorId: String,
        val delFlag: String,
        val fileList: MutableList<File>,
        val filePath: Any,
        val id: String,
        val pic: String,
        val tableTag: String,
        val taskId: String,
        val traceRemark: String,
        val traceStatus: String,
        val updateTime: Long,
        val updator: String,
        val updatorId: String,
        val userId: String,
        val userName: String
    )

    data class File(
        val fileName: String,
        val filePath: String,
        val id: String,
        val recordId: String,
        val tableTag: String
    )
}