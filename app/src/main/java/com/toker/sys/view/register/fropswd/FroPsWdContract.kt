package com.toker.sys.view.register.fropswd

import android.widget.Button
import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView

/**
 * @author yyx
 */

object FroPsWdContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showError(desc: String)
        fun showSuccess(data: String)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun initFroCode(phone: String, btnFroVcode: Button?)
    }
}
