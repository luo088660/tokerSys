package com.toker.sys.view.home.activity.my.projtdeta.attenddetail

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.util.Log
import android.view.TextureView
import android.view.View
import com.alibaba.fastjson.JSON
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.PolylineOptions
import com.baidu.mapapi.model.LatLng
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.dialog.my.AttendanStatusDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.bean.AstatuBean
import com.toker.sys.utils.network.params.AttendSchedulImp
import com.toker.sys.utils.tools.DataUtil
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.adapter.AttendDetailAdapter
import com.toker.sys.view.home.activity.my.bean.AttenDetailBean
import com.toker.sys.view.home.activity.my.bean.ViewAttenListBean
import kotlinx.android.synthetic.main.activity_attend_detail.*
import kotlinx.android.synthetic.main.layout_attend_detail_01.*
import kotlinx.android.synthetic.main.layout_attend_detail_02.*
import kotlinx.android.synthetic.main.layout_attend_detail_03.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_view_attendan_01.*
import kotlinx.android.synthetic.main.layout_view_attendan_03.*
import kotlinx.android.synthetic.main.layout_view_attendan_04.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Appendable
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * 查看考勤详情
 * @author yyx
 */

class AttendDetailActivity : BaseActivity<AttendDetailContract.View, AttendDetailPresenter>(),
    AttendDetailContract.View {
    val formatType = "yyyy-MM-dd HH:mm:ss"
    private var mDatePicker1: CustomDatePicker? = null
    override var mPresenter: AttendDetailPresenter = AttendDetailPresenter()
    var tableTag = ""
    var baiduMap: BaiduMap? = null
    private val sdf = SimpleDateFormat("HH:mm")
    private val sdf1 = SimpleDateFormat("yyyy/MM/dd")
    private val sdf2 = SimpleDateFormat("yyyy-MM-dd")
    var mBeans: MutableList<AttenDetailBean.TraceVO> = mutableListOf()
    var adapter: AttendDetailAdapter? = null
    override fun layoutResID(): Int = R.layout.activity_attend_detail
    var bean: ViewAttenListBean.BeanEvent? = null
    var date: String? = ""
    var time: String = ""

    override fun initView() {
        EventBus.getDefault().register(this)
        bean = intent.getSerializableExtra("event") as ViewAttenListBean.BeanEvent
        time= intent.getStringExtra("time")
        Log.e(TAG, "time:$time ");
        date = time
//        date = sdf2.format(System.currentTimeMillis())
        tv_title.text = "查看考勤详情"
        adapter = AttendDetailAdapter(this, mBeans)
        rv_atten_detail3.adapter = adapter
        mPresenter.loadRepositories()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        baiduMap = mv_map_atend3.map
        setOnClickListener(img_back)
        setOnClickListener(tv_attend_deta1_02)
        setOnClickListener(cb_attend_deta2_01)
        setOnClickListener(cb_attend_deta2_02)
        setOnClickListener(tv_attend_detail_01)
        setOnClickListener(tv_attend_detail_02)
        initDatePicker()
        tv_attend_deta1_02.text = "$time\t${DataUtil.getWeek(time)}"

        /*tv_attend_deta1_02.text = "${sdf1.format(System.currentTimeMillis()).replace(
            "/",
            "-"
        )}\t${DataUtil.getWeek(sdf1.format(System.currentTimeMillis()).replace("/", "-"))}"*/
    }

    override fun showDataList(data: AttenDetailBean.Data) {

        tv_my_attem_011.visibility = GONE
        ll_attend2_01.visibility = VISIBLE
        ll_attend2_02.visibility = VISIBLE
        ll_attend_detail_01.visibility = VISIBLE
        tableTag = data.tableTag
        tv_attend_deta1_01.text = data.userName
//        tv_attend_deta1_02.text = "${data.date}\t${DataUtil.getWeek(data.date)}"
        tv_attend_deta1_03.text =
            "${sdf.format(Date(DataUtil.stringToLong(data.standardCheckInTime, formatType)))}"
        tv_attend_deta1_04.text =
            "${sdf.format(Date(DataUtil.stringToLong(data.standardCheckOutTime, formatType)))}"
        tv_attend_deta1_05.text = data.address


        //当天签到记录
        tv_attend_deta2_01.text =
            "${sdf.format(Date(DataUtil.stringToLong(data.checkInTime, formatType)))}"
        tv_attend_deta2_02.text = "签到\t${data.checkInRemark}"
        tv_attend_deta2_03.text = if (!TextUtils.isEmpty(data.checkInAddress)) data.checkInAddress else "暂无考勤数据"
        if(TextUtils.isEmpty(data.checkOutAddress)) {
            tv_attend_deta2_03.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
        tv_attend_deta2_04.text = if (TextUtils.isEmpty(data.checkInStatus)) {
            "暂无签到"
        } else if (data.checkInStatus == "0") "签到异常" else "签到正常"

        tv_attend_deta2_05.text =
            "${sdf.format(Date(DataUtil.stringToLong(if(!TextUtils.isEmpty(data.checkOutTime))data.checkOutTime else data.standardCheckOutTime, formatType)))}"
        tv_attend_deta2_06.text = "签退\t${if(TextUtils.isEmpty(data.checkOutRemark)) "" else data.checkOutRemark}"
        tv_attend_deta2_07.text =  if (!TextUtils.isEmpty(data.checkOutAddress)) data.checkOutAddress else "暂无考勤数据"
        if(TextUtils.isEmpty(data.checkOutAddress)) {
            tv_attend_deta2_07.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
        tv_attend_deta2_08.text = if (TextUtils.isEmpty(data.checkOutStatus)){
            "暂无签到"
        } else {if (data.checkOutStatus == "0") "签到异常" else "签到正常"}


//        ll_attend_detail.visibility = if (data.checkInStatus == "1" && data.checkOutStatus == "1") GONE else VISIBLE

//        cb_attend_deta2_01.visibility = if (data.checkInStatus == "0") VISIBLE else GONE
//        cb_attend_deta2_02.visibility = if (data.checkOutStatus == "0") VISIBLE else GONE

        //TODO 中层不能进行审批？？//||data.isApprove == "1"
        cb_attend_deta2_01.visibility =
            if ((AppApplication.TYPE == Constants.RESCUE1) ||(AppApplication.TYPE == Constants.RESCUE4) ||TextUtils.isEmpty(data.checkInStatus )||data.checkInStatus == "1") GONE else VISIBLE
        if(TextUtils.isEmpty(data.checkInStatus )||data.checkInStatus == "1"){
            cb_attend_deta2_01.isChecked = false
        }

        cb_attend_deta2_02.visibility =
            if ((AppApplication.TYPE == Constants.RESCUE1)||(AppApplication.TYPE == Constants.RESCUE4) ||TextUtils.isEmpty(data.checkOutStatus )||data.checkOutStatus == "1") GONE else VISIBLE
        if(TextUtils.isEmpty(data.checkOutStatus )||data.checkOutStatus == "1"){
            cb_attend_deta2_02.isChecked = false
        }
        ll_attend_detail.visibility =
            if ((AppApplication.TYPE == Constants.RESCUE1)||(AppApplication.TYPE == Constants.RESCUE4) || (data.checkInStatus == "1" && data.checkOutStatus == "1")) GONE else VISIBLE


        tv_attend_deta2_01.setTextColor(resources.getColor(if (data.checkInStatus == "0") R.color.btn_red else R.color.c_black_6))
        tv_attend_deta2_05.setTextColor(resources.getColor(if (data.checkOutStatus == "0") R.color.btn_red else R.color.c_black_6))

        tv_attend_deta2_02.setTextColor(resources.getColor(if (data.checkInStatus == "0") R.color.btn_red else R.color.c_black_6))
        tv_attend_deta2_06.setTextColor(resources.getColor(if (data.checkOutStatus == "0") R.color.btn_red else R.color.c_black_6))

        tv_attend_deta2_04.setTextColor(resources.getColor(if (data.checkInStatus == "0") R.color.c_orange_5 else R.color.c_black_6))
        tv_attend_deta2_08.setTextColor(resources.getColor(if (data.checkOutStatus == "0") R.color.c_orange_5 else R.color.c_black_6))
        //修改按钮状态
        deterClickable()
        if (data.traceVOList.size > 0) {
            adapter!!.refreshData(data.traceVOList)
            attendanceId = data.traceVOList[0].attendanceId
            ll_attend_detail_02.visibility = VISIBLE
//            ll_attend_detail.visibility = VISIBLE
            tv_attend_deta14.visibility = GONE

            try {
                val voList :MutableList<AttenDetailBean.TraceVO> = mutableListOf()
                        data.traceVOList.forEach{
                    if (!TextUtils.isEmpty(it.locateLatitude)&&!TextUtils.isEmpty(it.locateLongitude)){
                        voList.add(it)
                    }
                }
                ll_attend_deta13.visibility = VISIBLE
                var mOptions: MutableList<LatLng> = mutableListOf()
                for (it in voList) {
                    if (!TextUtils.isEmpty(it.locateLatitude) && !TextUtils.isEmpty(it.locateLongitude)) {
                        mOptions.add(
                            LatLng(
                                it.locateLatitude.toDouble(),
                                it.locateLongitude.toDouble()
                            )
                        )
                    }
                }
                LogUtils.d(TAG, "voList:$voList ");
                LogUtils.d(TAG, "mOptions:$mOptions ");
//                if (voList.size > 0) {
                val point = mOptions[0]

                val mMapStatus = MapStatus.Builder()
                    .target(point)
                    .zoom(15.toFloat())
                    .build()
                val newMapStatus = MapStatusUpdateFactory.newMapStatus(mMapStatus)


                baiduMap?.animateMapStatus(newMapStatus)

                if(mOptions.size>1){
                    val points = PolylineOptions().width(10)
                        .color(R.color.map_color).points(mOptions)
                    baiduMap?.addOverlay(points)
                }else{
                   /* val points = PolylineOptions().width(10)
                        .color(R.color.map_color).points(mOptions[0])
                    baiduMap?.addOverlay(points)*/
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            ll_attend_detail.visibility = GONE
            ll_attend_detail_02.visibility = GONE
            tv_attend_deta14.visibility = VISIBLE
            tv_my_attem_011.visibility = VISIBLE

            ll_attend2_01.visibility = GONE
            ll_attend2_02.visibility = GONE
//            tv_attend_deta1_03.text = "暂无数据"
//            tv_attend_deta1_04.text = "暂无数据"
//            tv_attend_deta1_05.text = "暂无数据"
        }
    }

    //无考勤记录
    override fun showDataError() {
        tv_my_attem_011.visibility = VISIBLE
        ll_attend2_01.visibility = GONE
        ll_attend2_02.visibility = GONE
        ll_attend_detail_01.visibility = GONE
        tv_attend_deta1_01.text = bean?.userName
        tv_attend_deta1_03.text = "暂无数据"
        tv_attend_deta1_04.text = "暂无数据"
        tv_attend_deta1_05.text = "暂无数据"
    }

    override fun showError(desc: String) {
        toast(desc)
    }
    var attendanceId = ""
    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> {
                finish()
            }
            tv_attend_deta1_02 -> {
                mDatePicker1!!.show(System.currentTimeMillis())
            }
            cb_attend_deta2_01 -> {
                deterClickable()
            }
            cb_attend_deta2_02 -> {
                deterClickable()
            }
            //考勤正常
            tv_attend_detail_01 -> {
                if (!cb_attend_deta2_01.isChecked && !cb_attend_deta2_02.isChecked) {
                    return
                }

                LogUtils.d(TAG, "考勤正常: 考勤正常")
                //签到和签退
                //签到
                //签退
                AttendanStatusDialog(
                    this,
                    1,
                    if (cb_attend_deta2_01.isChecked && cb_attend_deta2_02.isChecked) "0" else if (cb_attend_deta2_01.isChecked && !cb_attend_deta2_02.isChecked) "1" else "2"
                )
            }
            //考勤异常
            tv_attend_detail_02 -> {
                if (!cb_attend_deta2_01.isChecked && !cb_attend_deta2_02.isChecked) {
                    return
                }
                LogUtils.d(TAG, "考勤异常: 考勤异常")
                //签到和签退
                //签到
                //签退
                AttendanStatusDialog(
                    this,
                    0,
                    if (cb_attend_deta2_01.isChecked && cb_attend_deta2_02.isChecked) "0" else if (cb_attend_deta2_01.isChecked && !cb_attend_deta2_02.isChecked) "1" else "2"
                )
            }

            else -> {
            }
        }
    }

    //
    override fun onSuccessStatus() {
        mPresenter.loadRepositories()
    }

    private fun deterClickable() {
        val isChecked = !cb_attend_deta2_01.isChecked && !cb_attend_deta2_02.isChecked
        //全部未选中 //选中一个 或两个
        tv_attend_detail_01.isClickable = !isChecked
        tv_attend_detail_02.isClickable = !isChecked
        tv_attend_detail_01.background =
            resources.getDrawable(if (isChecked) R.drawable.btn_bg_kaoqin_zc_normal else R.drawable.btn_bg_kaoqin_zc_perssed)
        tv_attend_detail_02.setTextColor(resources.getColor(if (isChecked) R.color.c_black_6 else R.color.c_orange_5))
        tv_attend_detail_02.background =
            resources.getDrawable(if (isChecked) R.drawable.btn_bg_kaoqin_yc_normal else R.drawable.btn_bg_kaoqin_yc_perssed)
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                map["date"] = date!!
                map["userId"] = bean?.userId!!
            }
            else -> {
                map["date"] = date!!
                map["attendanceId"] = attendanceId
                map["attendanceType"] = attendanceType
                map["result"] = status
                map["isRequest"] = isRequest
                map["tableTag"] = tableTag
                if (!TextUtils.isEmpty(remark)) {
                    map["remark"] = remark
                }
                map["type"] = if (attendanceType == "0") "1,2" else attendanceType
            }
        }

        return map
    }

    /**
     * 时间选择
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {
        // 通过时间戳初始化日期，毫秒级别
        mDatePicker1 = CustomDatePicker(this, object : CustomDatePicker.Callback {
            @SuppressLint("SetTextI18n")
            override fun onTimeSelected(timestamp: Long) {
                date = sdf2.format(Date(timestamp))
                tv_attend_deta1_02.text = "${date}\t${DataUtil.getWeek(date!!)}"
                mPresenter.loadRepositories()

            }
        }, 1, isVisiTab = false, isSEndTime = false)
        // 不允许点击屏幕或物理返回键关闭
//        mDatePicker1?.setCancelable(false)
        // 不显示时和分
        mDatePicker1?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mDatePicker1?.setScrollLoop(true)
        // 不允许滚动动画
        mDatePicker1?.setCanShowAnim(false)


    }

    var attendanceType = ""
    var status = ""
    var isRequest = ""
    var remark = ""
    //异常考勤审批
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun attendanStatusDialog(event: AstatuBean.Bean) {
        attendanceType = event.attType
        status = event.status
        isRequest = event.isRequest
        remark = event.remark
        mPresenter.updateAttendanceStatus()

    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> AttendSchedulImp.API_CUST_ATTEND_INFO
            else -> AttendSchedulImp.API_CUST_ATTEND_UPDATE_STATUS
        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
