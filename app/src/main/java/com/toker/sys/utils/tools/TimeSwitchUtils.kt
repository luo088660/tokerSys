package com.toker.sys.utils.tools

import java.util.*

class TimeSwitchUtils :TimeSwitchListener {
    private var year = 2019
    private var month = 1
    private var day = 1
    private var monthTotal = 0
    init {
        year = getYear()
        month = getMonth()
        day = getDay()
        monthTotal = getMonthDays(year, month)
    }
    fun thatTime(time:String):String{
        year  = time.substring(0,4).toInt()
        month  = time.substring(5,7).toInt()
        day  = time.substring(8,10).toInt()
        return  time
    }
    fun thisTime():String{
        return  return  "${year}-${if (month<10)"0$month" else month}-${if (day<10)"0$day" else day}"
    }
    //前一天
    override fun imgTimeLeft():String {
        day -= 1
        if (day<1){
            month -= 1
            if (month<1){
                year -=1
                month = 12
            }
            monthTotal = getMonthDays(year, month)
            day = monthTotal

        }
        return  "${year}-${if (month<10)"0$month" else month}-${if (day<10)"0$day" else day}"
    }
    //后一天
    override fun imgTimeRight():String  {
        day += 1
        if (day > monthTotal) {
            month += 1
            if (month > 12) {
                year += 1
                month = 1
            }
            day = 1
            monthTotal = getMonthDays(year, month)
        }
        return  "${year}-${if (month<10)"0$month" else month}-${if (day<10)"0$day" else day}"

    }

    /**
     * 通过-份和-份 得到当-的子
     *
     * @param year
     * @param month
     * @return
     */
    public fun getMonthDays(year: Int, month: Int): Int {
        var month1 = month;
        month1++
        when (month) {
            1, 3, 5, 7, 8, 10, 12 -> return 31
            4, 6, 9, 11 -> return 30
            2 -> {
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            }
            else -> {
                return -1
            }
        }
    }

    /**
     * 获取-
     * @return
     */
    public fun getYear(): Int {
        val cd = Calendar.getInstance();
        return cd.get(Calendar.YEAR);
    }

    /**
     * 获取-
     * @return
     */
    public fun getMonth(): Int {
        val cd = Calendar.getInstance();
        return cd.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取
     * @return
     */
    public fun getDay(): Int {
        val cd = Calendar.getInstance();
        return cd.get(Calendar.DATE);
    }
}