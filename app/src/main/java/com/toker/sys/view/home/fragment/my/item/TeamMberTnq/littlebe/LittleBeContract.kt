package com.toker.sys.view.home.fragment.my.item.TeamMberTnq.littlebe

import com.toker.sys.mvp.base.IBasePresenter
import com.toker.sys.mvp.base.IBaseView
import com.toker.sys.view.home.fragment.my.item.bean.LittleBeBean

/**
 * @author yyx
 */

object LittleBeContract {

    interface View : IBaseView {
        fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int)
        fun showDataList(data: LittleBeBean.Data)
    }

    interface Presenter : IBasePresenter<View> {
        fun loadRepositories()
    }
}
