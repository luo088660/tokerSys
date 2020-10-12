package com.toker.sys.utils.tools

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider

import java.io.File
import android.telephony.TelephonyManager
import android.app.Activity
import android.provider.Settings


/**
 * 兼容android 7.0以上获取uri异常的工具类
 */
object FileProvider7 {

    /**
     * 获取uri
     *
     * @param context
     * @param file
     * @return
     */
    fun getUriForFile(context: Context, file: File): Uri? {
        var fileUri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = getUriForFile24(context, file)
        } else {
            fileUri = Uri.fromFile(file)
        }
        return fileUri
    }

    /**
     * android 7.0以上获取uri的方法
     *
     * @param context
     * @param file
     * @return
     */
    private fun getUriForFile24(context: Context, file: File): Uri {

        return FileProvider.getUriForFile(
            context, "com.toker.sys.fileProvider",
            file
        )
    }
    /**
     * 通过WiFiManager获取mac地址
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds", "MissingPermission")
    fun tryGetWifiMac(context:Context):String? {
        val androidID =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return androidID
    }
    @SuppressLint("MissingPermission", "HardwareIds")
    fun getIMEI(ctx: Context): String? {

        val tm = ctx.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
        return tm?.deviceId
    }

    @Throws(Exception::class)
     fun getVersionName(context:Context): String {
        // 获取packagemanager的实例
        val packageManager = context.packageManager
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        val packInfo = packageManager.getPackageInfo(context.packageName, 0)
        return packInfo.versionName
    }
}

