package com.toker.sys.view.home.activity.my.projtdeta.viewshift

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.my.bean.ViewShiftBean

/**
 * @author yyx
 */

object ViewShiftContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun DetailByDay(data: ViewShiftBean.Data)
        fun showTime(data: MutableList<String>)
        fun onErrorData(desc: String)
        fun onErrorVData()
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun showTime()
    }
}
