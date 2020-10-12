package com.toker.sys.view.home.fragment.sheet.item.stsheet

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.my.bean.ProjectFormanBean
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.fragment.my.item.bean.MainCountBean
import com.toker.sys.view.home.fragment.my.item.bean.ReadCountBean

/**
 * @author yyx
 */

object STSheetContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun preForMan(data: ProjectFormanBean.Data)
        fun showDataNumBer(data: ReadCountBean.Data)
        fun showDataCount(data: MainCountBean.Data)
        fun preErrorForMan()
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun getMyPerformance()
    }
}
