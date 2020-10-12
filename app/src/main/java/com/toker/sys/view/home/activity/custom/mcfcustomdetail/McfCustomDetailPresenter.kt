package com.toker.sys.view.home.activity.custom.mcfcustomdetail

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import org.json.JSONObject

/**
 * @author yyx
 */

class McfCustomDetailPresenter : BasePresenter<McfCustomDetailContract.View>(), McfCustomDetailContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, FCustomDetailTBean.Bean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showData(toBean.data)
                }
            }

            2, 3, 4 -> {
                val toBean = GsonUtil.GsonToBean(toJson, Bean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showSuccess(url_type)
                }
            }

        }
    }

    override fun loadRepositories() {
        reqData(1)
    }

    override fun invalidReason() {
        reqData(2)
    }

    override fun customerFollow() {
        reqData(3)
    }

    override fun customerRevocation() {
        reqData(4)
    }


    data class Bean(
        val `data`: Any,
        val code: String,
        val desc: String
    ) {
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }
}