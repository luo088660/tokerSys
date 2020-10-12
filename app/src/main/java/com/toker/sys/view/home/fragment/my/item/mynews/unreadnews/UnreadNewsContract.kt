package com.toker.sys.view.home.fragment.my.item.mynews.unreadnews

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.my.item.bean.UnreadNewsBean

/**
 * @author yyx
 */

object UnreadNewsContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showDataList(data: UnreadNewsBean.Data)
        fun noReadCount(i: Int)
        fun showDataSuccess(desc: String)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun readMsg()
        fun noSendTraceMsg()
    }
}
