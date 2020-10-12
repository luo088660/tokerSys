package com.toker.sys.view.home.activity.my.projtdeta.viewshift

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.util.Log
import android.view.View
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.AttendSchedulImp
import com.toker.sys.utils.tools.DataUtil
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.adapter.ViewShiftAdapter
import com.toker.sys.view.home.activity.my.bean.ViewShiftBean
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_date_time.*
import kotlinx.android.synthetic.main.layout_view_shift_01.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 查看排班明细
 * @author yyx
 */

class ViewShiftActivity : BaseActivity<ViewShiftContract.View, ViewShiftPresenter>(), ViewShiftContract.View,
    CalendarView.OnCalendarSelectListener {

    var mBeans: MutableList<ViewShiftBean.Work> = mutableListOf()
    var mBeans1: MutableList<ViewShiftBean.Work> = mutableListOf()

    var shiftAdapter: ViewShiftAdapter? = null
    var shiftAdapter1: ViewShiftAdapter? = null
//    val mBeans: MutableList<ViewShiftBean.Work> = mutableListOf()
    private val sdf = SimpleDateFormat("yyyy-MM-dd")
    override fun onCalendarOutOfRange(calendar: Calendar?) {

    }

    var day: String = ""
    var projectId: String = ""
    var curMonth: Int? = 0
    var year: Int? = 0
    @SuppressLint("SetTextI18n")
    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        LogUtils.d(TAG, "isClick:$isClick ");
        curMonth = calendar?.month
        year = calendar?.year
        val Day = calendar?.day
        if (curMonth != null) {
            tv_time.text = "$year-${if (curMonth!! < 10) "0$curMonth" else curMonth.toString()}"
            if (isClick) {
                day =
                    "$year-${if (curMonth!! < 10) "0$curMonth" else curMonth.toString()}-${if (Day!! < 10) "0$Day" else Day.toString()}"
                mPresenter.showTime()
            } else {
                day =  "$year-${if (curMonth!! < 10) "0$curMonth" else curMonth.toString()}-01"
                mPresenter.loadRepositories()
            }

        }
    }

    override var mPresenter: ViewShiftPresenter = ViewShiftPresenter()

    override fun layoutResID(): Int = R.layout.activity_view_shift

    override fun initView() {
        projectId = intent.getStringExtra("projectId")
        tv_title.text = "排班计划"
        cv_time_view.setOnCalendarSelectListener(this)
        val curMonth = cv_time_view.curMonth
        tv_time.text = "${cv_time_view.curYear}-${if (curMonth < 10) "0$curMonth" else curMonth.toString()}"
        day = sdf.format(Date(System.currentTimeMillis()))
        mPresenter.loadRepositories()
        rv_view_shift2_01.layoutManager = GridLayoutManager(this, 1)
        rv_view_shift2_02.layoutManager = GridLayoutManager(this, 1)

        shiftAdapter = ViewShiftAdapter(this, mBeans)
        shiftAdapter1 = ViewShiftAdapter(this, mBeans1)
        rv_view_shift2_01.adapter = shiftAdapter
        rv_view_shift2_02.adapter = shiftAdapter1

    }

    override fun initData() {
        year = cv_time_view.curYear
        curMonth = cv_time_view.curMonth
        setOnClickListener(img_time_right)
        setOnClickListener(img_time_left)
        setOnClickListener(img_back)
        setOnClickListener(tv_view_shif2_04)
        setOnClickListener(tv_view_shif2_05)
    }

    /**
     * 某一天的排班计划
     */
    override fun DetailByDay(data: ViewShiftBean.Data) {

        tv_view_shif2_00.text = "$day\t ${DataUtil.getWeek(day)}"
        tv_view_shif2_01.text = data.address
        tv_view_shif2_02.text = data.standardCheckInTime
        tv_view_shif2_03.text = data.standardCheckOutTime

        tv_view_shif2_04.text =
            Html.fromHtml(String.format(resources.getString(R.string.tip_task_number_05), data.workList.size))
        tv_view_shif2_05.text =
            Html.fromHtml(String.format(resources.getString(R.string.tip_task_number_06), data.personalLeaveList.size))

        shiftAdapter?.refreshView(data.workList)
        shiftAdapter1?.refreshView(data.personalLeaveList)
        rv_view_shift2_01.visibility = GONE
        rv_view_shift2_02.visibility = GONE

    }

    override fun onErrorVData() {

        tv_view_shif2_00.text = "$day\t ${DataUtil.getWeek(day)}"
        tv_view_shif2_01.text = "暂无数据"
        tv_view_shif2_02.text = "暂无数据"
        tv_view_shif2_03.text = "暂无数据"

        tv_view_shif2_04.text =
            Html.fromHtml(String.format(resources.getString(R.string.tip_task_number_05), "0"))
        tv_view_shif2_05.text =
            Html.fromHtml(String.format(resources.getString(R.string.tip_task_number_06), "0"))
        shiftAdapter?.refreshView(mBeans)
        shiftAdapter1?.refreshView( mBeans1)

    }
    override fun showTime(data: MutableList<String>) {
        schemeDate(data)
    }

    private fun schemeDate(ydata: MutableList<String>) {
        val map = HashMap<String, Calendar>()
        for (i in ydata) {
            map[getSchemeCalendar(year!!, curMonth!!, i.toInt(), -0xbf24db, "").toString()] =
                getSchemeCalendar(year!!, curMonth!!, i.toInt(), -0xbf24db, "")//"事"
        }
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        cv_time_view.setSchemeDate(map)
    }

    override fun onErrorData(desc: String) {
        toast(desc)
    }

    var isfoVisiBtn1 = false
    var isfoVisiBtn2 = false
    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_time_right -> {
                cv_time_view.onRight()
            }
            img_time_left -> {
                cv_time_view.onLeft()
            }
            img_back -> finish()
            tv_view_shif2_04 -> {
                val drawable =
                    resources.getDrawable(if (!isfoVisiBtn1) R.mipmap.icon_xxjt else R.mipmap.icon_xsjt )
                rv_view_shift2_01.visibility = if (!isfoVisiBtn1) VISIBLE else GONE
                isfoVisiBtn1 = !isfoVisiBtn1
                tv_view_shif2_04.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
            }
            tv_view_shif2_05 -> {
                val drawable =
                    resources.getDrawable(if (!isfoVisiBtn2)  R.mipmap.icon_xxjt else R.mipmap.icon_xsjt)
                rv_view_shift2_02.visibility = if (!isfoVisiBtn2) VISIBLE else GONE
                isfoVisiBtn2 = !isfoVisiBtn2
                tv_view_shif2_05.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
            }
            else -> {
            }
        }
    }

    override fun getUrl(url_type: Int): String {
        val map = mutableMapOf<String, String>()
        return when (url_type) {
            1 -> {
                AttendSchedulImp.API_CUST_ATTEND_LIST_DAY
            }
            2 -> {
                AttendSchedulImp.API_CUST_ATTEND_DETAIL_BY_DAY
            }
            else -> {
                ""
            }
        }
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                map["projectId"] = projectId
                map["month"] = tv_time.text.toString()
            }
            2 -> {
                map["date"] = day
                map["projectId"] = projectId
                map["month"] = tv_time.text.toString()
            }
            else -> {
            }
        }
        return map
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    private fun getSchemeCalendar(year: Int, month: Int, day: Int, color: Int, text: String): Calendar {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color//如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text
        return calendar
    }
}
