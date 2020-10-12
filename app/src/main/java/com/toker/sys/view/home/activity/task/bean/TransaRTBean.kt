package com.toker.sys.view.home.activity.task.bean

/**
 * packageName: com.toker.sys.view.home.activity.task.bean
 * author: star
 * created on: 2019/11/12 10:47
 * description:
 */
class TransaRTBean {
    class Bean(
        val `data`: Any,
        val code: String,
        val desc: String
    ) {
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }
}