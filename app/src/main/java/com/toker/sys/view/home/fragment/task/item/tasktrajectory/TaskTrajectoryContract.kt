package com.toker.sys.view.home.fragment.task.item.tasktrajectory

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.task.bean.TaskTrajectoryBean

/**
 * @author yyx
 */

object TaskTrajectoryContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: TaskTrajectoryBean.Data)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
