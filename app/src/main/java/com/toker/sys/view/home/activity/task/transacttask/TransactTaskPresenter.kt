package com.toker.sys.view.home.activity.task.transacttask

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.task.bean.TransactABean
import com.toker.sys.view.home.activity.task.bean.TransactList
import com.toker.sys.view.home.activity.task.bean.TransactTaskBean
import org.json.JSONObject

/**
 * @author yyx
 */

class TransactTaskPresenter : BasePresenter<TransactTaskContract.View>(), TransactTaskContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()

        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, TransactTaskBean.TransactBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showData(toBean.data)
                }
            }
            2->{
                val toBean = GsonUtil.GsonToBean(toJson, TransactABean.TransactBBean::class.java)
                if (toBean!!.isSuccess()){
                    mView?.showTBeanList(toBean.data)
                }
            }
            3->{
                val toBean = GsonUtil.GsonToBean(toJson, TransactList.TransactListBean::class.java)
                if (toBean!!.isSuccess()){
                    mView?.showTBeanLists(toBean.data)
                }
            }
            else -> {
            }
        }


    }

    override fun loadRepositories() {
        reqData(1)
        getEventTaskTraceList()
    }
    override fun getEventTaskTraceList() {
        reqData(2)
    }

    override fun taskReportList() {

        reqData(3)
    }
}