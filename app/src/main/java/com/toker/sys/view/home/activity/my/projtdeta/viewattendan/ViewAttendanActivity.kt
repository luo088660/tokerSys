package com.toker.sys.view.home.activity.my.projtdeta.viewattendan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.toker.sys.R
import com.toker.sys.dialog.ProjectListDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.AttendSchedulImp
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.tools.DataUtil
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.adapter.AttendAdapter
import com.toker.sys.view.home.activity.adapter.ViewAttendanAdapter
import com.toker.sys.view.home.activity.my.bean.ProjectByTeamBean
import com.toker.sys.view.home.activity.my.bean.ViewAttenListBean
import com.toker.sys.view.home.activity.my.bean.ViewAttenListBean1
import com.toker.sys.view.home.activity.my.bean.ViewAttenTeamBean
import com.toker.sys.view.home.activity.my.projtdeta.attenddetail.AttendDetailActivity
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.fragment.my.item.bean.GroupListBean
import kotlinx.android.synthetic.main.activity_view_attendan.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_view_attendan_01.*
import kotlinx.android.synthetic.main.layout_view_attendan_02.*
import kotlinx.android.synthetic.main.layout_view_attendan_03.*
import kotlinx.android.synthetic.main.layout_view_attendan_04.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * 查看考勤明细
 * @author yyx
 */

class ViewAttendanActivity : BaseActivity<ViewAttendanContract.View, ViewAttendanPresenter>(),
    ViewAttendanContract.View {

    override var mPresenter: ViewAttendanPresenter = ViewAttendanPresenter()
    private var mGroupList: ArrayList<GroupListBean.Data> = arrayListOf()
    private var dataProJect: MutableList<Data> = mutableListOf()
    private var isType: Boolean = false
    private var timeType: Int = 2
    private var projectId = ""
    private var groupId = ""
    private var mBeans: MutableList<ViewAttenTeamBean.PageData> = mutableListOf()
    private var adapter: ViewAttendanAdapter? = null
    private var mBeans1: MutableList<ViewAttenListBean1.Data> = mutableListOf()
    private var mBeans2: MutableList<ViewAttenListBean1.Data> = mutableListOf()
    private var adapter1: AttendAdapter? = null
    private var adapter2: AttendAdapter? = null
    @SuppressLint("SimpleDateFormat")
    private val sdfY = SimpleDateFormat(DateFormatUtils.DATE_FORMAT_PATTERN_Y)
    @SuppressLint("SimpleDateFormat")
    private val sdfM = SimpleDateFormat(DateFormatUtils.DATE_FORMAT_PATTERN_MM)
    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat(DateFormatUtils.DATE_FORMAT_PATTERN_YMD)
    private val sdfYM = SimpleDateFormat(DateFormatUtils.DATE_FORMAT_PATTERN_YM)
    private var isfoVisiBtn1 = false
    private var isfoVisiBtn2 = false
    private var page = 1
    private var condition = ""
    private var year = sdfY.format(Date(System.currentTimeMillis()))
    private var month = sdfM.format(Date(System.currentTimeMillis()))
    private var date = sdf.format(Date(System.currentTimeMillis()))
    private val formatType1 = "yyyy-MM-dd"
    private val formatType2 = "yyyy-MM"
    private var mDatePicker1: CustomDatePicker? = null
    private var timestamp = System.currentTimeMillis()
    override fun layoutResID(): Int = R.layout.activity_view_attendan

    override fun initView() {
        EventBus.getDefault().register(this)
        tv_title.text = "考勤统计"

        isType = intent.getBooleanExtra("isType", false)
        if (isType) {
            //拓客组长 我的团队
            try {
                mGroupList = intent.getParcelableArrayListExtra<GroupListBean.Data>("mGroupList")
                tv_ver_01.text = "全部团队"
                LogUtils.d(TAG, "mGroupList:$mGroupList ");

                mGroupList.forEach {
                    dataProJect.add(Data(it.id, it.projectId, it.groupName, ""))
                }
                groupId = mGroupList[0]!!.projectId
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            projectId = intent.getStringExtra("projectId")
            val projectName = intent.getStringExtra("projectName")
            tv_ver_01.text = projectName
            mPresenter.getProjectList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        setOnClickListener(img_back)
        setOnClickListener(ll_view_attendan)
        setOnClickListener(tv_ver_01)
        setOnClickListener(tv_view_atte4_01)
        setOnClickListener(tv_view_atte4_02)
        setOnClickListener(tv_receivde1_02)
        setOnClickListener(img_ver_03)
        mPresenter.loadRepositories()
        rv_view_attendan.layoutManager = GridLayoutManager(this, 1)
        adapter = ViewAttendanAdapter(this, mBeans)
        rv_view_attendan.adapter = adapter

        rv_view_atte4_01.layoutManager = GridLayoutManager(this, 1)
        rv_view_atte4_02.layoutManager = GridLayoutManager(this, 1)
        //考勤情况

        adapter1 = AttendAdapter(this, mBeans1)
        adapter2 = AttendAdapter(this, mBeans2)
        rv_view_atte4_01.adapter = adapter1
        rv_view_atte4_02.adapter = adapter2
        tv_ver_02.text =
            sdfYM.format(Date(DataUtil.stringToLong(date, formatType1))).replace("-", "/")
        initDatePicker()
        initSpringView(sv_view_attendan)
    }

    override fun onFRefresh() {
        super.onFRefresh()
        page = 1
        mBeans1.clear()
        mBeans2.clear()
        mBeans.clear()

        if (timeType == 2) mPresenter.loadRepositories() else mPresenter.ListByDay()

    }

    override fun onFLoadmore() {
        super.onFLoadmore()
        page++
        if (timeType == 2) {
            mPresenter.loadRepositories()
        } else {
//            mPresenter.ListByDay()
        }
    }

    /**
     * 项目或者团队考勤统计
     */
    override fun byTeamData(data: ProjectByTeamBean.Data) {
        tv_view_atten2_01.text = "${tv_ver_02.text}\t实出勤${data.totalPeople}人"
        tv_attend_sta_01.text =
            if (data.totalCheckInNum.isNullOrEmpty()) "0" else "${data.totalCheckInNum}"
        tv_attend_sta_02.text =
            if (data.totalCheckOutNum.isNullOrEmpty()) "0" else "${data.totalCheckOutNum}"
        tv_attend_sta_03.text = if (data.regularNum.isNullOrEmpty()) "0" else "${data.regularNum}"
        tv_attend_sta_04.text =
            if (data.exceptionNum.isNullOrEmpty()) "0" else "${data.exceptionNum}"
    }


    /**
     * 指定月份员工考勤统计列表
     */
    override fun showDataList(data: ViewAttenTeamBean.Data) {
//        mBeans.clear()
        mBeans.addAll(data.pageData)
        adapter!!.refreshData(mBeans)

    }

    /**
     * 指定日期员工考勤列表
     */
    override fun byDayListData(data: MutableList<ViewAttenListBean1.Data>) {
//        mBeans1.clear()
//        mBeans2.clear()
        data.forEach {//it.checkInStatus == "1" && it.checkOutStatus == "1"
            if (it.status == "0" ) {
                mBeans2.add(it)
            } else {
                mBeans1.add(it)
            }
        }
        tv_view_atte4_01.text =
            Html.fromHtml(
                String.format(
                    resources.getString(R.string.tip_task_number_07),
                    mBeans1.size
                )
            )
        tv_view_atte4_02.text =
            Html.fromHtml(
                String.format(
                    resources.getString(R.string.tip_task_number_08),
                    mBeans2.size
                )
            )
        adapter1!!.refreshData(mBeans1)
        adapter2!!.refreshData(mBeans2)

        val totalPeople = data.size

        var totalCheckInNum = 0
        var totalCheckOutNum = 0
        var regularNum = 0
        var exceptionNum = 0
        data.forEach {
            if (!it.checkInTime.isNullOrEmpty()) totalCheckInNum++
            if (!it.checkOutTime.isNullOrEmpty()) totalCheckOutNum++
            if (!it.checkInStatus.isNullOrEmpty() && !it.checkOutStatus.isNullOrEmpty()) {
                if (it.checkInStatus == "1" || it.checkOutStatus == "1") regularNum++ else exceptionNum++
            }
        }
        tv_view_atten2_01.text = "${tv_ver_02.text}实出勤${totalCheckInNum}人"
        tv_attend_sta_01.text = "$totalCheckInNum"
        tv_attend_sta_02.text = "$totalCheckOutNum"
        tv_attend_sta_03.text = "${mBeans1.size}"
        tv_attend_sta_04.text = "${mBeans2.size}"

    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            //查询
            tv_receivde1_02 -> {
                condition = "${tv_receivde1_01.text}"
                onFRefresh()
            }

            //选择项目
            //选择团队
            tv_ver_01 -> {
                val distinctBy: MutableList<Data>  =  mutableListOf()
                distinctBy.addAll(this.dataProJect!!.distinctBy { it })
                ProjectListDialog(this, distinctBy, isType)
            }
            //切换日
            //切换月
            img_ver_03 -> {
                var dateStr = tv_ver_02.text.toString()
                dateStr = dateStr.replace("/", "-")
                mDatePicker1!!.show(timestamp
                   /* sdf.format(
                        if (dateStr.length > 7) {
                            Date(DataUtil.stringToLong(dateStr, formatType1))
                        } else {
                            Date(DataUtil.stringToLong(dateStr, formatType2))
                        }
                    )*/
                )
            }
            tv_view_atte4_01 -> {
                val drawable =
                    resources.getDrawable(if (!isfoVisiBtn1) R.mipmap.icon_xxjt else R.mipmap.icon_xsjt)
                rv_view_atte4_01.visibility = if (!isfoVisiBtn1) VISIBLE else GONE
                isfoVisiBtn1 = !isfoVisiBtn1
                tv_view_atte4_01.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    drawable,
                    null
                )
            }
            tv_view_atte4_02 -> {
                val drawable =
                    resources.getDrawable(if (!isfoVisiBtn2) R.mipmap.icon_xxjt else R.mipmap.icon_xsjt)
                rv_view_atte4_02.visibility = if (!isfoVisiBtn2) VISIBLE else GONE
                isfoVisiBtn2 = !isfoVisiBtn2
                tv_view_atte4_02.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null,
                    null,
                    drawable,
                    null
                )
            }
            else -> {
            }
        }
    }

    /**
     * 查看考勤详情
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun PageData(event: ViewAttenListBean.BeanEvent) {
        val intent = Intent(this, AttendDetailActivity::class.java)
        intent.putExtra("time","${ tv_ver_02.text}".replace("/","-"))
        intent.putExtra("event", event)
        startActivity(intent)
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                if (isType) map["groupId"] = groupId else map["projectId"] = projectId
//                if (isType) map["groupId"] = "66a2dbd2-d184-49f7-824c-772eb28422d6" else map["projectId"] = projectId
                map["year"] = year
                map["month"] = month

            }
            2 -> {
                if (isType) map["groupId"] = groupId else map["projectId"] = projectId
                map["year"] = year
                map["month"] = month
                map["pageSize"] = "10"
                map["page"] = "$page"
                if (!TextUtils.isEmpty(condition)) {
                    map["condition"] = condition
                }
            }
            3 -> {
                map["projectId"] = projectId
                map["date"] = date
//                map["pageSize"] = "10"
//                map["page"] = "$page"
            }
            else -> {
            }
        }
        return map
    }


    //我的项目
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showProjectDate(data: Data) {
        tv_ver_01.text = data.projectName
        LogUtils.d(TAG, "data:$data ");
        LogUtils.d(TAG, "isType:$isType ");
        if (isType) groupId = data.id else projectId = data.projectId
        onFRefresh()
//        if (timeType == 2) mPresenter.loadRepositories() else mPresenter.ListByDay()
    }

    override fun getUrl(url_type: Int): String {

        return when (url_type) {
            1 -> AttendSchedulImp.API_CUST_ATTEND_BY_TEAM
            2 -> AttendSchedulImp.API_CUST_ATTEND_LIST_BY_MONTH
            3 -> AttendSchedulImp.API_CUST_ATTEND_LIST_BY_DAY
            4 -> SystemSettImp.API_SYSTEM_PROJECT_LIST
            else -> {
                ""
            }
        }
    }

    override fun showProjectDate(data: MutableList<Data>) {
        this.dataProJect = data
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }


    /**
     * 时间选择
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {
        // 通过时间戳初始化日期，毫秒级别
        mDatePicker1 = CustomDatePicker(this, object : CustomDatePicker.CallbackListen {
            override fun onTimeSelected(timestamp: Long, timeType: Int) {
                this@ViewAttendanActivity.timestamp = timestamp
                tv_ver_02.text =
                    DateFormatUtils.long2Str(timestamp, false, timeType).replace("-", "/")
                year = sdfY.format(Date(timestamp))
                month = sdfM.format(Date(timestamp))
                date = sdf.format(Date(timestamp))
                this@ViewAttendanActivity.timeType = timeType
                ll_view_attendan_03.visibility = if (timeType == 2) VISIBLE else GONE
                ll_view_attendan_04.visibility = if (timeType == 2) GONE else VISIBLE
                onFRefresh()
//                if (timeType == 2) mPresenter.loadRepositories() else mPresenter.ListByDay()

            }
        }, if (isType) 2 else 3, isVisiTab = !isType, isSEndTime = false)
        // 不允许点击屏幕或物理返回键关闭
//        mDatePicker1?.setCancelable(false)
        // 不显示时和分
        mDatePicker1?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mDatePicker1?.setScrollLoop(true)
        // 不允许滚动动画
        mDatePicker1?.setCanShowAnim(false)


    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
