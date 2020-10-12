package com.toker.sys.view.home.fragment.my.item.mysched

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView

/**
 * @author yyx
 */

object MySchedContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
