package com.toker.sys.view.home.activity.custom.choocustneed.visitingarea

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.custom.bean.ArriveAreaBean

/**
 * @author yyx
 */

object VisitingAreaContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showArriveAreaData(data: MutableList<ArriveAreaBean.Data>)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
