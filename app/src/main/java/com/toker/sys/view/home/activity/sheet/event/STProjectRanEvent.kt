package com.toker.sys.view.home.activity.sheet.event

class MemberRanEvent(val type: Int, val name: String)
class STProjectRanEvent(val type: Int, val name: String)
class STProjectTimeEvent(val type: Int, val name: String)
class TimeEvent(val time: String, val type: String){
    var beginDate = ""
    var endDate = ""
    constructor(time: String,type: String,beginDate:String,endDate:String):this(time,type){

        this.beginDate = beginDate
        this.endDate = endDate
    }
}
class VISIEvent(val VISI: Boolean, val type: String)