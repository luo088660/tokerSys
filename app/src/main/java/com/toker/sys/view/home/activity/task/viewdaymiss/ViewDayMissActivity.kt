package com.toker.sys.view.home.activity.task.viewdaymiss

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.common.request.MonthIndicImp
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.view.home.activity.task.adapter.ViewDayMissAdapter
import com.toker.sys.view.home.activity.task.bean.ViewDayMissBean
import com.toker.sys.view.home.fragment.task.bean.PageData
import kotlinx.android.synthetic.main.activity_view_day_miss.*
import kotlinx.android.synthetic.main.layout_content_title.*

/**
 * 查看日任务
 * @author yyx
 */

class ViewDayMissActivity : BaseActivity<ViewDayMissContract.View, ViewDayMissPresenter>(),
    ViewDayMissContract.View {


    override var mPresenter: ViewDayMissPresenter = ViewDayMissPresenter()
    var viewDayMissAdapter: ViewDayMissAdapter? = null
    val mBeans: MutableList<ViewDayMissBean.Data> = mutableListOf();
    var objectName = ""
    private var bean: MonthIndicImp? = null
    override fun layoutResID(): Int = R.layout.activity_view_day_miss

    override fun initView() {
        tv_title.text = "每日任务详情"

        bean = intent.getSerializableExtra(Constants.BEANDATA) as MonthIndicImp
        mPresenter.loadRepositories()
        rv_view_day.layoutManager = GridLayoutManager(this, 1)

        viewDayMissAdapter = ViewDayMissAdapter(this, mBeans)
        rv_view_day.adapter = viewDayMissAdapter
    }

    override fun initData() {
        setOnClickListener(img_back)
        setOnClickListener(img_day_miss_quqea)
//        et_view_day_miss.addTextChangedListener(mPresenter.textChangerd())
    }

    override fun afterTextChanged() {
        objectName = "${et_view_day_miss.text}"
        mPresenter.loadRepositories()


    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            img_day_miss_quqea -> afterTextChanged()
            else -> {
            }
        }
    }

    override fun showData(data: MutableList<ViewDayMissBean.Data>) {
        viewDayMissAdapter!!.refreshData(data)
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["taskId"] = bean!!.id
        map["tableTag"] = bean!!.tableTag
        if (objectName.isNotEmpty()) {
            map["objectName"] = objectName
        }
        return map
    }

    override fun getUrl(url_type: Int): String {
        return TaskManageImp.API_TASK_TARGET_DAY_TASK
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {

    }
}
