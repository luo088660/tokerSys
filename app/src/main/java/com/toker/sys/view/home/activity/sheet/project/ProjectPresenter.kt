package com.toker.sys.view.home.activity.sheet.project

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean
import org.json.JSONObject

/**
 * @author yyx
 */

class ProjectPresenter : BasePresenter<ProjectContract.View>(), ProjectContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean =  GsonUtil.GsonToBean(toJson, ProjectListBean.ProjectBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showListData(toBean.data)
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