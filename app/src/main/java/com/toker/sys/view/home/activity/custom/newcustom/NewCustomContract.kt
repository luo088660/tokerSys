package com.toker.sys.view.home.activity.custom.newcustom

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.custom.bean.ArriveAreaBean
import com.toker.sys.view.home.activity.custom.bean.CityBean
import com.toker.sys.view.home.activity.custom.bean.ProjectOnCityBean
import com.toker.sys.view.home.bean.Data

/**
 * @author yyx
 */

object NewCustomContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showSuccess(data: String)
        fun showError(desc: String)
        fun showProjectDate(data: MutableList<Data>)
        fun showCityData(data: MutableList<CityBean.Data>)
        fun showProjectCityData(data: MutableList<ProjectOnCityBean.Data>)
        fun showArriveAreaData(data: MutableList<ArriveAreaBean.Data>)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun getProjectList()
        fun customerRecommend()
        fun getCities()
        fun getProjectOnCityId()
        fun getArriveArea()
        fun getArriveAddr()
    }
}
