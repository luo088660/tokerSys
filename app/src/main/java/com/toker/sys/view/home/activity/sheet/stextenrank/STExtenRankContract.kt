package com.toker.sys.view.home.activity.sheet.stextenrank

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.bean.STExtenRankBean
import com.toker.sys.view.home.fragment.my.item.bean.GroupListBean

/**
 * @author yyx
 */

object STExtenRankContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showListData(data: STExtenRankBean.Data)
        fun showProjectDate(data: MutableList<Data>)
        fun showGroupList(data: MutableList<GroupListBean.Data>)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun getProjectList()
        fun getGroupList()
    }
}
