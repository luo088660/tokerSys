package com.toker.sys.view.home.fragment.my.event

class MyEvent(val type: Int) {
    var name: String = ""

    constructor(type: Int, name: String) : this(type) {
        this.name = name
    }

}