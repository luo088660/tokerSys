package com.toker.sys.view.home.activity.custom.mcfcustomrecom

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import com.toker.sys.view.home.bean.Data

/**
 * @author yyx
 */

object McfCustomRecomContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: FCustomDetailTBean.Data)
        fun showDatarActivate()
        abstract fun showErrorrActivate(desc: String)
        fun showProjectDate(data: MutableList<Data>)
        fun showSuccess(desc: String)
        fun showError(desc: String)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun customerActivate()
        fun customerRecommend()
        fun showProjectDate()
    }
}
