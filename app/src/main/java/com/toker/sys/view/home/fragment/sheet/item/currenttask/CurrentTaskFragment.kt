package com.toker.sys.view.home.fragment.sheet.item.currenttask

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.AppApplication
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.dialog.sheet.TurnoverDialog
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.home.activity.sheet.event.STProjectRanEvent
import com.toker.sys.view.home.activity.task.transacttask.TransactTaskActivity
import com.toker.sys.view.home.activity.task.transareporttask.TransaReportTaskActivity
import com.toker.sys.view.home.bean.STExtenRankBean
import com.toker.sys.view.home.fragment.sheet.adapter.CurrentTaskAdapter
import com.toker.sys.view.home.fragment.sheet.event.CurrentTaskEvent
import com.toker.sys.view.home.fragment.task.item.controller.treceived.Data
import kotlinx.android.synthetic.main.activity_s_t_project_ran.*
import kotlinx.android.synthetic.main.fragment_current_task.*
import kotlinx.android.synthetic.main.layout_current_task_01.*
import kotlinx.android.synthetic.main.layout_current_task_02.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*


/**
 * 拓客员 拓客组长
 * 当前任务
 * @author yyx
 */

class CurrentTaskFragment : BaseFragment<CurrentTaskContract.View, CurrentTaskPresenter>(),
    CurrentTaskContract.View {
    val sdf = SimpleDateFormat("yyyy-MM-dd")

    companion object {
        @JvmStatic
        fun newInstance(): CurrentTaskFragment {
            val args = Bundle()
            val fragment = CurrentTaskFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mBeans: MutableList<STExtenRankBean.Record> = mutableListOf()
    private var type = "2"
    private var time = DateFormatUtils.long2Str(System.currentTimeMillis(), false, 2)
    private var item: String = "4"
    private var sortWay: String = "desc"
    private var sortBy: String = "1"
    private var page: Int = 1
    private val sdfD = SimpleDateFormat("yyyy/MM/dd")
    var datePicker: CustomDatePicker? = null
    private var adapter: CurrentTaskAdapter? = null
    private var drawableS: Drawable? = null
    private var drawableX: Drawable? = null

    private var drawableM: Drawable? = null
    var isOrder = false
    override var mPresenter: CurrentTaskPresenter = CurrentTaskPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_current_task, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)

        rv_current_task.layoutManager = GridLayoutManager(context, 1)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.loadRepositories()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        adapter = CurrentTaskAdapter(context!!, mBeans,item)
        rv_current_task.adapter = adapter
        drawableS = resources.getDrawable(R.mipmap.icon_xspx)
        drawableX = resources.getDrawable(R.mipmap.icon_xxpx)
        drawableM = resources.getDrawable(R.mipmap.icon_mrpx)
        // / 这一步必须要做,否则不会显示.
        drawableS!!.setBounds(0, 0, drawableS!!.minimumWidth, drawableS!!.minimumHeight)
        drawableX!!.setBounds(0, 0, drawableX!!.minimumWidth, drawableX!!.minimumHeight)
        drawableM!!.setBounds(0, 0, drawableM!!.minimumWidth, drawableM!!.minimumHeight)

        setOnClickListener(btn_layout_current_task_01)
        setOnClickListener(tv_current_task2_01)
        setOnClickListener(tv_current_task2_02)
//        setOnClickListener(tv_current_task2_03)
        initDatePicker()
        initSpringView(sv_current_task)
        tv_current_task2_01.text = time.replace("-", "/")
        onFRefresh()
        if (!TextUtils.isEmpty(AppApplication.COMPANY)){
            tv_current_lhban.text = "${AppApplication.COMPANY}龙虎版"
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            //查看全部任务
            btn_layout_current_task_01 -> {
                EventBus.getDefault().post(MainEvent(17))
            }
            //时间
            tv_current_task2_01 -> datePicker?.show(System.currentTimeMillis())
            //成交额
            tv_current_task2_02 -> TurnoverDialog(context!!)
            //排序
            tv_current_task2_03 -> {
                tv_current_task2_03.setCompoundDrawables(
                    null,
                    null,
                    if (isOrder) drawableS else drawableX,
                    null
                )
                sortWay = if (isOrder) "asc" else "desc"
                onFRefresh()
                isOrder = !isOrder
            }
            else -> {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun CurrentTaskEvent(event: CurrentTaskEvent){
        onFRefresh()
    }
    override fun onFRefresh() {
        super.onFRefresh()
        mBeans.clear()
        page = 1
        mPresenter.getUserCompanyRank()
    }
    override fun onFLoadmore() {
        super.onFLoadmore()
        page ++
        mPresenter.getUserCompanyRank()
    }

    //数据展示
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    override fun eventTaskPage(data: Data) {
        if (!data.pageData.isNullOrEmpty()) {
            ll_layout_current_task.visibility = VISIBLE
            tv_layout_current_task.visibility = GONE
            val list = data.pageData[0]
            btn_layout_current_task_03.text = list.taskName
            tv_current_task_05.text = list.projectName
            btn_layout_current_task_05.text =
                "${sdf.format(Date(list.startDate))}至${sdf.format(Date(list.endDate))}\t${list.startTime}~${list.endTime}"
            btn_layout_current_task_07.text = list.address
            btn_layout_current_task_08.text = list.content
            btn_layout_current_task_09.text = list.objectList.joinToString { it.objectName }
            tv_t_entire3_03.text = when (list.status.toInt()) {
                1 -> {
                    tv_t_entire3_03.setTextColor(context!!.resources.getColor(R.color.c_txt_weikaishi))
                    tv_t_entire3_03.setBackgroundColor(context!!.resources.getColor(R.color.c_bg_weikaishi))
                    "未开始"
                }
                2 -> {
                    tv_t_entire3_03.setTextColor(context!!.resources.getColor(R.color.c_txt_yirenchou))
                    tv_t_entire3_03.setBackgroundColor(context!!.resources.getColor(R.color.c_bg_yirenchou))
                    "进行中"
                }
                3 -> {
                    tv_t_entire3_03.setTextColor(context!!.resources.getColor(R.color.c_txt_tjchenggong))
                    tv_t_entire3_03.setBackgroundColor(context!!.resources.getColor(R.color.c_bg_tjchenggong))
                    "已完成"
                }
                else -> {
                    "草稿中"
                }
            }
            val isHasRecord = list.hasRecord == "1"
            btn_t_entire_03_01.background = context!!.resources.getDrawable(if (isHasRecord)R.drawable.btn_bg_login_normal else R.drawable.btn_bg_quit_login_normal,null)
            btn_t_entire_03_01.isEnabled = !isHasRecord
            //填写任务汇报
            btn_t_entire_03_01.setOnClickListener {
                val intent = Intent(activity, TransaReportTaskActivity::class.java)
                intent.putExtra("id", list.id)
                intent.putExtra("tableTag", list.tableTag)
                activity!!.startActivity(intent)
            }
            //查看任务详情
            btn_layout_current_task_02.setOnClickListener {
                val intent = Intent(context, TransactTaskActivity::class.java)
                intent.putExtra("type", 2)
                intent.putExtra("taskId", list.id)
                intent.putExtra("userId", "")
                intent.putExtra("tableTag", list.tableTag)
                intent.putExtra("updateTime", list.updateTime)
                context!!.startActivity(intent)
            }
        } else {
            ll_layout_current_task.visibility = GONE
            tv_layout_current_task.visibility = VISIBLE
        }
    }

    override fun showDataList(data: STExtenRankBean.Data) {
        mBeans.addAll(data.records)
        adapter?.refreshData(mBeans,item)

    }


    //成交额
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun stProjectRanEvent(event: STProjectRanEvent) {
        tv_current_task2_02.text = event.name
        item = "${event.type}"
        tv_current_task2_03.text = event.name
        sortWay = "desc"
//        tv_current_task2_03?.setCompoundDrawables(null, null, drawableM, null  )
        onFRefresh()
    }

    /**
     * 时间选择
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {
        datePicker = CustomDatePicker(context, object : CustomDatePicker.CallbackListen {
            override fun onTimeSelected(timestamp: Long, timeType: Int) {
                type = "$timeType"
                val time = DateFormatUtils.long2Str(timestamp, false, timeType)
                this@CurrentTaskFragment.time = time
                tv_current_task2_01.text = time.replace("-", "/")
              onFRefresh()
            }
        }, 4, isVisiTab = true, isSEndTime = false)

        // 通过时间戳初始化日期，毫秒级别
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }


    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                map["tag"] = "1"
                map["status"] = "2"
                map["pageSize"] = "10"
                map["page"] = "1"
            }
            2 -> {
                map["time"] = time
                map["type"] = type
                map["item"] = item //留电数:1,到访数:2,成交量:3,成交额:4"McfCustomDetailPresenter
                map["sortWay"] = sortWay//"sortBy不能为空,asc:正序;desc:倒序"
                map["sortBy"] = sortBy //完成：1，目标：2，完成率：3"
                map["pageSize"] = "10"//完成：1，目标：2，完成率：3"
                map["page"] = "$page" //完成：1，目标：2，完成率：3"
            }
            else -> {


            }
        }
        return map
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            //当前任务
            1 -> {
                TaskManageImp.API_TASK_PERSON_EVENT_TASK_PAGE
            }
            2 -> PerformStateImp.API_PER_USER_COMPANY_RANK
            else -> {
                ""
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
        datePicker?.onDestroy()
    }

}