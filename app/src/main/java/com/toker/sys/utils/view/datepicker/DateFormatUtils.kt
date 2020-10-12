package com.toker.sys.utils.view.datepicker

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 说明：日期格式化工具
 * 作者：liuwan1992
 * 添加时间：2018/12/17
 * 修改人：liuwan1992
 * 修改时间：2018/12/18
 */
object DateFormatUtils {

    val DATE_FORMAT_PATTERN_Y = "yyyy"
    val DATE_FORMAT_PATTERN_MM = "MM"
    private val DATE_FORMAT_PATTERN_DAY = "dd"
    val DATE_FORMAT_PATTERN_YM = "yyyy-MM"
    val DATE_FORMAT_PATTERN_YMD = "yyyy-MM-dd"
    private val DATE_FORMAT_PATTERN_YMD_HM = "yyyy-MM-dd HH:mm"

    /**
     * 时间戳转字符串
     *
     * @param timestamp     时间戳
     * @param isPreciseTime 是否包含时分
     * @return 格式化的日期字符串
     */

    fun long2Str(timestamp: Long, isPreciseTime: Boolean, timeType: Int): String {
        return long2Str(timestamp, getFormatPattern(isPreciseTime, timeType))
    }

    private fun long2Str(timestamp: Long, pattern: String): String {
        return SimpleDateFormat(pattern, Locale.CHINA).format(Date(timestamp))
    }
    /**
     * 获取当前时间
     */
    @SuppressLint("SimpleDateFormat")
    fun currentTime(timeType: Int) :String {
        val sdf = SimpleDateFormat(
            when (timeType) {
                1 -> DATE_FORMAT_PATTERN_Y
                2 -> DATE_FORMAT_PATTERN_YM
                else -> DATE_FORMAT_PATTERN_YMD
            }
        )
        return sdf.format(Date())
    }

    /**
     * 字符串转时间戳
     *
     * @param dateStr       日期字符串
     * @param isPreciseTime 是否包含时分
     * @return 时间戳
     */
    fun str2Long(dateStr: String, isPreciseTime: Boolean): Long {
        return str2Long(dateStr, getFormatPattern(isPreciseTime))
    }

    private fun str2Long(dateStr: String, pattern: String): Long {
        try {
            return SimpleDateFormat(pattern, Locale.CHINA).parse(dateStr).time
        } catch (ignored: Throwable) {
        }
        return 0
    }

    private fun getFormatPattern(showSpecificTime: Boolean, timeType: Int): String {
        return if (showSpecificTime) {
            DATE_FORMAT_PATTERN_YMD_HM
        } else {
            when (timeType) {
                1 -> DATE_FORMAT_PATTERN_Y
                2 -> DATE_FORMAT_PATTERN_YM
                else -> DATE_FORMAT_PATTERN_YMD
            }
        }
    }

    private fun getFormatPattern(showSpecificTime: Boolean): String {
        return if (showSpecificTime) {
            DATE_FORMAT_PATTERN_YMD_HM
        } else {
            DATE_FORMAT_PATTERN_YMD
        }
    }

}
