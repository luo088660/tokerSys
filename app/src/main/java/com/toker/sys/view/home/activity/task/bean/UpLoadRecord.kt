package com.toker.sys.view.home.activity.task.bean

class UpLoadRecord {
    data class UpRecord(
        val `data`: Data,
        val code: String,
        val desc: String
    ){
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

    data class Data(
        val ETag: String,
        val OssFilePathName: String,
        val Url: String
    )
}