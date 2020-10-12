package com.toker.sys.view.home.activity.custom.event

class FUAllStatesEvent(val type:Int ,val name:String)
class FUAllRecUserEvent(val name:String)
class NCSubPrompEvent(val isfo: Boolean){
    var isTStatus = 0
    constructor(isfo: Boolean,isTStatus:Int):this(isfo){
        this.isTStatus = isTStatus
    }
}
class NCExistedEvent(val isfo: Boolean)