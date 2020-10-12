package com.toker.sys.view.home.fragment.sheet.item.attendtoday

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.my.item.myatten.bean.MeAttenBean

/**
 * @author yyx
 */

object AttendTodayContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: MeAttenBean.Data)
        fun onShowError(desc: String)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
