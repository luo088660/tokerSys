package com.toker.sys.view.register.enternewpwd

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.TextureView
import android.view.View
import android.widget.CompoundButton
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.SystemSettImp.Companion.API_SYSTEM_reset_Password
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.register.login.LoginActivity
import kotlinx.android.synthetic.main.activity_enter_new_pwd.*
import kotlinx.android.synthetic.main.activity_fro_ps_wd.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus

/**
 * 忘记密码
 * 输入新密码
 * @author yyx
 */

class EnterNewPwdActivity : BaseActivity<EnterNewPwdContract.View, EnterNewPwdPresenter>(), EnterNewPwdContract.View,
    CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView) {
            cb_new_01 -> {
                //显示密码
                tv_enter_new_02.transformationMethod =
                    if (isChecked) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()

            }
            else -> {

                //显示密码
                tv_enter_new_03.transformationMethod =
                    if (isChecked) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
            }
        }
    }

    override var mPresenter: EnterNewPwdPresenter = EnterNewPwdPresenter()


    override fun layoutResID(): Int = R.layout.activity_enter_new_pwd
    var  phone= ""
    var  code= ""
    override fun initView() {
        tv_title.text = "忘记密码"
        phone = intent.getStringExtra("phone")
        code = intent.getStringExtra("code")
    }

    override fun initData() {
        tv_enter_new_01.text = "手机号码\t${phone}"
        setOnClickListener(img_back)
        setOnClickListener(btn_fro_ps_commin_)
        cb_new_01.setOnCheckedChangeListener(this)
        cb_new_02.setOnCheckedChangeListener(this)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> {
                finish()
            }
            else -> {
                if (TextUtils.isEmpty("${tv_enter_new_02.text}")){
                    toast("用户新密码不能为空")
                    return
                }
                if (TextUtils.isEmpty("${tv_enter_new_03.text}")){
                    toast("用户新密码确认不能为空")
                    return
                }
                if ("${tv_enter_new_02.text}" != "${tv_enter_new_03.text}"){
                    toast("用户新密码确认不相符")
                    return
                }

                mPresenter.loadRepositories()
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
        val map = mutableMapOf<String,String>()
        map["phone"] = phone
        map["code"] = code
        map["newPassword"] = "${tv_enter_new_02.text}"
        map["newPasswordSec"] = "${tv_enter_new_03.text}"
        return map
    }

    override fun getUrl(url_type: Int): String {
        return API_SYSTEM_reset_Password
    }
    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
}
