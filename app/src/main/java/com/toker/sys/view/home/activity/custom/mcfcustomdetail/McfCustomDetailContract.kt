package com.toker.sys.view.home.activity.custom.mcfcustomdetail

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean

/**
 * @author yyx
 */

object McfCustomDetailContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showData(data: FCustomDetailTBean.Data)
        fun showSuccess(i: Int)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun invalidReason()
        fun customerFollow()
        fun customerRevocation()
    }
}
