package com.toker.sys.view.home.activity.my.mylittlebee

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView

/**
 * @author yyx
 */

object MyLittleBeeContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
