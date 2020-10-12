package com.toker.sys.view.register.splash

import android.content.Context
import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.bean.SplashBean

/**
 * @author yyx
 */

object SplashContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun Login(bean: SplashBean)
        fun setVersion(version: String, content: String)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
        fun getLogin(mContext: Context)
        fun uploadTaskRecord()
    }
}
