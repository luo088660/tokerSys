package com.toker.sys.view.home.fragment.task.bean

class TaskTrajectoryBean {
    data class TrajectoryBean(
        val `data`: Data,
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
        val error: Int,
        val right: Int,
        val traceList: MutableList<Trace>
    )

    data class Trace(
        val date: Long,
        val id: String,
        val objectId: String,
        val pic: String,
        val remark: String,
        val status: String,
        val tableTag: String,
        val taskId: String,
        val userName: String
    )
}