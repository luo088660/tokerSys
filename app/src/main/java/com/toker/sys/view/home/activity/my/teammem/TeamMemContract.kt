package com.toker.sys.view.home.activity.my.teammem

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.my.bean.GroupProjectBean
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.fragment.my.item.bean.ExtensionBean
import com.toker.sys.view.home.fragment.my.item.bean.LittleBeBean

/**
 * @author yyx
 */

object TeamMemContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showEDataList(data: ExtensionBean.Data)
        fun showLDataList(data: LittleBeBean.Data)
        fun showGroupProject(data: MutableList<GroupProjectBean.Data>)
        fun showProjectDate(data: MutableList<Data>)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun loadRepositories(type: Int)
        fun getGroupByProjectId()
    }
}
