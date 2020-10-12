package com.toker.sys.view.home.fragment.task.item.missreport

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.task.bean.MissReportBean

/**
 * @author yyx
 */

object MissReportContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: MissReportBean.Data)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
