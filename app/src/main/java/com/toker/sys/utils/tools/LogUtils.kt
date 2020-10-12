package com.toker.sys.utils.tools

import android.util.Log

/**
 * Created by you on 2016/10/2.
 */

object LogUtils {

    var UPLOAD_LOG_VIA_EXCEPT = true

    private val TAG = "SYS"

    fun e(tag: String, msg: String) {

        if (UPLOAD_LOG_VIA_EXCEPT) {
            Log.e(tag, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (UPLOAD_LOG_VIA_EXCEPT) {
            Log.i(tag, msg)
        }
    }
    @JvmStatic
    fun d(tag: String, msg: String) {
        if (UPLOAD_LOG_VIA_EXCEPT) {
            Log.d(tag, msg)
        }
    }

    fun v(tag: String, msg: String) {
        if (UPLOAD_LOG_VIA_EXCEPT) {
            Log.v(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (UPLOAD_LOG_VIA_EXCEPT) {
            Log.w(tag, msg)
        }
    }

    fun e(msg: String) {
        e(TAG, msg)
    }

    fun i(msg: String) {
        i(TAG, msg)
    }

    fun d(msg: String) {
        d(TAG, msg)
    }


    fun w(msg: String) {
        w(TAG, msg)
    }

    fun e(tag: String, e: Throwable) {
        if (UPLOAD_LOG_VIA_EXCEPT) {
            Log.e(tag, Log.getStackTraceString(e))
        }
    }

    fun e(e: Throwable) {
        e(TAG, e)
    }

}
