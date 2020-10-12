package com.toker.sys.view.home.fragment.sheet.item.stsheet

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.home.activity.my.bean.ProjectFormanBean
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.fragment.my.item.bean.MainCountBean
import com.toker.sys.view.home.fragment.my.item.bean.ReadCountBean
import com.toker.sys.view.home.fragment.sheet.adapter.SheentTopAdapter
import com.toker.sys.view.home.fragment.sheet.adapter.SheetTitleAdapter
import com.toker.sys.view.home.fragment.sheet.event.CurrentTaskEvent
import com.toker.sys.view.home.fragment.sheet.event.SheetEvent
import com.toker.sys.view.home.fragment.sheet.item.attendtoday.AttendTodayFragment
import com.toker.sys.view.home.fragment.sheet.item.currenttask.CurrentTaskFragment
import com.toker.sys.view.home.fragment.sheet.item.topcustperfran.TopCustPerfRanFragment
import com.toker.sys.view.home.fragment.sheet.param.SheentTopBean
import kotlinx.android.synthetic.main.fragment_s_t_sheet.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_my_performance.*
import kotlinx.android.synthetic.main.layout_perfor.*
import kotlinx.android.synthetic.main.layout_t_received_01.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 首页 主页面
 * @author yyx
 */

class STSheetFragment : BaseFragment<STSheetContract.View, STSheetPresenter>(),
    STSheetContract.View {
    companion object {
        @JvmStatic
        fun newInstance(): STSheetFragment {
            val args = Bundle()
            val fragment = STSheetFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var type = "2"
    private var timestamp = System.currentTimeMillis()
    private var time = DateFormatUtils.long2Str(System.currentTimeMillis(), false, 2)
    private var mTitleLs = arrayListOf<SheentTopBean>()
    private var mBeans = arrayListOf<SheentTopBean>()
    private var inforCDatePicker: CustomDatePicker? = null
    private var titleAdapter: SheetTitleAdapter? = null
    private var topAdapter: SheentTopAdapter? = null
    override var mPresenter: STSheetPresenter = STSheetPresenter()

    override fun onNetworkLazyLoad() {
        LogUtils.d(TAG, "STSheetFragment: onNetworkLazyLoad")
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_s_t_sheet, container!!)
        }
        return main_layout!!
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initView() {
        EventBus.getDefault().register(this)
        ll_msg.visibility = VISIBLE
        tv_title.text = "首页"
        img_back.visibility = GONE
        initFragmentTabHost()
        setOnClickListener(btn_sheet)
        setOnClickListener(ll_msg)
        setOnClickListener(tv_ver_01)
        setOnClickListener(ll_my_performance_01)

        tv_t_sheet_01.text = when (AppApplication.TYPE) {
            Constants.RESCUE1 -> {
                tv_t_sheet_02.visibility = GONE
                "${AppApplication.USERNAME}\t拓客中层"
            }
            Constants.RESCUE2 -> {
                "${AppApplication.USERNAME}\t拓客管理员"
            }
            Constants.RESCUE3 -> {
                "${AppApplication.USERNAME}\t拓客员"
            }
            else -> {
                "${AppApplication.USERNAME}\t拓客组长"
            }
        }
        initDatePicker()
    }

    override fun initData() {
        tv_my_performance_01?.text = time.replace("-", "/")
        mPresenter.loadRepositories()
        //今日考勤  AttendTodayFragment
        if (AppApplication.TYPE == Constants.RESCUE3 || AppApplication.TYPE == Constants.RESCUE4) {
            setIntentFragment(R.id.fl_jrkq, AttendTodayFragment.newInstance(), Bundle())
            //当前任务 CurrentTaskFragment
            setIntentFragment(R.id.fl_dqrw, CurrentTaskFragment.newInstance(), Bundle())
        } else {
            //拓客业绩排名
            setIntentFragment(R.id.fl_dqrw, TopCustPerfRanFragment.newInstance(), Bundle())
        }
    }


    override fun onStart() {
        super.onStart()
        if (AppApplication.TYPE == Constants.RESCUE3 || AppApplication.TYPE == Constants.RESCUE4) {
            try {
                EventBus.getDefault().post(CurrentTaskEvent(true))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (!TextUtils.isEmpty(AppApplication.ICON)) {
            Glide.with(context!!).load(AppApplication.ICON)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(40)))//圆角半径
                .into(imt_st_sheet)
        }

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            //返回顶部
            btn_sheet -> sl_sheent.fullScroll(View.FOCUS_UP)
            //我的消息
            ll_msg -> EventBus.getDefault().post(MainEvent(16))
            //我的业绩时间选择
            ll_my_performance_01 -> {
                inforCDatePicker?.show(timestamp)
            }
            tv_ver_01 -> EventBus.getDefault().post(SheetEvent(9, 1))

            else -> {
            }
        }
    }

    //统计数据查询
    override fun showDataCount(data: MainCountBean.Data) {

        /* val toDistributedCustomerNum: Int,//待分配客户数量	int	管理员
         val toFollowCustomerNum: Int,    //待跟进客户数量	int	中层、管理员、组长、拓客员
         val toFollowTaskNum: Int,//待跟进任务数量	int	管理员
         val pendingTaskNum: Int,//	待审批任务数量	int	中层
         val myTaskNum: Int,//	我的任务数量	int	组长、拓客员
         val groupTaskNum: Int//	小组待跟进任务	int	组长*/
        for (i in mTitleLs) {
            when (i.name) {
                //待审批任务
                resources.getString(R.string.task_await_appr) -> i.resImg = data.pendingTaskNum
                //待跟进客户
                resources.getString(R.string.custom_foll_up1) -> i.resImg =
                    data.overFollowCustomerNum
                //待跟进任务
                resources.getString(R.string.task_foll_up) -> i.resImg = data.toFollowTaskNum
                //待分配客户
                resources.getString(R.string.custom_be_assig) -> i.resImg =
                    data.toDistributedCustomerNum
                //小组待跟进任务
                resources.getString(R.string.group_foll_task) -> i.resImg = data.groupTaskNum
                else -> {
                }
            }

        }
        if (AppApplication.TYPE == Constants.RESCUE3 || AppApplication.TYPE == Constants.RESCUE4) {
            for (i in mBeans) {
                i.resNum = data.myTaskNum
            }
            topAdapter?.refreshData(mBeans)
        }


        titleAdapter?.refreshData(mTitleLs)

    }

    //用户的业绩查询
    override fun preForMan(data: ProjectFormanBean.Data) {
        tv_ly_perfor_01.text =
            "${if (!TextUtils.isEmpty(data.curPhoneNum)) data.curPhoneNum else "0"}"
        tv_ly_perfor_02.text =
            "${if (!TextUtils.isEmpty(data.curVisitNum)) data.curVisitNum else "0"}"
        tv_ly_perfor_03.text =
            "${if (!TextUtils.isEmpty(data.curDealNum)) data.curDealNum else "0"}"
        tv_ly_perfor_04.text =
            "${if (!TextUtils.isEmpty(data.curTurnover)) data.curTurnover else "0"}"
    }

    override fun preErrorForMan() {
        tv_ly_perfor_01.text = "0"
        tv_ly_perfor_02.text = "0"
        tv_ly_perfor_03.text = "0"
        tv_ly_perfor_04.text = "0"
    }

    private fun initFragmentTabHost() {


//        var mTitleLs = arrayListOf<SheentTopBean>()
        val array = when (AppApplication.TYPE) {
            Constants.RESCUE1 -> resources.getStringArray(R.array.sheet_title_ls_1)
            Constants.RESCUE2 -> resources.getStringArray(R.array.sheet_title_ls_2)
            Constants.RESCUE3 -> resources.getStringArray(R.array.sheet_title_ls_3)
            else -> resources.getStringArray(R.array.sheet_title_ls_4)
        }
        for (i in array) {
            mTitleLs.add(SheentTopBean(i))
        }
        //待完成事务
        rv_sheet_title_ls.layoutManager = GridLayoutManager(activity, mTitleLs.size)
        titleAdapter = SheetTitleAdapter(activity!!, mTitleLs)
        rv_sheet_title_ls.adapter = titleAdapter



        when (AppApplication.TYPE) {
            Constants.RESCUE1,
            Constants.RESCUE2 -> {
                val arrayRes1 = resources.getStringArray(R.array.sheet_rescue1)
                val arrayResSrc1 = resources.obtainTypedArray(R.array.sheet_src_rescue1)
                for (i in 0 until arrayRes1.size) {
                    mBeans.add(SheentTopBean(arrayRes1[i], arrayResSrc1.getResourceId(i, 0)))
                }
            }
            Constants.RESCUE3 -> {
                val arrayRes2 = resources.getStringArray(R.array.sheet_rescue2)
                val arrayResSrc2 = resources.obtainTypedArray(R.array.sheet_src_rescue2)
                for (i in 0 until arrayRes2.size) {
                    mBeans.add(SheentTopBean(arrayRes2[i], arrayResSrc2.getResourceId(i, 0)))
                }
            }
            else -> {
                val arrayRes3 = resources.getStringArray(R.array.sheet_rescue3)
                val arrayResSrc3 = resources.obtainTypedArray(R.array.sheet_src_rescue3)
                for (i in 0 until arrayRes3.size) {
                    mBeans.add(SheentTopBean(arrayRes3[i], arrayResSrc3.getResourceId(i, 0)))
                }
            }
        }
        //我的相关
        rv_sheet.layoutManager = GridLayoutManager(activity!!, mBeans.size)
        topAdapter = SheentTopAdapter(activity!!, mBeans)
        rv_sheet.adapter = topAdapter

    }

    private var projectId = ""
    /**
     * 全部项目
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: Data) {
        if (event.stuta == 1) {
            tv_ver_01!!.text = event.projectName
            projectId = event.projectId
            mPresenter.getMyPerformance()

        }
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            //当前业绩查询
            1 -> PerformStateImp.API_PER_STATE_PER_FORMANCE
            2 -> TaskManageImp.API_TASK_NOREAD_COUNT
            3 -> TaskManageImp.API_MAIN_PAGE_COUNT
            else -> {
                ""
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun listData(data: List<Data>) {
        if (data.isNotEmpty()) {
            tv_t_sheet_02.text = data.joinToString {
                it.projectName
            }.replace(",", "/")

        }
    }

    //未读消息统计
    override fun showDataNumBer(data: ReadCountBean.Data) {

        tv_my_msg_01.visibility = if (data.countNum != 0) VISIBLE else GONE

    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val mapOf = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {

                mapOf["type"] = type

                when (type) {
                    "4" -> {
                        mapOf["beginDate"] = beginDate
                        mapOf["endDate"] = endDate
                    }
                    else -> {
                        mapOf["time"] = time
                    }
                }
                if (!projectId.isNullOrEmpty()) {
                    mapOf["projectId"] = projectId
                }

            }
            2 -> {
            }
            else -> {
            }
        }
        return mapOf
    }

    /**
     * 时间选择
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {

        // 通过时间戳初始化日期，毫秒级别
        /* inforCDatePicker = CustomDatePicker(context, object : CustomDatePicker.CallbackListen {
             override fun onTimeSelected(timestamp: Long, timeType: Int) {
                 this@STSheetFragment.timestamp = timestamp
                 type = "$timeType"
                 val time = DateFormatUtils.long2Str(timestamp, false, timeType)
                 this@STSheetFragment.time = time
                 tv_my_performance_01?.text = time.replace("-", "/")
                 mPresenter.getMyPerformance()

             }
         }, 4, isVisiTab = true, isSEndTime = false)*/

        // 通过时间戳初始化日期，毫秒级别
        inforCDatePicker = CustomDatePicker(context, object : CustomDatePicker.CallbackSEListen {
            override fun onTimeSelected(
                startTime: Long, endTime: Long, timeType: Int
            ) {
                Log.e(TAG, "timeType: $timeType");
                Log.e(TAG, "startTime: $startTime");
                Log.e(TAG, "endTime: $endTime");
                when (timeType) {
                    //按照 年月日 进行筛选
                    1, 2, 3 -> {
                        if (startTime == 0L) {
                            return
                        }
                        this@STSheetFragment.timestamp = startTime
                        type = "$timeType"

                        val time = DateFormatUtils.long2Str(timestamp, false, timeType)
                        this@STSheetFragment.time = time
                        tv_my_performance_01?.text = time.replace("-", "/")
                        mPresenter.getMyPerformance()

                    }
                    else -> {

                        if (startTime == 0L || endTime == 0L) {
                            return
                        }
                        this@STSheetFragment.timestamp = startTime
                        type = "4"
                        beginDate = DateFormatUtils.long2Str(startTime, false, timeType)
                        endDate = DateFormatUtils.long2Str(endTime, false, timeType)

                        tv_my_performance_01?.text =
                            "${beginDate.replace("-", "/")}\n${endDate.replace("-", "/")}"
                        mPresenter.getMyPerformance()
                    }
                }

            }

        }, 4, true, true)
    }

    var beginDate = ""
    var endDate = ""


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {

    }

    private fun setIntentFragment(srcId: Int, fragment: Fragment, bundle: Bundle) {
        val beginTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        beginTransaction.replace(srcId, fragment)
        beginTransaction.commitAllowingStateLoss()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        inforCDatePicker?.onDestroy()
        EventBus.getDefault().unregister(this)
        log("onDestroyView", "STSheetFragment")
    }
}