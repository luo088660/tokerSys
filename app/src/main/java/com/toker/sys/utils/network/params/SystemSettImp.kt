package com.toker.sys.utils.network.params

import com.toker.sys.common.Constants


/**
 * 系统设置
 */
class SystemSettImp{
    companion object{

//        a)测试环境：http://appd.evergrande.com/hdtoke-test/android/version.xml
        // 版本校验；http://appd.evergrande.com/hdtoke-test/test_index.html
        // 版本更新跳转
//        b)生产环境：http://appd.evergrande.com/hdtoke/android/version.xml
        // 版本校验；http://appd.evergrande.com/hdtoke/index.html    版本更新跳转

        //4.1.	APP端
        //版本检测
//        val API_VERSION = "http://appd.evergrande.com/hdtoke-test/android/version.xml"
        val API_VERSION = Constants.API_VERSION_URL
        //版本更新跳转
//        val API_HDTOKE = "http://appd.evergrande.com/hdtoke-test/index.html"
//        val API_HDTOKE = "http://appd.evergrande.com/hdtoke-test/test_index.html"
        val API_HDTOKE = Constants.API_HDTOKE_URL
        //4.1.1.	登陆
        val API_SYSTEM_LOGIN = "auth/login"
        val API_SYSTEM_PHONEID = "auth/setPhoneId"

        //4.1.2.	修改密码
        val API_SYSTEM_UPDATE_PWD = "auth/appUpdatePassword"
        //找回密码
        val API_SYSTEM_reset_Password = "auth/resetPassword"

        //4.1.3.	发送验证码
        val API_SYSTEM_SEND_MESSAGE = "auth/sendCode"

        //4.1.4.	小蜜峰登陆
        val API_SYSTEM_MILOGIN = "miLogin"

        //4.1.5.	小蜜峰注册
        val API_SYSTEM_MIREGIST = "miRegist"

        //4.2.	获取组织机构树
        val API_SYSTEM_ORGANZATION = "getOrganzation"

        //4.3.	获取项目列表
        val API_SYSTEM_PROJECT = "getProject"
        //获取图形验证码
        val API_SYSTEM_AUTH_GETCODET = "auth/getCode"

        //全部项目
        val API_SYSTEM_PROJECT_LIST = "auth/getProjectList"
        val API_SYSTEM_UPDATE_HEADERPIC = "auth/updateHeaderPic"


    }

}










