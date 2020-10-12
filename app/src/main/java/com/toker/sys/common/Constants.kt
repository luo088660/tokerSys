package com.toker.sys.common

import com.toker.sys.BuildConfig

class Constants {
    companion object {
        //人员角色

        val THATTIME: String = "thatTime"
        const val PWD = "password"
        const val USERNAME = "username"
        const val RESCUE1 = "1"
        const val RESCUE2 = "2"
        const val RESCUE3 = "4"
        const val RESCUE4 = "3"


        const val TASKTYPE = "taskType"
        const val TYPE = "type"

        const val BEANDATA = "beanData"


        /**
         * 控制服务器URL是否为正式地址 false为正式地址，true为测试地址,打包的时候需要切换
         */
        @JvmField
        val SERVER_DEBUG = BuildConfig.SERVER_DEBUG;
//        val SERVER_DEBUG =true;

        /*----------------debug--------------------------------*/
        val DEBUG_API_SERVER_URL = "https://cstuoke.evergrande.com/hd/"
//        val DEBUG_API_SERVER_URL = "http://2652841pv0.zicp.vip:80/"
        // 版本校验
        val DEBUG_API_VERSION = "http://appd.evergrande.com/hdtoke-test/android/version.xml"
        // 版本更新跳转
        val DEBUG_API_HDTOKE = "http://appd.evergrande.com/hdtoke-test/test_index.html"


        /*-----------------release-----------------------------*/
        val RELEASE_API_SERVER_URL = "https://tuokeapp.evergrande.com/"
        //版本校验；
        val RELEASE_API_VERSION = "http://appd.evergrande.com/hdtoke/android/version.xml"
        // 版本更新跳转
        val RELEASE_API_HDTOKE = "http://appd.evergrande.com/hdtoke/index.html"


        //最终引用的数据请求接口的地址
        var API_SERVER_URL = if (SERVER_DEBUG) DEBUG_API_SERVER_URL else RELEASE_API_SERVER_URL;
        //版本校验；
        var API_VERSION_URL = if (SERVER_DEBUG) DEBUG_API_VERSION else RELEASE_API_VERSION;
        //版本更新跳转
        var API_HDTOKE_URL = if (SERVER_DEBUG) DEBUG_API_HDTOKE else RELEASE_API_HDTOKE;
    }


}


