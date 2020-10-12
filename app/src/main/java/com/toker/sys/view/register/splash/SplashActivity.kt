package com.toker.sys.view.register.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.toker.sys.R
import com.toker.sys.dialog.task.SplashDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.tools.FileProvider7
import com.toker.sys.utils.tools.Tool
import com.toker.sys.view.home.activity.main.MainActivity
import com.toker.sys.view.home.activity.task.event.SplashEvent
import com.toker.sys.view.home.bean.SplashBean
import com.toker.sys.view.register.login.LoginActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 启动页面
 * @author yyx
 */

class SplashActivity : BaseActivity<SplashContract.View, SplashPresenter>(), SplashContract.View {

    override var mPresenter: SplashPresenter = SplashPresenter()

    var name = ""
    var pwd = ""
    var deviceId :String?= ""
    override fun layoutResID(): Int = R.layout.activity_splash

    override fun initView() {
        EventBus.getDefault().register(this)
        mPresenter.uploadTaskRecord()
//        mPresenter.getLogin(this)

    }

    //检测更新
    override fun setVersion(version: String, content: String) {
        val versionName = FileProvider7.getVersionName(this)
        Log.d(TAG, "versionName:${versionName.toDouble()}")
        Log.d(TAG, "version:${version.toDouble()}")
        if (version.toDouble() > versionName.toDouble()){
            SplashDialog(this)
        }else{
            mPresenter.getLogin(this)
        }
    }

    //跳转到下载页面
    private fun loadHdtoke(){
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val contenUri = Uri.parse(SystemSettImp.API_HDTOKE)
        intent.data = contenUri
        startActivity(intent)
        finish()

    }
    override fun Login(bean: SplashBean) {
         deviceId = Tool.getValue(this, "deviceId")
        if (bean.name.isNullOrEmpty() || bean.pwd.isNullOrEmpty()||TextUtils.isEmpty(deviceId)) {
            startActivity(Intent(this,LoginActivity::class.java))
        }else{
            name  = bean.name!!
            pwd = bean.pwd!!
            mPresenter.loadRepositories()
        }
    }

    override fun initData() {

    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["account"] = name
        map["password"] =pwd
        map["phoneId"] = deviceId!!
        return map
    }
    override fun getUrl(url_type: Int): String {
        return SystemSettImp.API_SYSTEM_LOGIN
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onError(url_type: Int, load_type: Int, error: String) {
        super.onError(url_type, load_type, error)
        toast(error)
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun SplashEvent(x: SplashEvent){
        when (x.type) {
            1 -> {
                mPresenter.getLogin(this)
            }
            2 -> {
                loadHdtoke()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
