package com.toker.sys.view.register.splash

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import com.toker.sys.AppApplication
import com.toker.sys.common.Constants
import com.toker.sys.http.EasyHttp
import com.toker.sys.http.callback.ProgressDialogCallBack
import com.toker.sys.http.exception.ApiException
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.network.bean.LoginBean
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.tools.Tool
import com.toker.sys.view.home.bean.SplashBean
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.lang.Exception

/**
 * @author yyx
 */

class SplashPresenter : BasePresenter<SplashContract.View>(), SplashContract.Presenter {

    private val WHAT_DELAY = 0x11// 启动页的延时跳转
    private val DELAY_TIME = 1500// 延时时间
    var bean: SplashBean = SplashBean()
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val logBean = GsonUtil.GsonToBean(jsonObject.toString(), LoginBean::class.java)
        if (logBean!!.isSuccess()) {
            AppApplication.TYPE = logBean!!.data.position
            AppApplication.USERNAME = logBean!!.data.username
            AppApplication.PHONE = logBean!!.data.phone
            AppApplication.ICON = if (!TextUtils.isEmpty(logBean!!.data.icon))logBean!!.data.icon else ""
            AppApplication.COMPANY = if (!TextUtils.isEmpty(logBean!!.data.company))logBean!!.data.company else ""
            AppApplication.USERID = logBean!!.data.userId
            val msg = logBean!!.code
            val status = jsonObject.optInt("status")
            mView?.onSuccessData(url_type, load_type, msg, status)
        } else {
            mView?.onError(url_type, load_type, logBean.desc)
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }


    /**
     * 检测更新
     */
    override fun uploadTaskRecord() {
        val url = "${SystemSettImp.API_VERSION}?time=${System.currentTimeMillis()}"
        EasyHttp.get(url)
            .sign(true)
            .accessToken(true)
            .timeStamp(true)
            .execute(object : ProgressDialogCallBack<String>(mView?.showDialog(), true, true) {
                override fun onError(e: ApiException?) {
                    super.onError(e)
                    LogUtils.d(TAG, "e.message: ${e?.message}")
                }

                override fun onSuccess(t: String) {
                    LogUtils.d(TAG, "onSuccess-->t:$t ")
                    var version = ""
                    var content = ""
                    LogUtils.d("dataEcb:---->")
                    val instance = XmlPullParserFactory.newInstance()
                    val parser = instance.newPullParser()
                    parser.setInput(StringReader(t))
                    var type = parser.eventType
                    while (type != XmlPullParser.END_DOCUMENT) {
//
//
                        val name = parser.name
                        Log.d(TAG, "name:$name ");
                        when (type) {
                            XmlPullParser.START_TAG -> {
                                if ("version" == name) {
                                    version = parser.nextText()//parser.getAttributeValue(null, "version")

                                }
                                if ("content" == name) {
                                    content = parser.nextText()//parser.getAttributeValue(null, "content")
                                }
                            }
                            XmlPullParser.END_TAG -> {
                                Log.d(TAG, "version:$version ");
                                Log.d(TAG, "content:$content ");
                                mView?.setVersion(version,content)
                            }
                        }
                        type = parser.next();

                    }
                }
            })
    }

    override fun getLogin(mContext: Context) {
        val unama = Tool.getValue(mContext, Constants.USERNAME)
        val pwd = Tool.getValue(mContext, Constants.PWD)
        bean.name = unama
        bean.pwd = pwd

        handler.sendEmptyMessageDelayed(WHAT_DELAY, DELAY_TIME.toLong())
    }

    // 创建Handler对象，处理接收的消息
    @SuppressLint("HandlerLeak")
    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                WHAT_DELAY// 延时3秒跳转
                -> mView?.Login(bean)
            }
        }
    }
}