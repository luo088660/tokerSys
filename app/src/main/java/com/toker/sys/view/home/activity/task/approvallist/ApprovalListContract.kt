package com.toker.sys.view.home.activity.task.approvallist

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.task.bean.Data

/**
 * @author yyx
 */

object ApprovalListContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun targetTaskPage(data: Data)
        fun showProjectDate(data: MutableList<com.toker.sys.view.home.bean.Data>)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun getProjectList()
    }
}
