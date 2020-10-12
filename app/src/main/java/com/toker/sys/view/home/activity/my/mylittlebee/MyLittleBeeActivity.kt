package com.toker.sys.view.home.activity.my.mylittlebee

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.view.home.fragment.event.TaskEvent
import com.toker.sys.view.home.fragment.my.item.mylittlebee.extenrecord.ExtenRecordFragment
import com.toker.sys.view.home.fragment.my.item.mylittlebee.littlebee.LittleBeeFragment
import com.toker.sys.view.home.fragment.my.item.mylittlebee.punchrecord.PunchRecordFragment
import com.toker.sys.view.home.fragment.task.adapter.TaskAdapter
import kotlinx.android.synthetic.main.activity_my_little_bee.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的小蜜蜂
 * @author yyx
 */

class MyLittleBeeActivity : BaseActivity<MyLittleBeeContract.View, MyLittleBeePresenter>(), MyLittleBeeContract.View{

    override var mPresenter: MyLittleBeePresenter = MyLittleBeePresenter()

    private var mBeans = arrayOf("")

    override fun layoutResID(): Int  = R.layout.activity_my_little_bee

    override fun initView() {
        tv_title.text = "我的小蜜蜂"
    }

    override fun initData() {
        EventBus.getDefault().register(this)
        mBeans =resources.getStringArray(R.array.my_bee)
        rv_my_been.layoutManager = GridLayoutManager(this, 3)
        rv_my_been.adapter = TaskAdapter(this, mBeans)
        setOnClickListener(img_back)
        TaskEvent(TaskEvent("小蜜蜂"))
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN_ORDERED)
    fun TaskEvent(event: TaskEvent) {
        setIntentFragment(
            when (event.name) {
                //小蜜蜂
                mBeans[0] -> {
                    ll_my_little.visibility = GONE
                    LittleBeeFragment.newInstance()
                }
                //打卡记录
                mBeans[1] -> {
                    ll_my_little.visibility = VISIBLE
                    PunchRecordFragment.newInstance()
                }
                //拓客记录
                else -> {
                    ll_my_little.visibility = VISIBLE
                    ExtenRecordFragment.newInstance()
                }
            }, Bundle()
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

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
    private fun setIntentFragment(fragment: Fragment, bundle: Bundle) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        beginTransaction.replace(R.id.fl_my_bee, fragment)
        beginTransaction.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
