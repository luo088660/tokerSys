package com.toker.sys.view.home.activity.main.event

class MainEvent(val type: Int) {
    var name: String = ""

    constructor(type: Int, name: String) : this(type) {
        this.name = name
    }
}