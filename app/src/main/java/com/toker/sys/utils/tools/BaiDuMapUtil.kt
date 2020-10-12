package com.toker.sys.utils.tools

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object BaiDuMapUtil {
    internal var DEF_PI = 3.14159265359 // PI
    internal var DEF_2PI = 6.28318530712 // 2*PI
    internal var DEF_PI180 = 0.01745329252 // PI/180.0
    internal var DEF_R = 6370693.5 // radius of earth

    //适用于近距离
    fun GetShortDistance(lon1: Double, lat1: Double, lon2: Double, lat2: Double): Double {
        val ew1: Double = lon1 * DEF_PI180
        val ns1: Double = lat1 * DEF_PI180
        val ew2: Double = lon2 * DEF_PI180
        val ns2: Double = lat2 * DEF_PI180
        val dx: Double
        val dy: Double
        var dew: Double
        val distance: Double
        // 角度转换为弧度
        // 经度差
        dew = ew1 - ew2
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
            dew = DEF_2PI - dew
        else if (dew < -DEF_PI)
            dew += DEF_2PI
        dx = DEF_R * cos(ns1) * dew // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2) // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = sqrt(dx * dx + dy * dy)
        return distance
    }

    //适用于远距离
    fun GetLongDistance(lon1: Double, lat1: Double, lon2: Double, lat2: Double): Double {
        val ew1: Double = lon1 * DEF_PI180
        val ns1: Double = lat1 * DEF_PI180
        val ew2: Double = lon2 * DEF_PI180
        val ns2: Double = lat2 * DEF_PI180
        var distance: Double
        // 角度转换为弧度
        // 求大圆劣弧与球心所夹的角(弧度)
        distance = sin(ns1) * sin(ns2) + cos(ns1) * cos(ns2) * cos(ew1 - ew2)
        // 调整到[-1..1]范围内，避免溢出
        if (distance > 1.0)
            distance = 1.0
        else if (distance < -1.0)
            distance = -1.0
        // 求大圆劣弧长度
        distance = DEF_R * acos(distance)
        return distance
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val mLat1 = 23.122519 // point1纬度
        val mLon1 = 113.352798 // point1经度
        val mLat2 = 23.122559 // point2纬度
        val mLon2 = 113.352779// point2经度
        val distance = GetShortDistance(mLon1, mLat1, mLon2, mLat2)
        println(distance.toInt())
        println(DataUtil.StringData())
    }

}