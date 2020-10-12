package com.toker.sys.view.register.login

import android.text.TextWatcher
import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView

/**
 * @author yyx
 */

object LoginContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun userNameChange()
        fun showSuccessId()
        fun showonError(code: String, desc: String)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun textChagerUserName(): TextWatcher?
        fun textChagerPassWd(): TextWatcher?
        fun loadLogin()
    }
}
