package com.toker.sys.view.home.fragment.task.item.controller.taskhomepage

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.home.activity.task.event.ScrollTopEvent
import com.toker.sys.view.home.fragment.event.TaskCustEvent
import com.toker.sys.view.home.fragment.event.TaskEvent
import com.toker.sys.view.home.fragment.event.TaskHomeEvent
import com.toker.sys.view.home.fragment.task.adapter.TaskAdapter
import com.toker.sys.view.home.fragment.task.item.controller.tapproval.TApprovalFragment
import com.toker.sys.view.home.fragment.task.item.controller.treceived.TReceivedFragment
import com.toker.sys.view.home.fragment.task.item.middleLayer.tentire.TEntireFragment
import com.toker.sys.view.home.fragment.task.item.middleLayer.tpend.TPendFragment
import com.toker.sys.view.home.fragment.task.item.teamLeader.tcurrent.TCurrentFragment
import com.toker.sys.view.home.fragment.task.item.teamLeader.tstart.TStartFragment
import kotlinx.android.synthetic.main.fragment_task_home_page.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 任务主页面
 * @author yyx
 */

class TaskHomePageFragment : BaseFragment<TaskHomePageContract.View, TaskHomePagePresenter>(),
    TaskHomePageContract.View, RadioGroup.OnCheckedChangeListener {
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            //我的任务
            R.id.rb_yx_custom -> {
                LogUtils.d(TAG, "我的任务")
                taskType = false
            }
            //团队任务
            else -> {
                LogUtils.d(TAG, "团队任务")
                taskType = true
            }
        }
        EventBus.getDefault().post(TaskCustEvent(taskType))
    }

    companion object {
        @JvmStatic
        fun newInstance(): TaskHomePageFragment {
            val args = Bundle()
            val fragment = TaskHomePageFragment()
            fragment.arguments = args
            return fragment
        }
    }
    //拓客组长
    private var taskType = false
    private var typeSW = 1

    private var mBeans1 = arrayOf("")
    private var mBeans2 = arrayOf("")
    private var mBeans3 = arrayOf("")
    private var mBeans = arrayOf("")
    private var taskAdapter: TaskAdapter? = null
    override var mPresenter: TaskHomePagePresenter = TaskHomePagePresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_task_home_page, container!!)
        }
        return main_layout!!
    }


    override fun initView() {
        img_back.visibility = GONE
//        ll_msg.visibility = VISIBLE
        tv_title.text = "任务"
        typeSW =  arguments?.getInt("typeSW")!!
        initFragmentTabHost()
        EventBus.getDefault().register(this)
        LogUtils.d(TAG, "typeSW:$typeSW ");
        ll_custom.setOnCheckedChangeListener(this)
        ll_custom.check(R.id.rb_yx_custom)
    }


    override fun initData() {
        setOnClickListener(img_back)
        setOnClickListener(ll_msg)
        setOnClickListener(btn_task_home)
        ll_task_home.visibility = if (AppApplication.TYPE == Constants.RESCUE4) VISIBLE else GONE
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            //我的消息
            ll_msg -> EventBus.getDefault().post(MainEvent(16))
            //返回顶部
            btn_task_home -> EventBus.getDefault().post(ScrollTopEvent(0))
            img_back -> {
                EventBus.getDefault().post(MainEvent(1))
            }
            else -> {
            }
        }
    }


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }


    private fun initFragmentTabHost() {
        log("initFragmentTabHost", "TaskFragment$typeSW")
        mBeans1 = resources.getStringArray(R.array.task_rescue1)
        mBeans2 = resources.getStringArray(R.array.task_rescue2)
        mBeans3 = resources.getStringArray(R.array.task_rescue3)
        mBeans = when (AppApplication.TYPE) {
            //全部，我创建的
            Constants.RESCUE1 -> {
                val bundle = Bundle()
                bundle.putString("type", "1")
                setIntentFragment(TEntireFragment.newInstance(), bundle)
                mBeans1
            }
            //我接收的任务，我下发的任务
            Constants.RESCUE2 -> {
                val bundle = Bundle()
                bundle.putInt("key", 1)
                setIntentFragment(TReceivedFragment.newInstance(), bundle)
                mBeans2
            }
            //当前任务
            Constants.RESCUE3 -> {
                val bundle = Bundle()
                bundle.putInt(Constants.TYPE,typeSW)
                setIntentFragment(TCurrentFragment.newInstance(), bundle)
                mBeans3
            }
            //当前任务
            else -> {
                val bundle = Bundle()
                bundle.putBoolean(Constants.TASKTYPE,taskType)
                bundle.putInt(Constants.TYPE,typeSW)
                setIntentFragment(TCurrentFragment.newInstance(), bundle)
                mBeans3
            }
        }
        rv_task.layoutManager = GridLayoutManager(activity!!, 3)
        taskAdapter = TaskAdapter(activity!!, mBeans)


        rv_task.adapter = taskAdapter


    }

    // 中层MiddleLayer
// 管理员Controller
// 组员TeamLeader
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN_ORDERED)
    fun TaskEvent(event: TaskEvent) {
        EventBus.getDefault().removeStickyEvent(event)
        val bundle = Bundle()
        val fragment: Fragment = when (event.name) {
            //全部，我创建的
            mBeans1[0] -> {
                bundle.putString("type", "1")
                TEntireFragment.newInstance()
            }
            mBeans1[1] -> {
                bundle.putString("type", "2")
                TEntireFragment.newInstance()
            }
            //待审批
            mBeans1[2] -> {
                taskAdapter!!.upDataView(2)
                TPendFragment.newInstance()
            }
            //我接收的任务，我下发的任务
            mBeans2[0],
            mBeans2[1] -> {
                bundle.putInt("key", if (event.name == mBeans2[0]) 1 else 2)
                TReceivedFragment.newInstance()
            }
            //审批中
            activity!!.resources.getString(R.string.tip_process)
            -> {
                taskAdapter!!.upDataView(2)
                TApprovalFragment.newInstance()
            }
            //当前任务
            mBeans3[0] -> {
                bundle.putBoolean(Constants.TASKTYPE,taskType)
                bundle.putInt(Constants.TYPE,typeSW)
                TCurrentFragment.newInstance()
            }
            //未开始，已完成
            mBeans3[1] -> {
                bundle.putBoolean(Constants.TASKTYPE,taskType)
                bundle.putString("type", "2")
                TStartFragment.newInstance()
            }
            mBeans3[2] -> {
                bundle.putBoolean(Constants.TASKTYPE,taskType)
                bundle.putString("type", "3")
                TStartFragment.newInstance()
            }

            else -> {
//                Fragment()
                return
            }

        }
        setIntentFragment(fragment, bundle)
    }

    private fun setIntentFragment(fragment: Fragment, bundle: Bundle) {
        val beginTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        beginTransaction.replace(R.id.fl_custom_home, fragment)
        beginTransaction.commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log("onDestroyView", "TaskFragment")
        EventBus.getDefault().unregister(this)
    }
}