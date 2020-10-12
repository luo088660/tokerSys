package com.toker.sys.view.home.fragment.sheet.item.currenttask

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.bean.STExtenRankBean
import com.toker.sys.view.home.fragment.task.bean.TEntireBean
import com.toker.sys.view.home.fragment.task.item.controller.treceived.TReceivedBean
import org.json.JSONObject

/**
 * @author yyx
 */

class CurrentTaskPresenter : BasePresenter<CurrentTaskContract.View>(),
    CurrentTaskContract.Presenter {
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, TReceivedBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.eventTaskPage(toBean.data)
                }
            }
            2 -> {
                val toBean = GsonUtil.GsonToBean(toJson, STExtenRankBean.STExtenBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showDataList(toBean.data)
                }
            }
            else -> {

            }
        }
    }

    override fun loadRepositories() {
        reqData(1)
//        getUserCompanyRank()
    }

    override fun getUserCompanyRank() {
        reqData(2)
    }

}