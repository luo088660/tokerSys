package com.toker.sys.view.home.activity.sheet.waitforcust

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.view.home.bean.ProjectNoBean
import com.toker.sys.view.home.bean.ProjectToBean
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean
import org.json.JSONObject

/**
 * @author yyx
 */

class WaitForCustPresenter : BasePresenter<WaitForCustContract.View>(),
    WaitForCustContract.Presenter {
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val toJson = jsonObject.toString()
        when (url_type) {
            2 -> {
                val toBean = GsonUtil.GsonToBean(toJson, com.toker.sys.view.home.bean.ProjectListBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showListData(toBean.data)
                }
            }
            1 -> {

                val toBean = GsonUtil.GsonToBean(toJson, ProjectToBean.ProjectToABean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showDataA(toBean.data)
                }
            }
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }


    override fun ProjectList() {
        reqData(2)
    }


}