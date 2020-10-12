package com.toker.sys.view.home.fragment.task.item.tasktrajectory

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.view.home.activity.sheet.event.TimeEvent
import com.toker.sys.view.home.activity.task.transacttask.TransactTaskActivity
import com.toker.sys.view.home.fragment.task.adapter.TaskTrajectoryAdapter
import com.toker.sys.view.home.fragment.task.bean.TaskTrajectoryBean
import kotlinx.android.synthetic.main.fragment_task_trajectory.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat

/**
 * 任务轨迹
 * @author yyx
 */

class TaskTrajectoryFragment : BaseFragment<TaskTrajectoryContract.View, TaskTrajectoryPresenter>(),
    TaskTrajectoryContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): TaskTrajectoryFragment {
            val args = Bundle()
            val fragment = TaskTrajectoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val sdf = SimpleDateFormat("yyyy-MM-dd")
    private var id: String = ""
    private var tableTag: String = ""
    private var date: String = sdf.format(System.currentTimeMillis())
    private var traceList = mutableListOf<TaskTrajectoryBean.Trace>()

    override var mPresenter: TaskTrajectoryPresenter = TaskTrajectoryPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_task_trajectory, container!!)
        }
        return main_layout!!
    }

    private var adapter: TaskTrajectoryAdapter? = null

    override fun initView() {
        EventBus.getDefault().register(this)
        id = arguments!!.getString("id")
        tableTag = arguments!!.getString("tableTag")
        date = arguments!!.getString("date")
        mPresenter.loadRepositories()
        rv_task_trajectory.layoutManager = GridLayoutManager(context!!, 1)

        adapter = TaskTrajectoryAdapter(context!!, traceList)
        rv_task_trajectory.adapter = adapter
        rv_task_trajectory.setHasFixedSize(true)
        rv_task_trajectory.isNestedScrollingEnabled = true
    }

    override fun initData() {

    }
    /**
     * 时间
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun TimeEvent(event: TimeEvent){
        date = event.time
        mPresenter.loadRepositories()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun TaskTrajectoryBean(event: TaskTrajectoryBean.Trace) {
        val intent = Intent(context, TransactTaskActivity::class.java)
        intent.putExtra("type", 1)
        intent.putExtra("taskId", event.taskId)
        intent.putExtra("userId", event.objectId)
        intent.putExtra("tableTag", event.tableTag)
        intent.putExtra("updateTime", event.date)
        context!!.startActivity(intent)
    }


    override fun showData(data: TaskTrajectoryBean.Data) {

        tv_task_tarjectory_01.text =
            Html.fromHtml(String.format(getString(R.string.tip_task_number_01), data.right))
        tv_task_tarjectory_02.text =
            Html.fromHtml(String.format(getString(R.string.tip_task_number_02), data.error))
        if (!data.traceList.isNullOrEmpty()) {
            adapter!!.refreshView(data.traceList)
        }else{
            adapter!!.refreshView(traceList)
        }

    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["taskId"] = id
        map["tableTag"] = tableTag
        map["date"] = date
        return map
    }

    override fun getUrl(url_type: Int): String {
        return TaskManageImp.API_TASK_EVENT_TRACE_COUNT
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}