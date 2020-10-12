package com.toker.sys.utils.tools

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {

    companion object{

        /**
         * <li>功能描述：时间相减得到天数
         * @param beginDateStr
         * @param endDateStr
         * @return
         * long
         * @author Administrator
         */
        fun getDaySub(beginDateStr: Long): Int {
            var day = 0;
            val format = SimpleDateFormat("yyyy-MM-dd");
            var beginDate: Date? = null;
            var endDate: Date? = null;
            try {
                day = ((System.currentTimeMillis() - beginDateStr) / (24 * 60 * 60 * 1000)).toInt();
                //System.out.println("相隔的天数="+day);
            } catch (e: ParseException) {
                // TODO 自动生成 catch 块
                e.printStackTrace();
            }
            return day
        }
    }
}