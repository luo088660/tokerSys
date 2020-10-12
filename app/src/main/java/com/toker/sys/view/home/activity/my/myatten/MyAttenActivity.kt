package com.toker.sys.view.home.activity.my.myatten

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.view.home.fragment.event.TaskEvent
import com.toker.sys.view.home.fragment.my.event.MyEvent
import com.toker.sys.view.home.fragment.my.item.myatten.attenstat.AttenStatFragment
import com.toker.sys.view.home.fragment.my.item.myatten.meatten.MeAttenFragment
import com.toker.sys.view.home.fragment.task.adapter.TaskAdapter
import kotlinx.android.synthetic.main.activity_my_atten.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的考勤
 * @author yyx
 */

class MyAttenActivity : BaseActivity<MyAttenContract.View, MyAttenPresenter>(), MyAttenContract.View{

    override var mPresenter: MyAttenPresenter = MyAttenPresenter()

    private var mBeans = arrayOf("")

    override fun layoutResID(): Int  = R.layout.activity_my_atten


    override fun initView() {
        tv_title.text = "我的考勤"
    }

    override fun initData() {
        EventBus.getDefault().register(this)
        mBeans = resources.getStringArray(R.array.my_attem)
        rv_my_atten.layoutManager = GridLayoutManager(this, 2)
        rv_my_atten.adapter = TaskAdapter(this, mBeans)
        setOnClickListener(img_back)
        setIntentFragment(MeAttenFragment.newInstance(), Bundle())
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN_ORDERED)
    fun TaskEvent(event: TaskEvent) {
        EventBus.getDefault().removeStickyEvent(event)
        setIntentFragment(
            when (event.name) {
                //我的考勤
                mBeans[0] -> {
                    MeAttenFragment.newInstance()
                }
                //考勤统计
                else -> {
                    AttenStatFragment.newInstance()
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
        beginTransaction.replace(R.id.fl_my_atten, fragment)
        beginTransaction.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
