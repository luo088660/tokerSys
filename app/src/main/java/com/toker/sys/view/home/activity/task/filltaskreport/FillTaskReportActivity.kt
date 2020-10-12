package com.toker.sys.view.home.activity.task.filltaskreport

import android.view.View
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import kotlinx.android.synthetic.main.layout_content_title.*

/**
 * 填写任务汇报
 * @author yyx
 */

class FillTaskReportActivity : BaseActivity<FillTaskReportContract.View, FillTaskReportPresenter>(), FillTaskReportContract.View{

    override var mPresenter: FillTaskReportPresenter = FillTaskReportPresenter()


    override fun layoutResID(): Int  = R.layout.activity_fill_task_report

    override fun initView() {
        tv_title.text = "天河体育中心派发宣传单"
    }

    override fun initData() {
        setOnClickListener(img_back)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> {
                finish()
            }
            else -> {
            }
        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
}
