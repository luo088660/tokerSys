package com.toker.sys.view.home.activity.custom.bean

/**
 * packageName: com.toker.sys.view.home.activity.custom.bean
 * author: star
 * created on: 2019/11/5 9:44
 * description:
 */

class MCFcustomBean {
    data class Bean(
        val `data`: Any,
        val code: String,
        val desc: String
    ) {
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }

}