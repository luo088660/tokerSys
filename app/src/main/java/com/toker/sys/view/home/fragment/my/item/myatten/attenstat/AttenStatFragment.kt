package com.toker.sys.view.home.fragment.my.item.myatten.attenstat

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.CalendarView
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.AttendSchedulImp
import com.toker.sys.utils.tools.DataUtil
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.adapter.AttendDetailAdapter
import com.toker.sys.view.home.activity.my.bean.AttenDetailBean
import com.toker.sys.view.home.fragment.my.item.bean.AttenStatBean
import kotlinx.android.synthetic.main.layout_attend_detail_03.*
import kotlinx.android.synthetic.main.layout_date_time.*
import kotlinx.android.synthetic.main.layout_my_atten_01.*
import kotlinx.android.synthetic.main.layout_my_atten_02.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * 考勤统计
 * @author yyx
 */

class AttenStatFragment : BaseFragment<AttenStatContract.View, AttenStatPresenter>(),
    AttenStatContract.View,
    CalendarView.OnCalendarSelectListener {
    override fun onCalendarOutOfRange(calendar: Calendar?) {
    }

    private val sdf = SimpleDateFormat("yyyy-MM-dd")

    private val sdf1 = SimpleDateFormat("yyyy-MM")
    private val sdf3 = SimpleDateFormat("yyyy")
    private val sdf4 = SimpleDateFormat("MM")
    var mDay = ""
    var curMonth = 0
    var curYear = 0
    var pageData: MutableList<AttenStatBean.Data> = mutableListOf()
    private val sdf2 = SimpleDateFormat("HH:mm")
    val formatType = "yyyy-MM-dd HH:mm:ss"
    var adapter: AttendDetailAdapter? = null
    var startDate = ""
    var endDate = ""
    var date = ""
    var baiduMap: BaiduMap? = null
    var mBeans: MutableList<AttenDetailBean.TraceVO> = mutableListOf()
    override fun onCalendarSelect(calendar: Calendar?, isClick: Boolean) {
        LogUtils.d(TAG, "onCalendarSelect: $isClick");
        val curMonth = calendar?.month
        val curYear = calendar?.year
        val day = calendar?.day
        if (curMonth != null) {
            if (isClick) {
                if (day != null) {
                    date =
                        "$curYear-${if (curMonth < 10) "0$curMonth" else curMonth.toString()}-${if (day < 10) "0$day" else day.toString()}"
                }
                mPresenter.ByTeam()
            } else {
                startDate = "$curYear-$curMonth-01"
                endDate = "${DataUtil.getLastDayOfMonth(curYear!!, curMonth)}"
                date = startDate
                tv_time.text =
                    "$curYear-${if (curMonth < 10) "0$curMonth" else curMonth.toString()}"
                mPresenter.loadRepositories()
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): AttenStatFragment {
            val args = Bundle()
            val fragment = AttenStatFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override var mPresenter: AttenStatPresenter = AttenStatPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_atten_stat, container!!)
        }
        return main_layout!!
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        cv_time_view.setOnCalendarSelectListener(this)

        baiduMap = mv_map_atend3.map
        baiduMap?.uiSettings?.isScrollGesturesEnabled = true
        val curMonth = cv_time_view.curMonth
        tv_time.text =
            "${cv_time_view.curYear}-${if (curMonth < 10) "0$curMonth" else curMonth.toString()}"
        mDay = sdf.format(Date(System.currentTimeMillis()))
        adapter = AttendDetailAdapter(context!!, mBeans)
        rv_atten_detail3.adapter = adapter
    }

    override fun initData() {

        date = sdf.format(Date(System.currentTimeMillis()))
        startDate = "${sdf1.format(Date(System.currentTimeMillis()))}-01"
        endDate = "${DataUtil.getLastDayOfMonth(
            sdf3.format(Date(System.currentTimeMillis())).toInt(),
            sdf4.format(Date(System.currentTimeMillis())).toInt()
        )}"


        mPresenter.loadRepositories()
        curYear = cv_time_view.curYear
        curMonth = cv_time_view.curMonth

        setOnClickListener(img_time_right)
        setOnClickListener(img_time_left)
    }

    //    rule	排班规则（上班、请假、轮休、非上班日）	String	排班规则（上班1、请假2、轮休3、非上班日4）
    override fun showListData(data: MutableList<AttenStatBean.Data>) {

        this.pageData.clear()

        this.pageData.addAll(data.distinctBy { it })
        LogUtils.d(TAG, "pageData:$pageData ");
        schemeDate()
        //正常
        var numZ = 0
        //请假
        var numQ = 0
        //异常
        var numY = 0


        this.pageData.forEach {

            if (it.status == "1") {
                //排班正常
                when (it.rule.toInt()) {
                    1 -> {
                        if (!TextUtils.isEmpty(it.checkInTime) || !TextUtils.isEmpty(it.checkOutTime)) {
                            numZ++
                        }
                    }
                    2, 3, 4 -> numQ++
                    else -> 0
                }
            } else if (it.status == "0") {
                when (it.rule.toInt()) {
                    1 -> if (!TextUtils.isEmpty(it.checkInTime) || !TextUtils.isEmpty(it.checkOutTime)) {
                        numZ++
                    }
                }
                numY++
            }
        }
        tv_attend_sta_02.text = if (numZ != 0) "$numZ" else "0"
        tv_attend_sta_03.text = if (numQ != 0) "$numQ" else "0"
        tv_attend_sta_04.text = if (numY != 0) "$numY" else "0"

    }

    override fun showErrorListData() {
        //考勤列表为空
        tv_attend_sta_02.text = "0"
        tv_attend_sta_03.text = "0"
        tv_attend_sta_04.text = "0"
    }

    //-0x20ecaa 请假、轮休
    //-0x123a93 异常
    //-0xbf24db 正常
    private fun schemeDate() {


        val map = HashMap<String, Calendar>()

        pageData.forEach {

            if (it.status == "1") {
                //排班正常
                val i = when (it.rule.toInt()) {
                    1 -> -0xbf24db
                    2, 3, 4 -> -0x20ecaa
                    else -> 0
                }
                map[getSchemeCalendar(curYear, curMonth, it.dateStr.toInt(), i, "").toString()] =
                    getSchemeCalendar(curYear, curMonth, it.dateStr.toInt(), i, "")
            } else if (it.status == "0") {
                map[getSchemeCalendar(
                    curYear,
                    curMonth,
                    it.dateStr.toInt(),
                    -0x123a93,
                    ""
                ).toString()] =
                    getSchemeCalendar(curYear, curMonth, it.dateStr.toInt(), -0x123a93, "")
            }

        }
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        cv_time_view.setSchemeDate(map)
    }

    //详情
    override fun showDataList(data: AttenDetailBean.Data) {
        try {
            tv_my_attem_011.visibility = GONE
            ll_my_attem_011.visibility = VISIBLE
            tv_my_attem_01.text = "${data.date.replace("-", "/")}\t ${DataUtil.getWeek(data.date)}"
            if (data.checkInTime != null) {
                tv_my_attem_02.text =
                    "${sdf2.format(Date(DataUtil.stringToLong(data.checkInTime, formatType)))}"
            } else {
                tv_my_attem_02.text =
                    "${sdf2.format(
                        Date(
                            DataUtil.stringToLong(
                                data.standardCheckInTime,
                                formatType
                            )
                        )
                    )}"
            }

            tv_my_attem_055.visibility = if (data.checkInTime != null) GONE else VISIBLE
            tv_my_attem_05.visibility = if (data.checkInTime != null) VISIBLE else GONE
            if (data.checkOutTime != null) {
                tv_my_attem_06.text =
                    "${sdf2.format(Date(DataUtil.stringToLong(data.checkOutTime, formatType)))}"
            } else {
                tv_my_attem_06.text =
                    "${sdf2.format(
                        Date(
                            DataUtil.stringToLong(
                                data.standardCheckOutTime,
                                formatType
                            )
                        )
                    )}"
            }
            tv_my_attem_099.visibility = if (data.checkOutTime != null) GONE else VISIBLE
            tv_my_attem_09.visibility = if (data.checkOutTime != null) VISIBLE else GONE
            tv_my_attem_03.text = data.checkInRemark
            tv_my_attem_04.text = data.checkInDeclare
            tv_my_attem_05.text =
                if (!TextUtils.isEmpty(data.checkInAddress)) data.checkInAddress else "暂无考勤数据"
            if (TextUtils.isEmpty(data.checkInAddress)) {
                tv_my_attem_05.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
            tv_my_attem_07.text = data.checkOutRemark
            tv_my_attem_08.text = data.checkOutDeclare
            tv_my_attem_09.text =
                if (!TextUtils.isEmpty(data.checkOutAddress)) data.checkOutAddress else "暂无考勤数据"

            if (TextUtils.isEmpty(data.checkOutAddress)) {
                tv_my_attem_09.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
            val voList = data.traceVOList
            if (voList != null) {
                ll_attend_deta13.visibility = VISIBLE
                val mBeans: MutableList<AttenDetailBean.TraceVO> = mutableListOf()
                //去重
                mBeans.addAll(voList.distinctBy { it })
                adapter!!.refreshData(mBeans)
                tv_attend_deta13.text = "当天工作轨迹"
                LogUtils.d(TAG, "voList:$voList ");
                var mVoList :MutableList<AttenDetailBean.TraceVO> = mutableListOf()
                for (i in voList.indices ){

                    val traceVO = voList[i]
                    traceVO.num = "${voList.size-i}"
                    if (traceVO.status == "1"){
                        mVoList.add(traceVO)
                    }
                }
                Log.d(TAG, "mVoList:$mVoList ");

                if (mVoList.size>0){
                    val point = LatLng(
                        mVoList[0].locateLatitude.toDouble(),
                        mVoList[0].locateLongitude.toDouble()
                    )
                    val mMapStatus = MapStatus.Builder()
                        .target(point)
                        .zoom(15.toFloat())
                        .build()
                    val newMapStatus = MapStatusUpdateFactory.newMapStatus(mMapStatus)
                    //定位到第一个坐标
                    baiduMap?.animateMapStatus(newMapStatus)
                    var mOptions: MutableList<LatLng> = mutableListOf()
                    val options = ArrayList<OverlayOptions>()
                    for (it in mVoList) {
                        if (!TextUtils.isEmpty(it.locateLatitude) && !TextUtils.isEmpty(it.locateLongitude)) {
                            mOptions.add(
                                LatLng(
                                    it.locateLatitude.toDouble(),
                                    it.locateLongitude.toDouble()
                                )
                            )
                            val view = LayoutInflater.from(context).inflate(R.layout.map_iamge, null)
                            val img = view.findViewById<ImageView>(R.id.img_img)
                            img.setImageResource(if (it.status.toInt() == 1) R.mipmap.icon_orange else R.mipmap.icon_red)
                            val tv = view.findViewById<TextView>(R.id.tv_img)
                            tv.text = it.num
                            val resource =
                                BitmapDescriptorFactory.fromView(view)
                            val point1 =
                                LatLng(it.locateLatitude.toDouble(), it.locateLongitude.toDouble())
                            val option = MarkerOptions()
                                .position(point1)
                                .icon(resource)
                            options.add(option)
                        }
                    }
                    Log.e(TAG, "mOptions:$mOptions ");
                    val points = PolylineOptions().width(10)
                        .color(R.color.map_color).points(mOptions)
                    baiduMap?.addOverlay(points)
                    baiduMap?.addOverlays(options)
                }

            } else {
                tv_attend_deta13.text = "暂无工作轨迹"
                ll_attend_deta13.visibility = GONE
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    //数据为空
    override fun showErrorData() {
        tv_my_attem_01.text = "${date.replace("-", "/")}\t ${DataUtil.getWeek(date)}"
        tv_my_attem_011.visibility = VISIBLE
        ll_my_attem_011.visibility = GONE
    }


    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> {
                AttendSchedulImp.API_CUST_ATTEND_LIST
            }
            2 -> {
                AttendSchedulImp.API_CUST_ATTEND_INFO
            }
            else -> {
                ""
            }
        }
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val mapOf = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                mapOf["startDate"] = startDate
                mapOf["endDate"] = endDate
                mapOf["userId"] = AppApplication.USERID
            }
            2 -> {
                mapOf["date"] = date
                mapOf["userId"] = AppApplication.USERID
            }
            else -> {
            }
        }
        return mapOf
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            img_time_right -> {
                cv_time_view.onRight()
            }
            img_time_left -> {
                cv_time_view.onLeft()
            }
            else -> {
            }
        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    private fun getSchemeCalendar(
        year: Int,
        month: Int,
        day: Int,
        color: Int,
        text: String
    ): Calendar {
        val calendar = Calendar()
        calendar.year = year
        calendar.month = month
        calendar.day = day
        calendar.schemeColor = color//如果单独标记颜色、则会使用这个颜色
        calendar.scheme = text
        return calendar
    }

}