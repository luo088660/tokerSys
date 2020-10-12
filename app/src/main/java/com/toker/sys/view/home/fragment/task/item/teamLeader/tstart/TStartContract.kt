package com.toker.sys.view.home.fragment.task.item.teamLeader.tstart

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.task.bean.Data

/**
 * @author yyx
 */

object TStartContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun targetTaskPage(data: Data)
        fun eventTaskPage(data: com.toker.sys.view.home.fragment.task.item.controller.treceived.Data)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories(typeStuta: Int)
    }
}
