package com.toker.sys.view.home.fragment.task.bean

class MissReportBean {
    data class MissReporBean(
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
        val recordList: MutableList<Record>,
        val unRecordList: MutableList<UnRecord>
    )

    data class Record(
        val content: String,
        val createTime: Long,
        val creator: String,
        val creatorId: String,
        val delFlag: String,
        val fileList: List<File>,
        val id: String,
        val pic: String,
        val tableTag: String,
        val taskId: String,
        val updateTime: Long,
        val updator: String,
        val updatorId: String,
        val userId: String,
        val userName: String
       /* id" : "1",
    "createTime" : 1563099623000,
    "creatorId" : "114",
    "creator" : "1",
    "updateTime" : 1563099636000,
    "updatorId" : "1",
    "updator" : "1",
    "delFlag" : "0",
    "taskId" : "5815af81-d538-4908-8c70-0c7f4540d96c",
    "userId" : "114",
    "content" : "派发传单20张，留电16个",
    "tableTag" : "2019-08-01",
    "userName" : "韩七",
    "pic" : "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3698764215,42315520&fm=26&gp=0.jpg",
    "fileList" : [ ],
    "filePath" : null,
    "traceStatus" : "1",
    "traceRemark" : ""*/
    )

    data class File(
        val fileName: String,
        val filePath: String,
        val id: String,
        val recordId: String,
        val tableTag: String
    )
    data class TableTag(
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

    data class Chronology(
        val calendarType: String,
        val id: String
    )
    data class UnRecord(
        val userId: String,
        val pic: String,
        val userName: String
    )
}
