package com.toker.sys.view.home.fragment.task.item.controller.treceived

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.dialog.task.AllStateDialog
import com.toker.sys.dialog.task.AllTaskTypeDialog
import com.toker.sys.dialog.task.TaskCategorieDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.task.event.AllStateEvent
import com.toker.sys.view.home.activity.task.event.AllTaskTypeEvent
import com.toker.sys.view.home.activity.task.event.ScrollTopEvent
import com.toker.sys.view.home.activity.task.event.TaskCategorieEvent
import com.toker.sys.view.home.fragment.event.TaskHomeEvent
import com.toker.sys.view.home.fragment.task.adapter.TRTaskPageAdapter
import com.toker.sys.view.home.fragment.task.adapter.TReceivedAdapter
import com.toker.sys.view.home.fragment.task.bean.Data
import com.toker.sys.view.home.fragment.task.bean.PageData
import kotlinx.android.synthetic.main.fragment_t_received.*
import kotlinx.android.synthetic.main.layout_t_received_01.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

/**
 * 拓客管理员
 * 我接收的任务，我下发的任务
 * @author yyx
 */

class TReceivedFragment : BaseFragment<TReceivedContract.View, TReceivedPresenter>(),
    TReceivedContract.View {


    private var mDatePicker1: CustomDatePicker? = null
    private var timeType = 3
    private val thisTimestamp = System.currentTimeMillis()

    var adapter: TReceivedAdapter? = null
    var tadapter: TRTaskPageAdapter? = null
    private var mBeans = mutableListOf<PageData>()
    var pageData1 =
        mutableListOf<com.toker.sys.view.home.fragment.task.item.controller.treceived.PageData>()
    private var startTime: String = ""
    private var endTime: String = ""
    private val sdf = SimpleDateFormat("yyyy/MM/dd")
    private val sdf1 = SimpleDateFormat("yyyy-MM-dd")

    companion object {
        @JvmStatic
        fun newInstance(): TReceivedFragment {
            val args = Bundle()
            val fragment = TReceivedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var tv02: TextView? = null
    private var tv011: TextView? = null
    private var tv01: TextView? = null
    private var tv03: TextView? = null
    private var tv04: TextView? = null
    private var tv05: TextView? = null
    private var tv07: TextView? = null

    //TODO key 为 1 我接收的任务 2 我下发的任务
    var key = 1
    //type 1 指标型 or 2 事务型
    var type = 1
    var taskType = ""
    var status = ""
    var projectId = ""
    private var pageSize = "1"
    override var mPresenter: TReceivedPresenter = TReceivedPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_t_received, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        key = arguments!!.getInt("key")
        log("key:$key", "TReceivedFragment")

        rv_t_received.layoutManager = GridLayoutManager(context!!, 1)

        mPresenter.loadRepositories(type)
        initRecyclerView()
    }

    private fun initRecyclerView() {

        val view = LayoutInflater.from(context).inflate(R.layout.layout_t_received_01, null)
        tv01 = view.findViewById<TextView>(R.id.tv_receivde1_01)
        tv011 = view.findViewById<TextView>(R.id.tv_receivde1_11)
        val ll = view.findViewById<LinearLayout>(R.id.ll_received_start_time)
        tv011!!.visibility = if (key == 1) GONE else VISIBLE
        tv02 = view.findViewById<TextView>(R.id.tv_receivde1_02)
        tv03 = view.findViewById<TextView>(R.id.tv_receivde1_03)
        tv04 = view.findViewById<TextView>(R.id.tv_receivde1_04)
        tv05 = view.findViewById<TextView>(R.id.tv_receivde1_05)
        val img = view.findViewById<ImageView>(R.id.tv_receivde1_06)
        tv07 = view.findViewById<TextView>(R.id.tv_receivde1_07)
        ll.visibility = VISIBLE
        setOnClickListener(tv01!!)
        setOnClickListener(tv011!!)
        setOnClickListener(tv02!!)
        setOnClickListener(tv03!!)
        setOnClickListener(tv04!!)
        setOnClickListener(tv05!!)
        setOnClickListener(img)

        adapter = TReceivedAdapter(context!!, mBeans)
        tadapter = TRTaskPageAdapter(context!!, pageData1)

        adapter!!.setHeaderView(view)
        tadapter!!.setHeaderView(view)
        rv_t_received.addItemDecoration(getItemDecoration())
        rv_t_received.adapter = adapter

        initSpringView(sp_t_received)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun initData() {
        //初始化时间
        initDatePicker()
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

    override fun onClick(v: View) {
        super.onClick(v)
        //当前时间
        val long2Str =
            DateFormatUtils.long2Str(thisTimestamp, false, this@TReceivedFragment.timeType)

        when (v.id) {
            //全部项目
            R.id.tv_receivde1_01 -> {
                EventBus.getDefault().post(TaskHomeEvent(3))
            }
            //任务类别
            R.id.tv_receivde1_11 -> {
                TaskCategorieDialog(context!!)
            }
            //全部任务类型
            R.id.tv_receivde1_02 -> AllTaskTypeDialog(context!!)
            //全部状态
            R.id.tv_receivde1_03 -> AllStateDialog(context!!)
            //任务开始日期
            R.id.tv_receivde1_04,
            R.id.tv_receivde1_05,
            R.id.tv_receivde1_06 -> {
                mDatePicker1!!.show(System.currentTimeMillis())
            }
            else -> {
            }
        }
    }

    //平滑到顶部
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun scrollToPosition(x: ScrollTopEvent) {
        rv_t_received.scrollToPosition(0)
    }
    /**
     * 指标型任务
     */
    override fun targetTaskPage(data: Data) {
        mBeans.addAll(data.pageData)
        adapter!!.refreshData(mBeans)
        rv_t_received.adapter = adapter

        tv07!!.text = Html.fromHtml(String.format(getString(R.string.tip_task), data.rowTotal))
    }

    /**
     * 事务型指标
     */
    override fun eventTaskPage(pageData: com.toker.sys.view.home.fragment.task.item.controller.treceived.Data) {
        pageData1.addAll(pageData.pageData)
        tadapter!!.refreshData(pageData1)
        rv_t_received.adapter = tadapter
        tv07!!.text = Html.fromHtml(String.format(getString(R.string.tip_task), pageData.rowTotal))
    }

    //任务类别
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun TaskCategorieEvent(event: TaskCategorieEvent) {
        tv011!!.text = event.name
        this.type = event.type
        onFRefresh()
    }

    //选择项目
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: com.toker.sys.view.home.bean.Data) {
        tv01!!.text = event.projectName
        projectId = event.projectId
        onFRefresh()
    }


    /**
     * 全部任务类型
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun AllTaskTypeEvent(event: AllTaskTypeEvent) {
        when (event.type) {
            3 -> {
                tv02!!.text = event.name
                taskType = ""
            }
            else -> {
                tv02!!.text = "${event.name}任务"
                taskType = "${event.type}"
            }
        }
        onFRefresh()
    }

    /**
     * 全部状态1
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun AllStateEvent(event: AllStateEvent) {
        tv03!!.text = event.name
        status = if (event.type == -1) "" else "${event.type}"
        onFRefresh()
    }


    /**
     * 时间选择
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker1 = CustomDatePicker(context, object : CustomDatePicker.CallbackSEListen {
            override fun onTimeSelected(
                startTime: Long, endTime: Long, timeType: Int
            ) {
                if(startTime ==0L ||endTime==0L){
                    return
                }
                LogUtils.d(TAG, "startTime:$startTime ");
                LogUtils.d(TAG, "endTime: $endTime");
                tv_receivde1_04.text = sdf.format(Date(startTime))
                tv_receivde1_05.text = sdf.format(Date(endTime))
                this@TReceivedFragment.startTime = sdf1.format(Date(startTime))
                this@TReceivedFragment.endTime = sdf1.format(Date(endTime))
                onFRefresh()
            }

        }, 0, false, true)

        // 不允许点击屏幕或物理返回键关闭
//        mDatePicker1?.setCancelable(false)
        // 不显示时和分
        mDatePicker1?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mDatePicker1?.setScrollLoop(true)
        // 不允许滚动动画
        mDatePicker1?.setCanShowAnim(false)


    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {

        val map = mutableMapOf<String, String>()
//        when (url_type) {
//            2 -> {
        if (taskType.isNotEmpty()) {
            map["taskType"] = taskType
        }
        map["tag"] = "$key"
        if (status.isNotEmpty()) {
            map["status"] = status
        }
        if (projectId.isNotEmpty()) {
            map["projectId"] = projectId
        }
        if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
            map["startTime"] = "$startTime 00:00:00"
            map["endTime"] = "$endTime 23:59:59"
        }
        map["pageSize"] = "10"
        map["page"] = pageSize
//            }
//            else -> {
//            }
//        }
        return map
    }

    override fun getUrl(url_type: Int): String {


        return when (url_type) {
            1 -> TaskManageImp.API_TASK_MANAGER_TARGET_TASKPAGE
            2 -> TaskManageImp.API_TASK_MANAGER_EVENT_TASK_PAGE
            else -> {
                ""
            }
        } //
    }


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
        mDatePicker1?.onDestroy()
    }

}