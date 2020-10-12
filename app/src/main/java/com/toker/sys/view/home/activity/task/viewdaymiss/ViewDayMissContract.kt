package com.toker.sys.view.home.activity.task.viewdaymiss

import android.text.TextWatcher
import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.task.bean.ViewDayMissBean

/**
 * @author yyx
 */

object ViewDayMissContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: MutableList<ViewDayMissBean.Data>)
        fun afterTextChanged()
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun textChangerd(): TextWatcher
    }
}
