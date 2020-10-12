package com.toker.sys.view.home.activity.sheet.waitforcust

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.bean.ProjectNoBean
import com.toker.sys.view.home.bean.ProjectToBean
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean

/**
 * @author yyx
 */

object WaitForCustContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showDataA(data: ProjectToBean.Data)
        fun showListData(data: MutableList<com.toker.sys.view.home.bean.Data>)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun ProjectList()
    }
}
