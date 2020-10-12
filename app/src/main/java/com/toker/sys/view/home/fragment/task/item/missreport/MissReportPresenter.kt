package com.toker.sys.view.home.fragment.task.item.missreport

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.task.bean.MissReportBean
import org.json.JSONObject

/**
 * @author yyx
 */

class MissReportPresenter : BasePresenter<MissReportContract.View>(), MissReportContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(toJson, MissReportBean.MissReporBean::class.java)
        if (toBean!!.isSuccess()) {

            mView?.showData(toBean.data)

        }
    }

    override fun loadRepositories() {
        reqData(1)
    }
}