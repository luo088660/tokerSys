package com.toker.sys.view.home.fragment.custom

import android.os.Bundle
import android.util.Log
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.bean.ProjectListBean
import org.json.JSONObject

/**
 * @author yyx
 */

class CustomPresenter : BasePresenter<CustomContract.View>(), CustomContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(toJson, ProjectListBean::class.java)
        if (toBean!!.isSuccess()) {
            mView?.showProjectDate(toBean.data)
        }


    }

    override fun loadRepositories() {
        reqData(1)
    }
}