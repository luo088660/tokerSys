package com.toker.sys.view.home.fragment.task.item.teamLeader.tcurrent

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.task.bean.TEntireBean
import com.toker.sys.view.home.fragment.task.item.controller.treceived.TReceivedBean
import org.json.JSONObject

/**
 * @author yyx
 */

class TCurrentPresenter : BasePresenter<TCurrentContract.View>(), TCurrentContract.Presenter {
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
                val toBean = GsonUtil.GsonToBean(toJson, TReceivedBean::class.java)
                if (toBean!!.isSuccess()){
                    mView?.eventTaskPage(toBean.data)
                }
            }
            else -> {
            }
        }

    }

    override fun loadRepositories(type: Int) {
        reqData(type)
    }
}