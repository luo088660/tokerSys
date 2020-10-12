package com.toker.sys.view.home.activity.task.adminitrandetail

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.task.bean.TransactTaskBean

/**
 * @author yyx
 */

object AdminiTranDetailContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: TransactTaskBean.Data)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
