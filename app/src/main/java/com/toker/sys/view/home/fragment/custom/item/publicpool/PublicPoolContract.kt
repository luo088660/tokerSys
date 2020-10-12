package com.toker.sys.view.home.fragment.custom.item.publicpool

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.custom.bean.PublicPoolBean

/**
 * @author yyx
 */

object PublicPoolContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun onShowData(data: PublicPoolBean.Data)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
