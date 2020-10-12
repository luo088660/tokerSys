package com.toker.sys.view.home.fragment.custom.item.invacust

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.custom.bean.FollowCustBean
import com.toker.sys.view.home.fragment.custom.bean.SelectUserList

/**
 * @author yyx
 */

object InvaCustContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: FollowCustBean.Data)
        fun showUserList(pageData: MutableList<SelectUserList.PageData>)
        fun showAllocation()
        fun showAllocationError(desc: String)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun selectUserEvent()
        fun publicPoCust()
    }
}
