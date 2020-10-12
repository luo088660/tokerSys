package com.toker.sys.view.home.activity.task.event

import com.toker.sys.view.home.fragment.task.bean.PageData

class AllTaskTypeEvent(val type: Int, val name: String)
class AllStateEvent(val type: Int, val name: String)
class SplashEvent(val type: Int)

class ApprovalTaskEvent(val type: Int) {
    var bean: PageData? = null
    var name: String = ""
    var result: String = ""
    var remark: String = ""
    constructor(type: Int, name: String) : this(type) {
        this.name = name
    }

    constructor(type: Int, bean: PageData) : this(type) {
        this.bean = bean
    }
    constructor(type: Int, bean: PageData,result: String,remark: String) : this(type) {
        this.bean = bean
        this.result = result
        this.remark = remark
    }
}

class TaskCategorieEvent(val type: Int) {

    var name: String? = ""

    constructor(type: Int, name: String) : this(type) {
        this.name = name
    }
}
class TransaReportTaskEvent(val position:Int)