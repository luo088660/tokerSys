package com.toker.sys.view.register.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.EventLog
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import com.toker.reslib.Main
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.dialog.my.CallToastDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.SystemSettImp.Companion.API_SYSTEM_LOGIN
import com.toker.sys.utils.network.params.SystemSettImp.Companion.API_SYSTEM_PHONEID
import com.toker.sys.utils.tools.FileProvider7
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.tools.Tool
import com.toker.sys.view.home.activity.custom.event.CustomerInvalid
import com.toker.sys.view.home.activity.main.MainActivity
import com.toker.sys.view.register.fropswd.FroPsWdActivity
import com.toker.sys.view.register.useragr.UserAgrActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception


/**
 * @author yyx
 */

class LoginActivity : BaseActivity<LoginContract.View, LoginPresenter>(), LoginContract.View,
    CompoundButton.OnCheckedChangeListener {
    var type = 0
    override var mPresenter: LoginPresenter = LoginPresenter()
    override fun layoutResID(): Int = R.layout.activity_login
    override fun initView() {
        img_back.visibility = GONE
        tv_title.text = "登录"
    }

    var deviceId: String? = ""

    @SuppressLint("MissingPermission", "HardwareIds")
    override fun initData() {
        EventBus.getDefault().register(this)
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermission();
        }

        deviceId = Tool.getValue(this, "deviceId")
        setOnClickListener(tv_useragree_login)
        setOnClickListener(tv_forget_pw_login)
        setOnClickListener(btn_login)
        setOnClickListener(btn_login1)
        setOnClickListener(btn_login2)
        setOnClickListener(btn_login3)
        setOnClickListener(btn_login4)
        setOnClickListener(btn_login5)
        setOnClickListener(btn_login6)
        setOnClickListener(btn_login7)
        setOnClickListener(btn_login8)
        setOnClickListener(cb_useragree)
        et_login_01.addTextChangedListener(mPresenter.textChagerUserName())
        ed_pwd_login.addTextChangedListener(mPresenter.textChagerPassWd())
        cb_login.setOnCheckedChangeListener(this)

        et_login_01.setText(Tool.getValue(this,Constants.USERNAME))
        ed_pwd_login.setText(Tool.getValue(this,Constants.PWD))
    }

    override fun showSuccessId() {
        mPresenter.loadRepositories()
    }

    @SuppressLint("MissingPermission")
    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            //用户协议
            tv_useragree_login -> startActivity(Intent(this, UserAgrActivity::class.java))
            //忘记密码
            tv_forget_pw_login -> startActivity(Intent(this, FroPsWdActivity::class.java))

            btn_login -> {
                if (Build.VERSION.SDK_INT >= 23) {
                    requestPermission();
                }

                type = 0
                val name = et_login_01.text.toString()
                val pwd = ed_pwd_login.text.toString()
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(pwd)) {
                    mPresenter.loadRepositories()
                } else {
                    toast("请输入用户名密码")
                }
            }
            btn_login1 -> {
                type = 1
                mPresenter.loadRepositories()
            }
            btn_login2 -> {
                if (Build.VERSION.SDK_INT >= 23) {
                    requestPermission();
                }

                type = 2
                mPresenter.loadRepositories()
            }
            btn_login3 -> {
                type = 3
                mPresenter.loadRepositories()
            }
            btn_login4 -> {
                type = 4
                mPresenter.loadRepositories()
            }
            btn_login5 -> {
                type = 5
                mPresenter.loadRepositories()
            }
            btn_login6 -> {
                type = 6
                mPresenter.loadRepositories()
            }
            btn_login7 -> {
                type = 7
                mPresenter.loadRepositories()
            }
            btn_login8 -> {
                type = 8
                mPresenter.loadRepositories()
            }
            //用户协议
            cb_useragree -> {
                btnLoginStuta()
            }
        }
    }

    //用户名//密码
    override fun userNameChange() {
        btnLoginStuta()
    }


    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()

        when (type) {
            0 -> {
                map["account"] = et_login_01.text.toString().trim()
            }
            1 -> {
                map["account"] = "adminsys"
            }
            2 -> {
                map["account"] = "15512345675"
            }
            3 -> {
                map["account"] = "15512345676"
            }
            4 -> {
                map["account"] = "15512345677"
            }
            5 -> {
                map["account"] = "15512345678"
            }
            6 -> {
                map["account"] = "14794922100"
            }
            7 -> {
                map["account"] = "13533322218"
            }
            else -> {
                map["account"] = "middle"
            }
        }
        when (url_type) {
            1 -> {

                when (type) {
                    0 -> {
                        map["password"] = ed_pwd_login.text.toString().trim()
                    }
                    1 -> {
                        map["password"] = "123456"
                    }
                    2 -> {
                        map["password"] = "123456"
                    }
                    3 -> {
                        map["password"] = "123456"
                    }
                    4 -> {
                        map["password"] = "123456"
                    }
                    5 -> {
                        map["password"] = "123456"
                    }
                    6 -> {
                        map["password"] = "123456"
                    }
                    7 -> {
                        map["password"] = "123456"
                    }
                    else -> {
                        map["password"] = "123456"
                    }
                }
            }
            else -> {
                map["phoneType"] = "1"
            }
        }
        map["phoneId"] =  deviceId!!
        LogUtils.e(TAG, " map.toString(): $map");
        Tool.commit(this, Constants.USERNAME, "${map["account"]}")
        Tool.commit(this, Constants.PWD, "${map["password"]}")
        return map
    }


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        //显示密码
        ed_pwd_login.transformationMethod =
            if (isChecked) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> {
                API_SYSTEM_LOGIN
            }
            else -> {
                API_SYSTEM_PHONEID
            }
        }
    }


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
        toast("登录成功")
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showonError(code: String, desc: String) {

        when (code) {
            "601" -> {
                CallToastDialog(
                    this, "用户为初次登陆，是否绑定该手机？\n" +
                            "绑定后，该用户不可在其他手机登陆。"
                )
            }
            else -> {
                toast(desc)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun CustomerInvalid(event: CustomerInvalid) {
        when (event.type) {
            100 -> {
                mPresenter.loadLogin()
            }
            else -> {
            }
        }
    }

    override fun onError(url_type: Int, load_type: Int, error: String) {
        super.onError(url_type, load_type, error)

        toast(error)

    }

    /**
     * 添加权限
     */
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA
            ), 0
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            for (i in permissions.indices) {
                if (grantResults[i] != -1) {
                    //T.showShort(mContext,"权限设置成功");
                    try {
//                        deviceId = FileProvider7.getIMEI(this)
                        deviceId = FileProvider7.tryGetWifiMac(this)
                        LogUtils.d(TAG, "tryGetWifiMac:$deviceId ");
                        Tool.commit(this, "deviceId", deviceId!!)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

        }
    }

    /**
     * 修改btn颜色以及状态
     */
    private fun btnLoginStuta() {
        val isStuta =
            cb_useragree.isChecked && "${et_login_01.text}".isNotEmpty() && "${ed_pwd_login.text}".isNotEmpty()
        btn_login.background = resources.getDrawable(
            if (isStuta) R.drawable.btn_bg_login_red_normal else R.drawable.btn_bg_login_normal
        )
        btn_login.isClickable = isStuta
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
