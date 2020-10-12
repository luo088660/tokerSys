package com.toker.sys.view.home.fragment.my.item.myatten.attenstat

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.my.bean.AttenDetailBean
import com.toker.sys.view.home.fragment.my.item.bean.AttenStatBean

/**
 * @author yyx
 */

object AttenStatContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showListData(data: MutableList<AttenStatBean.Data>)
        fun showDataList(data: AttenDetailBean.Data)
        fun showErrorData()
        fun showErrorListData()
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun ByTeam()
    }
}
