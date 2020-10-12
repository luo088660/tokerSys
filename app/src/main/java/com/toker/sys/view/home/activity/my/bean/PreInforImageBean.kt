package com.toker.sys.view.home.activity.my.bean

/**
 * packageName: com.toker.sys.view.home.activity.my.bean
 * author: star
 * created on: 2019/10/29 9:21
 * description:
 */
class PreInforImageBean {
    data class PreInImageBean(
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