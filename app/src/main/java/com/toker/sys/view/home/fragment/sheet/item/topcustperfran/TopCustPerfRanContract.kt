package com.toker.sys.view.home.fragment.sheet.item.topcustperfran

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.my.item.bean.GroupListBean

/**
 * @author yyx
 */

object TopCustPerfRanContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showGroupList(data: MutableList<GroupListBean.Data>)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
