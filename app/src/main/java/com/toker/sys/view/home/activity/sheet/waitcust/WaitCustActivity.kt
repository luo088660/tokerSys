package com.toker.sys.view.home.activity.sheet.waitcust

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.toker.sys.R
import com.toker.sys.dialog.my.CallPhoneDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.view.home.activity.custom.mcfcustomdetail.McfCustomDetailActivity
import com.toker.sys.view.home.activity.custom.newcustom.NewCustomActivity
import com.toker.sys.view.home.fragment.custom.event.MyCusEvent
import com.toker.sys.view.home.fragment.event.TaskEvent
import com.toker.sys.view.home.fragment.sheet.item.comingintpool.ComingIntPoolFragment
import com.toker.sys.view.home.fragment.sheet.item.overdfollowup.OverdFollowUpFragment
import com.toker.sys.view.home.fragment.task.adapter.TaskAdapter
import kotlinx.android.synthetic.main.activity_wait_cust.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 拓客员，拓客组长
 * 待跟进客户
 * @author yyx
 */

class WaitCustActivity : BaseActivity<WaitCustContract.View, WaitCustPresenter>(), WaitCustContract.View {

    override var mPresenter: WaitCustPresenter = WaitCustPresenter()


    override fun layoutResID(): Int = R.layout.activity_wait_cust

    override fun initView() {
        tv_title.text = "逾期未跟进客户"
        setIntentFragment(OverdFollowUpFragment.newInstance(), Bundle())
    }

    override fun initData() {
        EventBus.getDefault().register(this)
        setOnClickListener(img_back)
        setOnClickListener(btn_wait_cust_phone)

        val mBeans = resources.getStringArray(R.array.task_custo_detail)
        rv_wait_cust.layoutManager = GridLayoutManager(this, 2)
        rv_wait_cust.adapter = TaskAdapter(this, mBeans)
    }


    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun TaskEvent(event: TaskEvent) {

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
            }, Bundle()
        )

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun MyCusEvent(event: MyCusEvent) {
        when (event.type) {
            //跟进记录
            1 -> {
                val bean = event.mBeans!!
                val intent = Intent()
                intent.putExtra("isType", false)
                intent.putExtra("bean", bean)


                intent.setClass(
                    this,
                    McfCustomDetailActivity::class.java
                )

                startActivity(intent)
            }
            //拨打电话
            2 -> {
                CallPhoneDialog(this, event.mBeans!!.phone)
            }
            //推荐客户
            3 -> {
                val intent = Intent(this, NewCustomActivity::class.java)
                intent.putExtra("type", true)
                intent.putExtra("mBean", event.mDBean)
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            //新增客户
            btn_wait_cust_phone->startActivity(Intent(this,NewCustomActivity::class.java))

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
        beginTransaction.replace(R.id.fl_wait_cust, fragment)
        beginTransaction.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
