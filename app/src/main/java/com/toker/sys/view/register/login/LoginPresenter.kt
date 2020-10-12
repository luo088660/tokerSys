package com.toker.sys.view.register.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.toker.sys.AppApplication
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.network.bean.LoginBean
import com.toker.sys.utils.network.bean.PhoneIdBean
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.utils.tools.GsonUtil.GsonToBean
import org.json.JSONObject
import java.lang.Exception

/**
 * @author yyx
 */

class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {


                val logBean = GsonToBean(toJson, LoginBean::class.java)
                if (logBean!!.isSuccess()) {
                    AppApplication.TYPE = logBean!!.data.position
                    AppApplication.USERNAME = logBean!!.data.username
                    AppApplication.PHONE = logBean!!.data.phone
                    try {
                        AppApplication.COMPANY = logBean!!.data.company
                        AppApplication.ICON = logBean!!.data.icon
                    } catch (e: Exception) {
                        AppApplication.ICON = ""
                    }
                    AppApplication.USERID = logBean!!.data.userId
                    val msg = logBean!!.code
                    val status = jsonObject.optInt("status")
                    mView?.onSuccessData(url_type, load_type, msg, status)
                } else {
                    mView?.showonError(logBean.code,logBean.desc)
                }
            }
            else -> {
                val toBean = GsonUtil.GsonToBean(toJson, PhoneIdBean.PhoneBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showSuccessId()
                }


            }
        }

    }

    override fun loadLogin() {
        reqData(2)
    }

    override fun loadRepositories() {
        reqData(1)
    }

    override fun textChagerPassWd(): TextWatcher? {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mView?.userNameChange()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        }


    }

    override fun textChagerUserName(): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                mView?.userNameChange()
            }
        }

    }
}