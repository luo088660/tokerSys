package com.toker.sys.view.home.activity.task.transacttask

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.task.bean.TransactABean
import com.toker.sys.view.home.activity.task.bean.TransactList
import com.toker.sys.view.home.activity.task.bean.TransactTaskBean

/**
 * @author yyx
 */

object TransactTaskContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: TransactTaskBean.Data)
        fun showTBeanList(pageData: MutableList<TransactABean.Data>)
        fun showTBeanLists(data: MutableList<TransactList.Data>)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun taskReportList()
        fun getEventTaskTraceList()
    }
}
