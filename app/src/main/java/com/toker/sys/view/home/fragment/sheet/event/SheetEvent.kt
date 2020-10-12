package com.toker.sys.view.home.fragment.sheet.event

class SheetEvent(var type: Int) {
    var name: String = ""
    var stuta:Int= 0

    constructor(type:Int,stuta:Int):this(type){
        this.stuta = stuta
    }

    constructor(type: Int, name: String) : this(type) {
        this.name = name
    }
}