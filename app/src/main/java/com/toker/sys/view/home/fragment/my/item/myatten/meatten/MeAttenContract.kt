package com.toker.sys.view.home.fragment.my.item.myatten.meatten

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.my.item.myatten.bean.MeAttenBean

/**
 * @author yyx
 */

object MeAttenContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: MeAttenBean.Data)
        fun onShowError(desc: String)
        fun onErrorData(desc: String)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun checkIn()
        fun checkOut()
        fun uploadLocation()
        fun updateCheck(type: Int)
    }
}
