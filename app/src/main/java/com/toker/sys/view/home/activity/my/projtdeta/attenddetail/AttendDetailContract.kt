package com.toker.sys.view.home.activity.my.projtdeta.attenddetail

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.my.bean.AttenDetailBean

/**
 * @author yyx
 */

object AttendDetailContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showDataList(data: AttenDetailBean.Data)
        fun onSuccessStatus()
        fun showDataError()
        fun showError(desc: String)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun updateAttendanceStatus()
    }
}
