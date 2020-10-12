package com.toker.sys.view.home.bean

class ProjectNoBean {
    data class ProjectNoTBean(
        val `data`: Data,
        val code: String,
        val desc: String
    ){
        /**
         * 请求是否成功
         */
        fun isSuccess(): Boolean {
            return code == "1"
        }

    }


    data class Data(
        val page: Page,
        val summary: Summary
    )

    data class Page(
        val records: MutableList<Record>
    )

    data class Record(
        val createTime: CreateTime,
        val id: String,
        val projectCode: String,
        val projectName: String,
        val recommendNum: Int,
        val totalNum: Int,
        val updateTime: UpdateTime
    )

    data class UpdateTime(
        val chronology: Chronology,
        val dayOfMonth: Int,
        val dayOfWeek: String,
        val dayOfYear: Int,
        val hour: Int,
        val minute: Int,
        val month: String,
        val monthValue: Int,
        val nano: Int,
        val second: Int,
        val year: Int
    )

    data class Chronology(
        val calendarType: String,
        val id: String
    )

    data class CreateTime(
        val chronology: Chronology,
        val dayOfMonth: Int,
        val dayOfWeek: String,
        val dayOfYear: Int,
        val era: String,
        val leapYear: Boolean,
        val month: String,
        val monthValue: Int,
        val year: Int
    )

    data class Summary(
        val createTime: Any,
        val id: Any,
        val projectCode: Any,
        val projectName: Any,
        val recommendNum: Int,
        val totalNum: Int,
        val updateTime: Any
    )
}