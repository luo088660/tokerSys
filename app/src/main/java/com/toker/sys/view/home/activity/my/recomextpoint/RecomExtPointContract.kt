package com.toker.sys.view.home.activity.my.recomextpoint

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView

/**
 * @author yyx
 */

object RecomExtPointContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
