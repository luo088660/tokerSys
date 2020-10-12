package com.toker.sys.view.home.fragment.my.item.mylittlebee.extenrecord

import android.os.Bundle
import com.google.gson.Gson
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.task.bean.ExtenRecordBean
import org.json.JSONObject

/**
 * @author yyx
 */

class ExtenRecordPresenter : BasePresenter<ExtenRecordContract.View>(), ExtenRecordContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val tojson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(tojson, ExtenRecordBean.ExtenRBean::class.java)
        if (toBean?.isSuccess()!!) {
            mView?.showSuccesData(toBean.data)
        }

    }

    override fun loadRepositories() {
        reqData(1)
    }
}