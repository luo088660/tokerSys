package com.toker.sys.view.register.fropswd

import android.os.Bundle
import android.widget.Button
import biz.kux.emergency.utils.thread.ThreadPoolUtil
import com.toker.sys.R
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.my.bean.PWdManageBean
import org.json.JSONObject

/**
 * @author yyx
 */

class FroPsWdPresenter : BasePresenter<FroPsWdContract.View>(), FroPsWdContract.Presenter {
    private var mCountTime: Int = 0
    var btn_login_vcode: Button? = null
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(toJson, PWdManageBean.PWdManBean::class.java)
        if (toBean?.isSuccess()!!) {
            mView?.showSuccess(toBean.data)
        }else{
            mView?.showError(toBean.desc)
            btn_login_vcode?.isClickable = true
            btn_login_vcode?.setText(R.string.NAxNZldo)
            ThreadPoolUtil.handler.removeMessages(0)
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }

    //获取验证码
    override fun initFroCode(phone: String, btnFroVcode: Button?) {
        onCountDown(btnFroVcode)
        reqData(1)
    }

    //修改btn状态
    private fun onCountDown(btn_login_vcode: Button?) {
        this.btn_login_vcode = btn_login_vcode
        mCountTime = 60
        ThreadPoolUtil.handler.postDelayed(object : Runnable {
            override fun run() {
                mCountTime--
                if (mCountTime < 0) {
                    btn_login_vcode?.isClickable = true
                    btn_login_vcode?.setText(R.string.NAxNZldo)
                    ThreadPoolUtil.handler.removeMessages(0)
                    return
                }
                btn_login_vcode?.isClickable = false
                btn_login_vcode?.text = mCountTime.toString() + "秒"
                ThreadPoolUtil.handler.postDelayed(this, 1000)

            }
        }, 1000)
    }
}