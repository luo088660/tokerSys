package com.toker.sys.view.home.fragment.sheet.item.topcustperfran.stteamrank

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.bean.STExtenRankBean

/**
 * @author yyx
 */

object STTeaMRankContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showDataList(data: STExtenRankBean.Data)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
