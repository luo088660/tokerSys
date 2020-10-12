package com.toker.sys.view.home.fragment.custom.item.mycus

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean

/**
 * @author yyx
 */

object MyCusContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: FollowCTBean.Data)
        fun deleteView(position: Int)
        fun deleteSuccess()
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun deleteView()
    }
}
