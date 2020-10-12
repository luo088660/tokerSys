package com.toker.sys.view.home.activity.sheet.waitforcust.custodetail

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.custom.fcustomdetail.FCustomDetailActivity
import com.toker.sys.view.home.bean.CustoDatailBean
import com.toker.sys.view.home.bean.ProjectToBean
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import com.toker.sys.view.home.fragment.event.TaskEvent
import com.toker.sys.view.home.fragment.sheet.item.comingintpool.ComingIntPoolFragment
import com.toker.sys.view.home.fragment.sheet.item.overdfollowup.OverdFollowUpFragment
import com.toker.sys.view.home.fragment.task.adapter.TaskAdapter
import kotlinx.android.synthetic.main.activity_custo_detail.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 客户明细
 * @author yyx
 */

class CustoDetailActivity : BaseActivity<CustoDetailContract.View, CustoDetailPresenter>(), CustoDetailContract.View {

    override var mPresenter: CustoDetailPresenter = CustoDetailPresenter()


    override fun layoutResID(): Int = R.layout.activity_custo_detail
    var bean:ProjectToBean.Record? = null
    override fun initView() {
        EventBus.getDefault().register(this)
        tv_title.text = "客户明细"
        bean = intent.getSerializableExtra("bean")as ProjectToBean.Record
        LogUtils.d(TAG, "bean:$bean ");
        val bundle = Bundle()
        Log.d(TAG, "bean!!.id:${bean!!.id} ");
        bundle.putString("id", bean!!.id)
        setIntentFragment(OverdFollowUpFragment.newInstance(), bundle)
    }

    override fun initData() {
        setOnClickListener(img_back)

        val mBeans = resources.getStringArray(R.array.task_custo_detail)
        rv_custo_detail.layoutManager = GridLayoutManager(this, 2)
        rv_custo_detail.adapter = TaskAdapter(this, mBeans)

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN_ORDERED)
    fun TaskEvent(event: TaskEvent) {
        Log.d(TAG, "bean!!.id:${bean!!.id} ");
        val bundle = Bundle()
        bundle.putString("id", bean!!.id)
        setIntentFragment(
            when (event.name) {
                //逾期未跟进
                resources.getString(R.string.tip_overdue_follow_up) -> {
                    OverdFollowUpFragment.newInstance()
                }
                //即将进入公客池
                else -> {
                    ComingIntPoolFragment.newInstance()
                }
            }, bundle
        )

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            else -> {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun MyCusEvent(event: CustoDatailBean.Record) {
        event.tableTag = ""
        val bean = FollowCTBean.PageData(event.customerId,event.tableTag)
        val intent = Intent(this, FCustomDetailActivity::class.java)
        intent.putExtra("type",5)
        intent.putExtra("bean",bean)
        startActivity(intent)
    }


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    private fun setIntentFragment(fragment: Fragment, bundle: Bundle) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        beginTransaction.replace(R.id.fl_custo_detail, fragment)
        beginTransaction.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
