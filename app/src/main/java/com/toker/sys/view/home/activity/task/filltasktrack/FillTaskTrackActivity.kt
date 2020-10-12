package com.toker.sys.view.home.activity.task.filltasktrack

import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity

/**
 * 填写任务轨迹
 * @author yyx
 */

class FillTaskTrackActivity : BaseActivity<FillTaskTrackContract.View, FillTaskTrackPresenter>(), FillTaskTrackContract.View{

    override var mPresenter: FillTaskTrackPresenter = FillTaskTrackPresenter()


    override fun layoutResID(): Int  = R.layout.activity_fill_task_track

    override fun initView() {
    }

    override fun initData() {
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
}
