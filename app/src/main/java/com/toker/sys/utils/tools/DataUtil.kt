package com.toker.sys.utils.tools

import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*




object DataUtil {
    private var mYear: String? = null
    private var mMonth: String? = null
    private var mDay: String? = null
    private var mWay: String? = null

    fun StringData(): String {
        val c = Calendar.getInstance()
        c.timeZone = TimeZone.getTimeZone("GMT+8:00")
        mYear = c.get(Calendar.YEAR).toString() // 获取当前年份
        mMonth = (c.get(Calendar.MONTH) + 1).toString()// 获取当前月份
        mDay = c.get(Calendar.DAY_OF_MONTH).toString()// 获取当前月份的日期号码
        mWay = c.get(Calendar.DAY_OF_WEEK).toString()
        if ("1" == mWay) {
            mWay = "天"
        } else if ("2" == mWay) {
            mWay = "一"
        } else if ("3" == mWay) {
            mWay = "二"
        } else if ("4" == mWay) {
            mWay = "三"
        } else if ("5" == mWay) {
            mWay = "四"
        } else if ("6" == mWay) {
            mWay = "五"
        } else if ("7" == mWay) {
            mWay = "六"
        }
        return "$mYear-$mMonth-$mDay\t\t星期$mWay"
    }

    /**
     * 根据当前日期获得是星期几
     * time=yyyy-MM-dd
     * @return
     */
    fun getWeek( time:String):String {
        var Week = "";
        val format = SimpleDateFormat("yyyy-MM-dd");
        val c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch ( e:ParseException) {
            e.printStackTrace();
        }

        val  wek=c.get(Calendar.DAY_OF_WEEK);

        if (wek == 1) {
            Week += "星期日";
        }
        if (wek == 2) {
            Week += "星期一";
        }
        if (wek == 3) {
            Week += "星期二";
        }
        if (wek == 4) {
            Week += "星期三";
        }
        if (wek == 5) {
            Week += "星期四";
        }
        if (wek == 6) {
            Week += "星期五";
        }
        if (wek == 7) {
            Week += "星期六";
        }
        return Week;
    }

    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    fun stringToLong(strTime: String?, formatType: String): Long {
        if (strTime.isNullOrEmpty()) {
            return 0
        }
        val date = stringToDate(strTime, formatType) // String类型转成date类型
        return if (date == null) {
            0
        } else {
            dateToLong(date)
        }
    }

    // date要转换的date类型的时间
    fun dateToLong(date: Date): Long {
        return date.getTime()
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    fun longToDate(currentTime: Long, formatType: String): Date {
        val dateOld = Date(currentTime) // 根据long类型的毫秒数生命一个date类型的时间
        val sDateTime = dateToString(dateOld, formatType) // 把date类型的时间转换为string
        return stringToDate(sDateTime, formatType)!!
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    fun stringToDate(strTime: String?, formatType: String): Date? {
        val formatter = SimpleDateFormat(formatType)
        var date: Date? = null
        if (strTime?.isNotEmpty()!!) {
            date = formatter.parse(strTime)
        }
        return date
    }

    // currentTime要转换的long类型的时间
    // formatType要转换的string类型的时间格式
    fun longToString(currentTime: Long, formatType: String): String {
        val date = longToDate(currentTime, formatType) // long类型转成Date类型
        return dateToString(date, formatType)
    }

    // data Date类型的时间
    fun dateToString(data: Date, formatType: String): String {
        return SimpleDateFormat(formatType).format(data)
    }

    /**
     * 获取某月的最后一天
     *
     */
    fun getLastDayOfMonth(year: Int, month: Int): String {
        val cal = Calendar.getInstance()
        //设置年份
        cal.set(Calendar.YEAR, year)
        //设置月份
        cal.set(Calendar.MONTH, month - 1)
        //获取某月最大天数
        val lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay)
        //格式化日期
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        return sdf.format(cal.time)
    }



}

fun main(args: Array<String>) {
//        val sdf2 = SimpleDateFormat("HH:mm")
//        val toLong = DataUtil.stringToLong("2019-07-14 08:00:00", "yyyy-MM-dd HH:mm:ss")
//        println(toLong)
//        val format = sdf2.format(Date(toLong))
//        println(format)
    val month = DataUtil.getLastDayOfMonth(2019, 8)
    println("最后一天$month")
}