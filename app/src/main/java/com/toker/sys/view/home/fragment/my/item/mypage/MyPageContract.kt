package com.toker.sys.view.home.fragment.my.item.mypage

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.my.item.bean.GroupListBean
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean
import com.toker.sys.view.home.fragment.my.item.bean.ReadCountBean

/**
 * @author yyx
 */

object MyPageContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showListData(data: MutableList<ProjectListBean.Data>)
        fun showDataNumBer(data: ReadCountBean.Data)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()

        fun ProjectList()
        fun noReadCount()
    }
}
