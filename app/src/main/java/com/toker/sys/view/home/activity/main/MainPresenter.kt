package com.toker.sys.view.home.activity.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.TagAliasCallback
import com.toker.sys.AppApplication
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.tools.Tool
import org.json.JSONObject

/**
 * @author yyx
 */

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    var mContext: Context?= null
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        when (url_type) {
            4 -> {
                //上传定位保存下一次定位时间
                mView?.onSuccessData(url_type,load_type,jsonObject.toString(),1)

            }
            else -> {
            }
        }

    }

    override fun loadRepositories() {
        reqData(1)
    }


    override fun uploadLocation() {
        Log.e(TAG, "uploadLocation: ");
        reqData(4)
    }

    override fun setAlias(mContext: Context) {
       this.mContext = mContext
        // 调用 Handler 来异步设置别名
//        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, AppApplication.PHONE))
    }


    private var mAliasCallback: TagAliasCallback = TagAliasCallback { code, alias, tags ->
        val logs: String
        when (code) {
            0 -> {
                logs = "Set tag and alias success"
                LogUtils.i(TAG, logs)
            }
            6002 -> {
                logs = "Failed to set alias and tags due to timeout. Try again after 60s."
                LogUtils.i(TAG, logs)
                // 延迟 60 秒来调用 Handler 设置别名
                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias),(1000 * 60).toLong())
            }
            else -> {
                logs = "Failed with errorCode = $code"
                LogUtils.e(TAG, logs)
            }
        }// 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
//        ExampleUtil.showToast(logs, this)
    }
    private val MSG_SET_ALIAS = 1001
    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_SET_ALIAS -> {
                    LogUtils.d(TAG, "Set alias in handler.")
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(mContext, msg.obj as String, null, mAliasCallback)
                }
                else -> LogUtils.i(TAG, "Unhandled msg - " + msg.what)
            }
        }
    }
}