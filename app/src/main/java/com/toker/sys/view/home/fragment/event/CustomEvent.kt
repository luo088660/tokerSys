package com.toker.sys.view.home.fragment.event

import android.graphics.Bitmap
import com.toker.sys.view.home.fragment.task.bean.PageData

class CustomEvent(val name: String)
class CustomEventT(val name: String)
class CustomProjectEvent(val type: Int)
class MyOCusEvent(val phone:String)
class ReadCountEvent(val numBer:Int)
class IamgeBitmap(val bmpPath: String)
class TaskEvent(val name: String)
class TaskEventT(val name: String)
class TaskCustEvent(val isType: Boolean)
data class TaskHomeEvent(val type: Int) {
    var bean: PageData? = null
    var id:String? = ""
    var tableTag:String? = ""
    var updateTime:Long? = 0

    constructor (type: Int, bean: PageData) : this(type) {
        this.bean = bean
    }

    constructor(type: Int,id: String,tableTag:String):this(type){
        this.id = id
        this.tableTag = tableTag
    }
    constructor(type: Int,id: String,tableTag:String,updateTime:Long):this(type){
        this.id = id
        this.tableTag = tableTag
        this.updateTime = updateTime
    }
}