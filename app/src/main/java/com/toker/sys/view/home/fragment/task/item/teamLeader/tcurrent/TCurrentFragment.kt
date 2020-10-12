package com.toker.sys.view.home.fragment.task.item.teamLeader.tcurrent

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.dialog.task.TaskCategorieDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.task.event.ScrollTopEvent
import com.toker.sys.view.home.activity.task.event.TaskCategorieEvent
import com.toker.sys.view.home.activity.task.filltaskreport.FillTaskReportActivity
import com.toker.sys.view.home.activity.task.transacttask.TransactTaskActivity
import com.toker.sys.view.home.fragment.event.TaskCustEvent
import com.toker.sys.view.home.fragment.task.adapter.TCurrentAdapter
import com.toker.sys.view.home.fragment.task.adapter.TRTaskPageAdapter
import com.toker.sys.view.home.fragment.task.bean.Data
import com.toker.sys.view.home.fragment.task.bean.PageData
import kotlinx.android.synthetic.main.fragment_t_current.*
import kotlinx.android.synthetic.main.fragment_t_received.*
import kotlinx.android.synthetic.main.layout_t_entire_03.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 拓客员
 * 拓客组长
 *
 * 当前任务
 * @author yyx
 */

class TCurrentFragment : BaseFragment<TCurrentContract.View, TCurrentPresenter>(),
    TCurrentContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): TCurrentFragment {
            val args = Bundle()
            val fragment = TCurrentFragment()
            fragment.arguments = args
            return fragment
        }
    }

    //TODO type 1 指标型 or 2 事务型
    var type = 1
    private var tv01: TextView? = null
    private var tv02: TextView? = null
    var adapter: TCurrentAdapter? = null
    var tadapter: TRTaskPageAdapter? = null
    private var mBeans = mutableListOf<PageData>()
    var pageData1 =
        mutableListOf<com.toker.sys.view.home.fragment.task.item.controller.treceived.PageData>()
    private var pageSize = "1"
    override var mPresenter: TCurrentPresenter = TCurrentPresenter()
    //TODO taskType false 我的任务 true 团队任务
    private var taskType = false

    override fun onNetworkLazyLoad() {

    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_t_current, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)


        taskType = arguments!!.getBoolean(Constants.TASKTYPE)
        type = arguments!!.getInt(Constants.TYPE, 1)
        LogUtils.d(TAG, "type:$type ");
//        mPresenter.loadRepositories(type)


    }

    override fun onStart() {
        super.onStart()
        onFRefresh()
        rv_t_current.adapter = if (type == 1) adapter else tadapter
    }

    override fun initData() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_t_current_01, null)

        tv01 = view.findViewById<TextView>(R.id.tv_current_01)
        tv02 = view.findViewById<TextView>(R.id.tv_current_02)

        adapter = TCurrentAdapter(context!!, mBeans)
        tadapter = TRTaskPageAdapter(context!!, pageData1, taskType)
        rv_t_current.layoutManager = GridLayoutManager(context, 1)



        adapter!!.setHeaderView(view)
        tadapter!!.setHeaderView(view)

        rv_t_current.addItemDecoration(getItemDecoration())
        initSpringView(sp_t_current)
        setOnClickListener(tv01!!)

        tv01!!.text = if (type == 1) "指标型任务" else "事务型任务"
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            //选择任务类型
            tv01 -> {
                TaskCategorieDialog(context!!)
            }
            //填写任务汇报 FillTaskReport
            btn_t_entire_03_01 -> {
                activity?.startActivity(Intent(activity, FillTaskReportActivity::class.java))
            }
            //填写任务轨迹 FillTaskTrack
            btn_t_entire_03_02 -> {
                //context?.startActivity(Intent(context,FillTaskTrackActivity::class.java))
            }
            //事务任务-任务详情
            tv_entire_se03_deta -> {
                activity?.startActivity(Intent(activity, TransactTaskActivity::class.java))
            }
            else -> {
            }
        }
    }
    //平滑到顶部
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun scrollToPosition(x: ScrollTopEvent) {
        rv_t_current.scrollToPosition(0)
    }
    //选择任务类型
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun TaskCategorieEvent(event: TaskCategorieEvent) {
        tv01!!.text = event.name
        this.type = event.type
        tv02!!.text = Html.fromHtml(String.format(getString(R.string.tip_start_01), "0"))
        onFRefresh()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun TaskCustEvent(event: TaskCustEvent) {
        //TODO taskType false 我的任务 true 团队任务
        taskType = event.isType
        mBeans.clear()
        onFRefresh()

    }

    override fun onFLoadmore() {
        super.onFLoadmore()
        //加载更多
        var toInt = pageSize.toInt()
        toInt++
        pageSize = toInt.toString()
        mPresenter.loadRepositories(type)

    }

    override fun onFRefresh() {
        super.onFRefresh()
        //刷新
        pageSize = "1"
        mPresenter.loadRepositories(type)
        mBeans.clear()
        pageData1.clear()
    }


    override fun targetTaskPage(data: Data) {
        mBeans.addAll(data.pageData)
        if (pageSize == "1") {
            rv_t_current.adapter = adapter
        }

        adapter!!.refreshData(mBeans)

        tv02!!.text = Html.fromHtml(String.format(getString(R.string.tip_start_01), data.rowTotal))
    }

    /**
     * 事务型指标
     */
    override fun eventTaskPage(pageData: com.toker.sys.view.home.fragment.task.item.controller.treceived.Data) {
        pageData1.addAll(pageData.pageData)

        tadapter!!.refreshData(pageData1, taskType)
        if (pageSize == "1") {
            rv_t_current.adapter = tadapter
        }
        tv02!!.text =
            Html.fromHtml(String.format(getString(R.string.tip_start_01), pageData.rowTotal))
    }

    override fun getUrl(url_type: Int): String {


        return when (url_type) {
            //团队 个人 指标型任务列表
            1 -> if (taskType) TaskManageImp.API_TASK_GROUP_TARGET_TASK_PAGE else TaskManageImp.API_TASK_PERSON_TARGET_TASKPAGE
            2 -> if (taskType) TaskManageImp.API_TASK_GROUP_EVENT_TASK_PAGE else TaskManageImp.API_TASK_PERSON_EVENT_TASK_PAGE
            else -> {
                ""
            }
        }
    }


    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {

        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
//                map["taskName"] = "月"
                map["tag"] = "1"
                map["status"] = ""
                map["pageSize"] = "10"
                map["page"] = pageSize
            }
            else -> {
                map["tag"] = "1"
                map["status"] = "2"
                map["pageSize"] = "10"
                map["page"] = pageSize
            }
        }
        return map
    }


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }


    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}// Required empty public constructor