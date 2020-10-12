package com.toker.sys

import org.junit.Test
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Test {

    @Test
    fun main() {
        print(System.currentTimeMillis())


//        val daySub = getDaySub("1564725175000", "1574725175000");
//        println(daySub);
//        System.currentTimeMillis()

    }

    /**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long
     * @author Administrator
     */
    fun getDaySub(beginDateStr: String): Long {
        var day = 0;
        val format = SimpleDateFormat("yyyy-MM-dd");
        var beginDate: Date? = null;
        var endDate: Date? = null;
        try {
            beginDate = format.parse(beginDateStr);
//            endDate = format.parse(endDateStr);
            day = ((System.currentTimeMillis() - beginDate.time) / (24 * 60 * 60 * 1000)).toInt();
            //System.out.println("相隔的天数="+day);
        } catch (e: ParseException) {
            // TODO 自动生成 catch 块
            e.printStackTrace();
        }
        return day as Long;
    }
}

fun main() {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val sdf1 = SimpleDateFormat("mm:ss")
    val lon1 = 1576726211403
    val lon2 = 1576726811403+600000*6
//    val lon2 = 1576726812383
    val format = sdf.format(Date(lon1))
    val format1 = sdf.format(Date(lon2))
    val message = lon2 - lon1
    println(message)
    val format2 = sdf1.format(Date(message))
    println(format2)
    println(format)
    println(format1)
    println(System.currentTimeMillis())
}