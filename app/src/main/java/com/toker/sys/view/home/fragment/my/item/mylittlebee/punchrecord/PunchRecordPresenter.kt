package com.toker.sys.view.home.fragment.my.item.mylittlebee.punchrecord

import android.os.Bundle
import com.google.gson.Gson
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.task.bean.PunchRecordBean
import org.json.JSONObject

/**
 * @author yyx
 */

class PunchRecordPresenter : BasePresenter<PunchRecordContract.View>(), PunchRecordContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()

        val toBean = GsonUtil.GsonToBean(toJson, PunchRecordBean.PunchRBean::class.java)
        if (toBean?.isSuccess()!!) {
            mView?.showSuccesData(toBean.data)
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }
}