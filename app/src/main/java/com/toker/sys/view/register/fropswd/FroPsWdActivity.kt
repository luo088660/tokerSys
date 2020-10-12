package com.toker.sys.view.register.fropswd

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.toker.reslib.sm.Text
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.SystemSettImp.Companion.API_SYSTEM_AUTH_GETCODET
import com.toker.sys.utils.network.params.SystemSettImp.Companion.API_SYSTEM_SEND_MESSAGE
import com.toker.sys.utils.tools.CodeUtils
import com.toker.sys.view.register.enternewpwd.EnterNewPwdActivity
import kotlinx.android.synthetic.main.activity_fro_ps_wd.*
import kotlinx.android.synthetic.main.layout_content_title.*

/**
 * 忘记密码
 * @author yyx
 */

class FroPsWdActivity : BaseActivity<FroPsWdContract.View, FroPsWdPresenter>(),
    FroPsWdContract.View {

    override var mPresenter: FroPsWdPresenter = FroPsWdPresenter()


    override fun layoutResID(): Int = R.layout.activity_fro_ps_wd
    private var code = ""
    override fun initView() {
    }

    override fun initData() {
        tv_title.text = "忘记密码"
        img_fro_pswd.setImageBitmap(CodeUtils.instance.createBitmap(this))
//        ed_fro_pswd.setText(CodeUtils.instance.code.toString())
        setOnClickListener(img_fro_pswd)
        setOnClickListener(img_back)
        setOnClickListener(btn_fro_vcode)
        setOnClickListener(btn_fro_ps_commin)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            //获取验证码
            btn_fro_vcode -> {
                if (TextUtils.isEmpty("${ed_fro_phone.text}")) {
                    toast("请输入电话号码")
                    return
                }
                mPresenter.initFroCode(ed_fro_phone.text.toString(), btn_fro_vcode)

            }
            img_fro_pswd -> {
                img_fro_pswd.setImageBitmap(CodeUtils.instance.createBitmap(this))
            }
            //输入新密码 EnterNewPwd
            btn_fro_ps_commin -> {
                Log.d(TAG, "CodeUtils.instance.code.toString().toLowerCase():${CodeUtils.instance.code.toString().toLowerCase()}")
                Log.d(TAG, "ed_fro_pswd:${"${ed_fro_pswd.text}".toLowerCase()}")
                if (CodeUtils.instance.code.toString().toLowerCase() != "${ed_fro_pswd.text}".toLowerCase()) {
                    toast("图形验证码错误")
                    return
                }
                if (TextUtils.isEmpty("${ed_pswd.text}")) {
                    toast("验证码不能为空")
                    return
                }
                if (code != "${ed_pswd.text}") {
                    toast("验证码错误")
                    return
                }

                val intent = Intent(this, EnterNewPwdActivity::class.java)
                intent.putExtra("phone", "${ed_fro_phone.text}")
                intent.putExtra("code", "${ed_pswd.text}")
                startActivity(intent)

            }
            else -> {

            }
        }
    }

    override fun showSuccess(data: String) {
        code = data
    }

    override fun showError(desc: String) {
        toast(desc)
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["phone"] = "${ed_fro_phone.text}"
        return map
    }

    override fun getUrl(url_type: Int): String {
        return API_SYSTEM_SEND_MESSAGE
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {

    }
}
