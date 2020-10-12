package com.toker.sys.view.home.activity.task.adminitrandetail

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.task.bean.TransactTaskBean
import org.json.JSONObject

/**
 * @author yyx
 */

class AdminiTranDetailPresenter : BasePresenter<AdminiTranDetailContract.View>(), AdminiTranDetailContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, TransactTaskBean.TransactBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showData(toBean.data)
                }
            }
            else -> {
            }
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }
}