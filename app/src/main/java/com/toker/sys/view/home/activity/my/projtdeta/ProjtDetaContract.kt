package com.toker.sys.view.home.activity.my.projtdeta

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.my.bean.ProjectByTeamBean
import com.toker.sys.view.home.activity.my.bean.ProjectFormanBean
import com.toker.sys.view.home.activity.my.bean.SchedMonthBean

/**
 * @author yyx
 */

object ProjtDetaContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showDataSched(data: SchedMonthBean.Data)
        fun byTeamData(data: ProjectByTeamBean.Data)
        fun preForMan(data: ProjectFormanBean.Data)
        fun showGroupAdmin(userName: String)
        fun showCountGroup(data: ProjtDetaPresenter.CountGroup.Data)
        fun onErrorSched(desc: String)
        fun preErrorForMan()
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun AttendanceByTeam()
        fun schedulingByMonth()
        fun getMyPerformance()
    }
}
