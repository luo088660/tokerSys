package com.toker.sys.view.home.fragment.sheet.item.currenttask

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.bean.STExtenRankBean
import com.toker.sys.view.home.fragment.task.item.controller.treceived.Data

/**
 * @author yyx
 */

object CurrentTaskContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun eventTaskPage(data: Data)
        fun showDataList(data: STExtenRankBean.Data)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun getUserCompanyRank()
    }
}
