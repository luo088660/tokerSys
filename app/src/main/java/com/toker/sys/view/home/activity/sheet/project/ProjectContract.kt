package com.toker.sys.view.home.activity.sheet.project

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean

/**
 * @author yyx
 */

object ProjectContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showListData(data: MutableList<ProjectListBean.Data>)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
