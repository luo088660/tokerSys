package com.toker.sys

import android.app.Application
import android.support.multidex.MultiDex
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.lzy.okgo.https.HttpsUtils.UnSafeHostnameVerifier
import com.toker.sys.http.EasyHttp
import com.toker.sys.http.cache.converter.SerializableDiskConverter
import com.toker.sys.http.model.HttpHeaders
import com.toker.sys.http.model.HttpParams
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.BuglyLog
import com.tencent.bugly.crashreport.CrashReport
import com.toker.sys.http.utils.FileUtils
import com.toker.sys.utils.tools.LogUtils

class AppApplication : Application() {


    companion object {
        var LOCATIONNUM: String = "5"
        var TYPE: String = ""
        var PHONE: String = ""
        var COMPANY: String = ""
        var ICON: String = ""
        var USERNAME: String = ""
        var ADDRESS: String = ""
        var LATITUDE: String = ""
        var LONGITUDE: String = ""
        var USERID: String = ""
        var REGISTRATION_ID: String = ""
        val MAP_KEY = "wmY9WtlqhXawvYMGwQcINQ8ijolcObdh"


        private var instance: AppApplication? = null
        @JvmStatic
        fun getInstance(): AppApplication {
            return instance!!
        }

        @JvmField
        var TokenId: String? = ""
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this)
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL)
        //初始化 极光推送
        JPushInterface.setDebugMode(true)    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this)            // 初始化 JPush

        REGISTRATION_ID = JPushInterface.getRegistrationID(this)
        LogUtils.e("AppApplication", "registrationId:$REGISTRATION_ID ")
        try {
            CrashReport.initCrashReport(applicationContext, "dcfbe26499", false)
            Beta.checkUpgrade(false, true)
            BuglyLog.setCache(1024 * 15)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        EasyHttp.init(this)


        //设置请求头
        val headers = HttpHeaders()
        //设置请求参数
        val params = HttpParams()
//        params.put("appId", AppConstant.APPID)
        EasyHttp.getInstance()
            .debug("Http", true)
//            .debug("Http", BuildConfig.DEBUG)
            .setReadTimeOut(60 * 1000)
            .setWriteTimeOut(60 * 1000)
            .setConnectTimeout(60 * 1000)
            .setRetryCount(3)//默认网络不好自动重试3次
            .setRetryDelay(500)//每次延时500ms重试
            .setRetryIncreaseDelay(500)//每次延时叠加500ms
//            .setBaseUrl(Url)
            .setCacheDiskConverter(SerializableDiskConverter())//默认缓存使用序列化转化
            .setCacheMaxSize(50 * 1024 * 1024)//设置缓存大小为50M
            .setCacheVersion(1)//缓存版本为1
//            .setHostnameVerifier(UnSafeHostnameVerifier(Url))//全局访问规则
            .setCertificates()//信任所有证书
            //.addConverterFactory(GsonConverterFactory.create(gson))//本框架没有采用Retrofit的Gson转化，所以不用配置
            .addCommonHeaders(headers)//设置全局公共头
            .addCommonParams(params)//设置全局公共参数
//            .addInterceptor(CustomSignInterceptor())//添加参数签名拦截器
        //.addInterceptor(new HeTInterceptor());//处理自己业务的拦截器


    }


}