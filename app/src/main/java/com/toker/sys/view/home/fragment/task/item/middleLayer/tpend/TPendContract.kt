package com.toker.sys.view.home.fragment.task.item.middleLayer.tpend

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.task.bean.Data

/**
 * @author yyx
 */

object TPendContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showApproveCount(data: Any)
        fun targetTaskPage(data: Data)
        fun onSuccess()
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun approveResult()
    }
}
