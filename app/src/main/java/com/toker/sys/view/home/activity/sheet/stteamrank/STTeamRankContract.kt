package com.toker.sys.view.home.activity.sheet.stteamrank

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.bean.STExtenRankBean

/**
 * @author yyx
 */

object STTeamRankContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showDataList(data: STExtenRankBean.Data)
        fun showProjectDate(data: MutableList<Data>)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun getProjectList()
    }
}
