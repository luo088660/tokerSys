package com.toker.sys.jpush

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Looper
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.widget.Toast

import java.util.regex.Matcher
import java.util.regex.Pattern

import cn.jpush.android.api.JPushInterface
import cn.jpush.android.helper.Logger

object ExampleUtil {
    val PREFS_NAME = "JPUSH_EXAMPLE"
    val PREFS_DAYS = "JPUSH_EXAMPLE_DAYS"
    val PREFS_START_TIME = "PREFS_START_TIME"
    val PREFS_END_TIME = "PREFS_END_TIME"
    val KEY_APP_KEY = "JPUSH_APPKEY"
    /**
     * 只能以 “+” 或者 数字开头；后面的内容只能包含 “-” 和 数字。
     */
    private val MOBILE_NUMBER_CHARS = "^[+0-9][-0-9]{1,}$"

    fun isEmpty(s: String?): Boolean {
        if (null == s)
            return true
        if (s.length == 0)
            return true
        return if (s.trim { it <= ' ' }.length == 0) true else false
    }

    fun isValidMobileNumber(s: String): Boolean {
        if (TextUtils.isEmpty(s)) return true
        val p = Pattern.compile(MOBILE_NUMBER_CHARS)
        val m = p.matcher(s)
        return m.matches()
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    fun isValidTagAndAlias(s: String): Boolean {
        val p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_!@#$&*+=.|]+$")
        val m = p.matcher(s)
        return m.matches()
    }

    // 取得AppKey
    fun getAppKey(context: Context): String? {
        var metaData: Bundle? = null
        var appKey: String? = null
        try {
            val ai = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            )
            if (null != ai)
                metaData = ai.metaData
            if (null != metaData) {
                appKey = metaData.getString(KEY_APP_KEY)
                if (null == appKey || appKey.length != 24) {
                    appKey = null
                }
            }
        } catch (e: NameNotFoundException) {

        }

        return appKey
    }

    // 取得版本号
    fun GetVersion(context: Context): String {
        try {
            val manager = context.packageManager.getPackageInfo(
                context.packageName, 0
            )
            return manager.versionName
        } catch (e: NameNotFoundException) {
            return "Unknown"
        }

    }

    fun showToast(toast: String, context: Context) {
        Thread(Runnable {
            Looper.prepare()
            Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
            Looper.loop()
        }).start()
    }

    fun isConnected(context: Context): Boolean {
        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = conn.activeNetworkInfo
        return info != null && info.isConnected
    }

    @SuppressLint("MissingPermission")
    fun getImei(context: Context, imei: String): String? {
        var ret: String? = null
        try {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            ret = telephonyManager.deviceId
        } catch (e: Exception) {
            Logger.e(ExampleUtil::class.java.simpleName, e.message)
        }

        return if (isReadableASCII(ret)) {
            ret
        } else {
            imei
        }
    }

    private fun isReadableASCII(string: CharSequence?): Boolean {
        if (TextUtils.isEmpty(string)) return false
        try {
            val p = Pattern.compile("[\\x20-\\x7E]+")
            return p.matcher(string).matches()
        } catch (e: Throwable) {
            return true
        }

    }

    fun getDeviceId(context: Context): String {
        return JPushInterface.getUdid(context)
    }
}
