package com.toker.sys.view.home.fragment.sheet.param

data class SheentTopBean(var name: String){

    var resImg: Int = 0
    var resNum: Int = 0
    constructor( name:String , resImg: Int):this(name){
        this.resImg = resImg
    }
    constructor( name:String , resImg: Int,resNum: Int):this(name,resImg){
        this.resNum = resNum
    }
}