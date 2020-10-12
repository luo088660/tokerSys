package com.toker.sys.view.home.fragment.task.item.middleLayer.tentire

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.task.bean.Data

/**
 * @author yyx
 */

object TEntireContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun targetTaskPage(pageData: Data)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
