package com.toker.sys.utils.tools

val time = "2019-01-30"

fun main() {
    val url = "url=/attendance/noSendTraceMsg#traceId=0QXX1PNGQRK1FUMQAE1URSMTE8;tableTag=2019-10-31"

    val indexOf = url.indexOf("#")
    val substring = url.substring(0,indexOf)

    println("substring--$substring")
    val urlreplace = url.replace(substring, "")

    val replace = substring.replace("url=", "")
    println("replace--$replace")
    println("urlreplace--$urlreplace")

    val indexOf1 = urlreplace.indexOf(";")
    val substring1 = urlreplace.substring(0,indexOf1).replace("traceId=","")
    val substring2 = urlreplace.substring(indexOf1).replace("tableTag=","")

    println("substring1--$substring1")
    println("substring2--$substring2")

}