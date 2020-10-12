package com.toker.sys.view.home.activity.task.approvallist

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.bean.ProjectListBean
import com.toker.sys.view.home.fragment.task.bean.TEntireBean
import org.json.JSONObject

/**
 * @author yyx
 */

class ApprovalListPresenter : BasePresenter<ApprovalListContract.View>(), ApprovalListContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, TEntireBean::class.java)
                if (toBean!!.isSuccess()){
                    mView?.targetTaskPage(toBean.data)
                }
            }
            2->{
                val toBean = GsonUtil.GsonToBean(toJson, ProjectListBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showProjectDate(toBean.data)
                }

            }
            else -> {
            }
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }
    override fun getProjectList() {
        reqData(2)
    }
}