package com.toker.sys.view.home.activity.custom.fcustomdetail

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import com.toker.sys.view.home.fragment.custom.bean.SelectUserList

/**
 * @author yyx
 */

object FCustomDetailContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: FCustomDetailTBean.Data)
        fun showUserList(records: MutableList<SelectUserList.PageData>)
        fun showAllocation()
        fun customerYDetail(data: com.toker.sys.view.home.activity.custom.fcustomdetail.Data)
        fun showErrorData(desc: String)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun publicPoCust()
        fun selectUserEvent()
        fun customerYDetail()
    }
}
