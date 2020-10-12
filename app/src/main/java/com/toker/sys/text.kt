package com.toker.sys

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/**
 * packageName: com.toker.sys
 * author: star
 * created on: 2019/11/19 18:04
 * description:
 */

fun main() {
    val bb4 = 1051f//长度单位是1051米。。
    val cc = bb4 / 100 //得到10.51==
    val d = cc.roundToInt()//四舍五入是11
    val e = d / 10.toFloat()//把10 也强转为float型的，再让10除以它==
    println("$e 公里")//1.1公里
    val time = 1581492225525
    val time1 = 1581481483217-1000
    val millis = 1581480801159
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

    val format1 = sdf.format(Date(time))
    val format11 = sdf.format(Date(time1))
    val format2 = sdf.format(Date(millis))

    println("format1---$format1")
    println("format11---$format11")
    println("format2---$format2")
}