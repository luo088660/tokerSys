package com.toker.sys.view.home.activity.sheet.stprojectran

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.bean.STExtenRankBean

/**
 * @author yyx
 */

object STProjectRanContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showListData(data: STExtenRankBean.Data)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
