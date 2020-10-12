package com.toker.sys.utils.tools

class CalendarUtils {


    var mSelYear :Int = 0
    var mSelMonth :Int = 0
    var mSelDay :Int = 0


    /**
     * @param flag  0：正常初始化日期 1：为左右点击的初始化日期
     */
    private fun init( year:Int, month:Int, day:Int, flag:Int):List<Cell> {
//        if (flag != 0) {
//            day = 1;
//        }
        setSelectYearMonth(year, month, day);
        val listData =  ArrayList<Cell>();

        val mMonthDays = getMonthDays(year, month)


        for (i in 0..mMonthDays){
            var cell =  Cell()
            cell.day = ((i + 1).toString());
            cell.month =  "${(month + 1)}月"
            if (i + 1 == day) {
                cell.setmCurrDay(day)
//                todayPos = day
            }
            listData.add(cell)
        }


        return listData;
    }



    private fun setSelectYearMonth( year:Int, month:Int, day:Int) {
         mSelYear = year
         mSelMonth = month
         mSelDay = day
    }

    /**
     * 通过年份和月份 得到当月的日子
     *
     * @param year
     * @param month
     * @return
     */
    public fun getMonthDays(year: Int, month: Int): Int {
        var month1 = month
        month1++
        return when (month1) {
            1,
            3,
            5,
            7,
            8,
            10,
            12
            -> 31

            4,
            6,
            9,
            11 -> 30
            2 -> {
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    29
                } else {
                    28
                }
            }
            else -> {
                -1
            }
        }

    }
}
