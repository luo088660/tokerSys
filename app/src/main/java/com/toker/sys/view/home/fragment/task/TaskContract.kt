package com.toker.sys.view.home.fragment.task

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.bean.Data

/**
 * @author yyx
 */

object TaskContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showProjectDate(data: MutableList<Data>)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
