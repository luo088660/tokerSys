package com.toker.sys.view.home.activity.my.pwdmanage

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.CompoundButton
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.tools.Tool
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.register.fropswd.FroPsWdActivity
import com.toker.sys.view.register.login.LoginActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_p_wd_manage.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus

/**
 * 密码管理
 * @author yyx
 */

class PWdManageActivity : BaseActivity<PWdManageContract.View, PWdManagePresenter>(),
    PWdManageContract.View,
    CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when (p0) {
            cb_wd_manage_01 -> {
                et_wd_manage_01.transformationMethod =
                    if (p1) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
            }
            cb_wd_manage_02 -> {
                et_wd_manage_02.transformationMethod =
                    if (p1) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
            }
            else -> {
                et_wd_manage_03.transformationMethod =
                    if (p1) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
            }
        }
    }

    override var mPresenter: PWdManagePresenter = PWdManagePresenter()


    override fun layoutResID(): Int = R.layout.activity_p_wd_manage

    override fun initView() {
        tv_title.text = "修改登录密码"
    }

    override fun initData() {
        setOnClickListener(img_back)
        setOnClickListener(btn_determine)
        setOnClickListener(btn_pwd_forgot)
        cb_wd_manage_01.setOnCheckedChangeListener(this)
        cb_wd_manage_02.setOnCheckedChangeListener(this)
        cb_wd_manage_03.setOnCheckedChangeListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            //忘记密码
            btn_pwd_forgot -> startActivity(Intent(this, FroPsWdActivity::class.java))
            btn_determine -> {

                if (TextUtils.isEmpty("${et_wd_manage_01.text}")) {
                    toast("用户原密码不能为空")
                    return
                }
                if (TextUtils.isEmpty("${et_wd_manage_02.text}")) {
                    toast("用户新密码不能为空")
                    return
                }
                if (TextUtils.isEmpty("${et_wd_manage_03.text}")) {
                    toast("用户新密码确认不能为空")
                    return
                }
                if ("${et_wd_manage_03.text}" != "${et_wd_manage_02.text}"){
                    toast("用户新密码确认不相符")
                    return
                }
                mPresenter.loadRepositories()

            }
            else -> {
            }
        }
    }

    override fun showSeccess() {
        toast("修改成功,请重新登录")
        EventBus.getDefault().post(MainEvent(99))
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    override fun showError(desc: String) {
        toast(desc)
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["oldPassword"] = "${et_wd_manage_01.text}"
        map["newPassword"] = "${et_wd_manage_02.text}"
        map["newPasswordSec"] = "${et_wd_manage_03.text}"
        return map
    }


    override fun getUrl(url_type: Int): String {
        return SystemSettImp.API_SYSTEM_UPDATE_PWD
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
}
