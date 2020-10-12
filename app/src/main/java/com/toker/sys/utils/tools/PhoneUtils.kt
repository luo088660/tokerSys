package com.toker.sys.utils.tools

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager

/**
 * Created by WangJinyong on 2018/3/31.
 * 获取SIM卡信息和手机号码
 */

class PhoneUtils(private val context: Context) {

    private val telephonyManager: TelephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    //移动运营商编号
    private var NetworkOperator: String? = null

    //获取sim卡iccid
    val iccid: String
        @SuppressLint("MissingPermission")
        get() {
            var iccid = "N/A"
            iccid = telephonyManager.simSerialNumber
            return iccid
        }

    //获取电话号码
    val nativePhoneNumber: String
        @SuppressLint("MissingPermission")
        get() {
            var nativePhoneNumber = "N/A"
            nativePhoneNumber = telephonyManager.line1Number
            return nativePhoneNumber
        }

    //获取手机服务商信息
    //IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
    //        Flog.d(TAG,"NetworkOperator=" + NetworkOperator);
    //中国移动
    //中国联通
    //中国电信
    val providersName: String
        get() {
            var providersName = "N/A"
            NetworkOperator = telephonyManager.networkOperator
            if (NetworkOperator == "46000" || NetworkOperator == "46002") {
                providersName = "中国移动"
            } else if (NetworkOperator == "46001") {
                providersName = "中国联通"
            } else if (NetworkOperator == "46003") {
                providersName = "中国电信"
            }
            return providersName

        }

    //移动运营商编号
    //移动运营商名称
    val phoneInfo: String
        @SuppressLint("MissingPermission")
        get() {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val sb = StringBuffer()

            sb.append("\nLine1Number = " + tm.line1Number)
            sb.append("\nNetworkOperator = " + tm.networkOperator)
            sb.append("\nNetworkOperatorName = " + tm.networkOperatorName)
            sb.append("\nSimCountryIso = " + tm.simCountryIso)
            sb.append("\nSimOperator = " + tm.simOperator)
            sb.append("\nSimOperatorName = " + tm.simOperatorName)
            sb.append("\nSimSerialNumber = " + tm.simSerialNumber)
            sb.append("\nSubscriberId(IMSI) = " + tm.subscriberId)
            return sb.toString()
        }

    companion object {

        private val TAG = "PhoneUtils"
    }

    /**
     * 隐藏手机号码中间四位
     * @param phone
     * @return
     */
    fun phoneRepla(phone: String): String {
        return phone.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
    }

    /**
     * 隐藏身份证号码中间11位
     * @param phone
     * @return
     */
    fun IDRepla(phone: String): String {
        return phone.replace("(\\d{3})\\d{11}(\\w{4})".toRegex(), "$1***********$2")
    }

}