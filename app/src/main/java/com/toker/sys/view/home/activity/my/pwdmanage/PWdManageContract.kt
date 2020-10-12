package com.toker.sys.view.home.activity.my.pwdmanage

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView

/**
 * @author yyx
 */

object PWdManageContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showSeccess()
        fun showError(desc: String)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
