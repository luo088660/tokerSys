package com.toker.sys.view.home.fragment.my.item.mylittlebee.extenrecord

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.task.bean.ExtenRecordBean

/**
 * @author yyx
 */

object ExtenRecordContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showSuccesData(data: ExtenRecordBean.Data)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
