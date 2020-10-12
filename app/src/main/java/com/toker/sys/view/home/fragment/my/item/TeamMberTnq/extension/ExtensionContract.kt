package com.toker.sys.view.home.fragment.my.item.TeamMberTnq.extension

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.my.item.bean.ExtensionBean

/**
 * @author yyx
 */

object ExtensionContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showDataList(data: ExtensionBean.Data)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
