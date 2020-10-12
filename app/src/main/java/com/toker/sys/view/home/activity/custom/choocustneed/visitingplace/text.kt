package com.toker.sys.view.home.activity.custom.choocustneed.visitingplace

import java.util.regex.Pattern

//bean:ChCustStatuEvent(type=1, productType=洋房,商铺, productAcreage=60~80㎡,120~140㎡, houseType=二室,五室及以上, purchaseFocus=学区,自然生态环境,物业服务)
fun main() {

    var productType="洋房,商铺"
    var beanType = mutableListOf<String>()
    var i1 = productType.indexOf(",")
    val i = appearNumber(productType, ",")
    for (i2 in 0 until i) {
        println("i2--${i2}")
        val substring = productType?.substring(0, i1)!!
        beanType.add(substring)
        println("$i---$substring")
        //                m1Bitmaps.put(String.valueOf(i2), substring);
        productType = productType?.substring(i1 + 1, productType.length)
        i1 = productType!!.indexOf(",")
    }
    println("$i---$productType")
    beanType.add(productType!!)
//    statuAdapter1.refeshData(beanType)
} /**
 * 获取指定字符串出现的次数
 *
 * @param srcText  源字符串
 * @param findText 要查找的字符串
 * @return
 */
fun appearNumber(srcText: String, findText: String): Int {
    var count = 0
    val p = Pattern.compile(findText)
    val m = p.matcher(srcText)
    while (m.find()) {
        count++
    }
    return count
}
