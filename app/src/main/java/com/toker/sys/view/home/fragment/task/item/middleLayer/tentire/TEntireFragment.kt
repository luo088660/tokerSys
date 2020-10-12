package com.toker.sys.view.home.fragment.task.item.middleLayer.tentire

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.dialog.task.AllStateDialog
import com.toker.sys.dialog.task.AllTaskTypeDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.TaskManageImp.Companion.API_TASK_MIDDLE_TARGET_TASK_PAGE
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.task.event.AllStateEvent
import com.toker.sys.view.home.activity.task.event.AllTaskTypeEvent
import com.toker.sys.view.home.activity.task.event.ScrollTopEvent
import com.toker.sys.view.home.fragment.event.TaskHomeEvent
import com.toker.sys.view.home.fragment.task.adapter.TEntireAdapter
import com.toker.sys.view.home.fragment.task.bean.Data
import com.toker.sys.view.home.fragment.task.bean.PageData
import kotlinx.android.synthetic.main.fragment_t_entire.*
import kotlinx.android.synthetic.main.fragment_t_received.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

/**
 * 拓客中层
 * 全部，我创建的
 * @author yyx
 */

class TEntireFragment : BaseFragment<TEntireContract.View, TEntirePresenter>(),
    TEntireContract.View {

    private var mDatePicker1: CustomDatePicker? = null
    private var mDatePicker2: CustomDatePicker? = null
    private var timeType = 3
    private val thisTimestamp = System.currentTimeMillis()
    var adapter: TEntireAdapter? = null
    private var type: String? = ""
    private var mBeans = mutableListOf<PageData>()
    private var pageSize = "1"
    private var startTime: String = ""
    private var endTime: String = ""
    private val sdf = SimpleDateFormat("yyyy/MM/dd")
    private val sdf1 = SimpleDateFormat("yyyy-MM-dd")
    private var tv01: TextView? = null
    private var tv02: TextView? = null
    private var tv03: TextView? = null
    private var tv04: TextView? = null
    private var tv05: TextView? = null
    private var img05: ImageView? = null
    private var tv07: TextView? = null
    var status = ""
    var projectId = ""
    var taskType = ""

    companion object {
        @JvmStatic
        fun newInstance(): TEntireFragment {
            val args = Bundle()
            val fragment = TEntireFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var mPresenter: TEntirePresenter = TEntirePresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_t_entire, container!!)
        }
        return main_layout!!
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun initView() {
        type = arguments?.getString("type")
        mPresenter.loadRepositories()
        EventBus.getDefault().register(this)
        rv_t_entire.layoutManager = GridLayoutManager(context, 1)
        initRecyclerView()

    }


    private fun initRecyclerView() {

        val view = LayoutInflater.from(context).inflate(R.layout.layout_t_entire_011, null)
        tv01 = view.findViewById<TextView>(R.id.tv_entire_01)
        tv02 = view.findViewById<TextView>(R.id.tv_entire_02)
        tv03 = view.findViewById<TextView>(R.id.tv_entire_03)
        tv04 = view.findViewById<TextView>(R.id.tv_entire_04)
        tv05 = view.findViewById<TextView>(R.id.tv_entire_05)
        img05 = view.findViewById<ImageView>(R.id.img_entire_05)
        tv07 = view.findViewById<TextView>(R.id.tv_entire_07)
        setOnClickListener(tv01!!)
        setOnClickListener(tv02!!)
        setOnClickListener(tv03!!)
        setOnClickListener(tv04!!)
        setOnClickListener(img05!!)
        setOnClickListener(tv05!!)
        adapter = TEntireAdapter(context!!, mBeans)
        rv_t_entire.adapter = adapter
        adapter!!.setHeaderView(view)
//        rv_t_entire.addItemDecoration(getItemDecoration())
        initSpringView(sp_t_entire)
    }

    override fun onFRefresh() {
        //刷新
        pageSize = "1"
        mPresenter.loadRepositories()
        mBeans.clear()

    }

    override fun onFLoadmore() {
        //加载更多
        var toInt = pageSize.toInt()
        toInt++
        pageSize = toInt.toString()
        mPresenter.loadRepositories()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        //初始化时间
        initDatePicker()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        //当前时间
        val long2Str = DateFormatUtils.long2Str(thisTimestamp, false, this@TEntireFragment.timeType)
        when (v) {
            //月指标考核任务 查看详情 MonthIndicAssFragment
//            tv_entire_see_deta -> {
//
//            }
            //全部项目
            tv01 -> EventBus.getDefault().post(TaskHomeEvent(3))
            //全部任务类型 AllTaskTypeDialog
            tv02 -> {
                AllTaskTypeDialog(context!!)
            }
            //全部状态AllStateDialog
            tv03 -> {
                AllStateDialog(context!!)
            }
            //任务开始日期
            tv04,
            tv05,
            img05 -> {
                mDatePicker1!!.show(System.currentTimeMillis())
            }

            else -> {
            }
        }
    }
    //平滑到顶部
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun scrollToPosition(x: ScrollTopEvent) {
        rv_t_entire.scrollToPosition(0)
    }
    //全部项目
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
                tv02?.text = event.name
                taskType = ""
            }
            else -> {
                tv02?.text = "${event.name}任务"
                taskType = "${event.type}"
            }
        }
        onFRefresh()
    }

    /**
     * 全部状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun AllStateEvent(event: AllStateEvent) {
        tv03?.text = event.name
        status = if (event.type == -1) "" else "${event.type}"
        onFRefresh()
    }

    /**
     * 数据列表
     */
    override fun targetTaskPage(pageData: Data) {
        mBeans.addAll(pageData.pageData)
        adapter!!.refreshData(mBeans)
        tv07!!.text = Html.fromHtml(String.format(getString(R.string.tip_task), pageData.rowTotal))
    }

    /**
     * 时间选择
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {
        // 通过时间戳初始化日期，毫秒级别
        mDatePicker1 = CustomDatePicker(context, object : CustomDatePicker.CallbackSEListen {
            override fun onTimeSelected(
                startTime: Long, endTime: Long, timeType: Int ) {
                if( startTime == 0L||endTime==0L){
                    return
                }

                LogUtils.d(TAG, "startTime:$startTime ");
                LogUtils.d(TAG, "endTime: $endTime");
                tv04!!.text = sdf.format(Date(startTime))
                tv05!!.text = sdf.format(Date(endTime))
                this@TEntireFragment.startTime = sdf1.format(Date(startTime))
                this@TEntireFragment.endTime = sdf1.format(Date(endTime))
                onFRefresh()
            }

        }, 0, isVisiTab = false, isSEndTime = true)


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
        when (url_type) {
            1 -> {
                if (taskType.isNotEmpty()) {
                    map["taskType"] = taskType
                }
                map["tag"] = type!!
                if (status.isNotEmpty()) {
                    map["status"] = status
                }
                if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
                    map["startTime"] = "$startTime 00:00:00"
                    map["endTime"] = "$endTime 23:59:59"
                }
                map["pageSize"] = "10"
                if (projectId.isNotEmpty()) {
                    map["projectId"] = projectId
                }
                map["page"] = pageSize
            }
            else -> {
            }
        }
        return map
    }

    override fun getUrl(url_type: Int): String {

        return API_TASK_MIDDLE_TARGET_TASK_PAGE
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
        mDatePicker1?.onDestroy()
        mDatePicker2?.onDestroy()
    }


}// Required empty public constructor