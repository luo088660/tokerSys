package com.toker.sys.view.home.activity.my.projtdeta.viewattendan

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.my.bean.ProjectByTeamBean
import com.toker.sys.view.home.activity.my.bean.ViewAttenListBean1
import com.toker.sys.view.home.activity.my.bean.ViewAttenTeamBean
import com.toker.sys.view.home.bean.Data

/**
 * @author yyx
 */

object ViewAttendanContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showDataList(data: ViewAttenTeamBean.Data)
        fun byTeamData(data: ProjectByTeamBean.Data)
        fun byDayListData(data: MutableList<ViewAttenListBean1.Data>)
        fun showProjectDate(data: MutableList<Data>)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun ListByDay()
        fun getProjectList()
    }
}
