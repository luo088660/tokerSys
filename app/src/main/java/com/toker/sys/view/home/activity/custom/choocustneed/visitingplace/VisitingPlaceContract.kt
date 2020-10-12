package com.toker.sys.view.home.activity.custom.choocustneed.visitingplace

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.custom.bean.VisiPlaceBean

/**
 * @author yyx
 */

object VisitingPlaceContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: MutableList<VisiPlaceBean.Data>)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
