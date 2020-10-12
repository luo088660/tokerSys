package com.toker.sys.view.home.fragment.task

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.common.request.MonthIndicImp
import com.toker.sys.dialog.ProjectListDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.task.adminitrandetail.AdminiTranDetailActivity
import com.toker.sys.view.home.activity.task.monthindicass.MonthIndicASActivity
import com.toker.sys.view.home.activity.task.transacttask.TransactTaskActivity
import com.toker.sys.view.home.activity.task.transareporttask.TransaReportTaskActivity
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.fragment.event.TaskEvent
import com.toker.sys.view.home.fragment.event.TaskEventT
import com.toker.sys.view.home.fragment.event.TaskHomeEvent
import com.toker.sys.view.home.fragment.task.item.controller.taskhomepage.TaskHomePageFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 任务
 * @author yyx
 */

class TaskFragment : BaseFragment<TaskContract.View, TaskPresenter>(), TaskContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): TaskFragment {
            val args = Bundle()
            val fragment = TaskFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var data: MutableList<Data>? = null
    override var mPresenter: TaskPresenter = TaskPresenter()


    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        log("createView", "TaskFragment")
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_task, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        val bundle = Bundle()
        bundle.putInt("typeSW",1)
        setIntentFragment(TaskHomePageFragment.newInstance(), bundle)
        LogUtils.d(TAG, "TaskEvent:---${222} ");
    }


    override fun initData() {
        mPresenter.loadRepositories()
    }


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {

    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN_ORDERED)
    fun TaskEvent(event: TaskEventT) {
        EventBus.getDefault().removeStickyEvent(event)
        EventBus.getDefault().postSticky(TaskEvent(resources.getString(R.string.tip_approved)))
    }


        // 中层MiddleLayer
    // 管理员Controller
    // 组员TeamLeader
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)//MAIN_ORDERED
    fun TaskEvent(event: TaskHomeEvent) {
        EventBus.getDefault().removeStickyEvent(event)
            LogUtils.d(TAG, "TaskEvent:---${111} ");
            LogUtils.d(TAG, "TaskEvent:---${event.bean?.id}--${event.bean?.tableTag} ");
        when (event.type) {
            1 -> {
                val bundle = Bundle()
                bundle.putInt("typeSW",2)
                setIntentFragment(TaskHomePageFragment.newInstance(), bundle)
            }

            2 -> {
                // 月指标考核任务 查看详情
//                val intent = Intent(activity!!, MonthIndicAssActivity::class.java)
                val intent = Intent(activity!!, MonthIndicASActivity::class.java)
                val bean = MonthIndicImp(event.bean?.id!!,event.bean?.tableTag!!)
                intent.putExtra(Constants.BEANDATA, bean)
                activity?.startActivity(intent)
            }

            3 -> ProjectListDialog(context!!, data!!)

            4, 5 -> {
                //管理员 事务详情  TransactTaskActivity
                val intent = Intent(activity, AdminiTranDetailActivity::class.java)
                intent.putExtra("type", event.type)
                intent.putExtra("id", event.id)
                intent.putExtra("tableTag", event.tableTag)
                activity?.startActivity(intent)
            }
            //填写事务任务汇报
            6 -> {
                val intent = Intent(activity, TransaReportTaskActivity::class.java)
                intent.putExtra("id", event.id)
                intent.putExtra("tableTag", event.tableTag)
                activity!!.startActivity(intent)
            }
            //查看个人任务轨迹
            7 -> {
                val intent = Intent(context, TransactTaskActivity::class.java)
                intent.putExtra("type", 2)
                intent.putExtra("taskId", event.id)
                intent.putExtra("userId", "")
                intent.putExtra("tableTag", event.tableTag)
                intent.putExtra("updateTime", event.updateTime)
                context!!.startActivity(intent)
            }
            else -> {
            }
        }


    }


    override fun showProjectDate(data: MutableList<Data>) {
        this.data = data

    }

    private fun setIntentFragment(fragment: Fragment, bundle: Bundle) {
        val beginTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        beginTransaction.replace(R.id.fl_custom, fragment)
        beginTransaction.commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log("onDestroyView", "TaskFragment")
        EventBus.getDefault().unregister(this)
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
//        map["taskId"] = "5815af81-d538-4908-8c70-0c7f4540d96c"
//        map["tableTag"] = "2019-08-01"
//        map["date"] = "2019-07-14"
        return map
    }


    override fun getUrl(url_type: Int): String {
        return SystemSettImp.API_SYSTEM_PROJECT_LIST
//        return API_TASK_EVENT_TASKR_EPORTLIST
    }

}