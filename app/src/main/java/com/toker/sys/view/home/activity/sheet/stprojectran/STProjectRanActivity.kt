package com.toker.sys.view.home.activity.sheet.stprojectran

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.toker.sys.R
import com.toker.sys.dialog.sheet.TurnoverDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.adapter.STProjectRanAdapter
import com.toker.sys.view.home.activity.sheet.event.STProjectRanEvent
import com.toker.sys.view.home.bean.STExtenRankBean
import kotlinx.android.synthetic.main.activity_s_t_project_ran.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_project_ver1.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat

/**
 * 项目业绩排名
 * @author yyx
 */

class STProjectRanActivity : BaseActivity<STProjectRanContract.View, STProjectRanPresenter>(),
    STProjectRanContract.View {
    private var mDatePicker1: CustomDatePicker? = null
    private var timeType = 3
    private var item: String = "4"
    private var sortWay: String = "asc"
    private var sortBy: String = "1"
    private var page = 1
    private val thisTimestamp = System.currentTimeMillis()

    private val sdfPb = SimpleDateFormat("yyyy-MM")
    private var time: String = sdfPb.format(System.currentTimeMillis())
    private var type: String = "2"
    override var mPresenter: STProjectRanPresenter = STProjectRanPresenter()

    private var drawableS: Drawable? = null
    private var drawableX: Drawable? = null
    private var drawableM: Drawable? = null
    private var mBeans: MutableList<STExtenRankBean.Record> = mutableListOf()
    var adapter: STProjectRanAdapter? = null

    var isOrder1 = false
    var isOrder2 = false
    var isOrder3 = false
    override fun layoutResID(): Int = R.layout.activity_s_t_project_ran
    private var timestamp = System.currentTimeMillis()

    override fun initView() {
        EventBus.getDefault().register(this)
        tv_title.text = "项目业绩排名"
        rv_s_t_project.layoutManager = GridLayoutManager(this, 1)
    }

    @SuppressLint("NewApi")
    override fun initData() {
        tv_ver_02.text = time.replace("-", "/")
        adapter = STProjectRanAdapter(this, mBeans)
        rv_s_t_project.adapter = adapter
        setOnClickListener(img_back)
        setOnClickListener(tv_ver_01)
        setOnClickListener(tv_ver_02)
        setOnClickListener(img_ver_03)
        setOnClickListener(tv_stp_03)
        setOnClickListener(tv_stp_04)
        setOnClickListener(tv_stp_05)
        initDatePicker()
        mPresenter.loadRepositories()

        // 使用代码设置drawableleft
        drawableS = resources.getDrawable(R.mipmap.icon_xspx)
        drawableX = resources.getDrawable(R.mipmap.icon_xxpx)
        drawableM = resources.getDrawable(R.mipmap.icon_mrpx)
        // / 这一步必须要做,否则不会显示.
        drawableS!!.setBounds(0, 0, drawableS!!.minimumWidth, drawableS!!.minimumHeight)
        drawableX!!.setBounds(0, 0, drawableX!!.minimumWidth, drawableX!!.minimumHeight)
        drawableM!!.setBounds(0, 0, drawableM!!.minimumWidth, drawableM!!.minimumHeight)
    }

    /**
     * 业绩项
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun STProjectRanEvent(event: STProjectRanEvent) {
        log("${event.name}-----", "STProjectRanActivity")
        tv_ver_01.text = event.name
        item = "${event.type}"
        onFRefresh()
    }

    override fun onFRefresh() {
        super.onFRefresh()
        mBeans.clear()
        page = 1
        mPresenter.loadRepositories()
    }

    override fun onFLoadmore() {
        super.onFLoadmore()
        page++
        mPresenter.loadRepositories()
    }

    override fun showListData(data: STExtenRankBean.Data) {
        mBeans.addAll(data.records)
        adapter?.refreshData(mBeans)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            //成交额
            tv_ver_01 -> {
                TurnoverDialog(this)
            }
            //开始时间
            tv_ver_02,
            img_ver_03 -> {
                mDatePicker1!!.show(timestamp)
            }
            //完成
            tv_stp_03 -> {
                tv_stp_03?.setCompoundDrawables(null, null, if (isOrder1) drawableS else drawableX, null  )
                tv_stp_04?.setCompoundDrawables(null, null, drawableM, null  )
                tv_stp_05?.setCompoundDrawables(null, null, drawableM, null  )
                sortWay = if (isOrder1) "desc" else "asc"
                isOrder1 = !isOrder1
                isOrder2 = false
                isOrder3 = false
                sortBy = "1"
                onFRefresh()
            }
            //目标
            tv_stp_04 -> {
                tv_stp_04?.setCompoundDrawables(null, null, if (isOrder2) drawableS else drawableX, null  )
                tv_stp_03?.setCompoundDrawables(null, null, drawableM, null  )
                tv_stp_05?.setCompoundDrawables(null, null, drawableM, null  )
                sortWay = if (isOrder2) "desc" else "asc"
                isOrder2 = !isOrder2
                isOrder1 = false
                isOrder3 = false
                sortBy = "2"
                onFRefresh()
            }
            //完成率
            tv_stp_05 -> {
                tv_stp_05?.setCompoundDrawables(null, null, if (isOrder3) drawableS else drawableX, null  )
                tv_stp_04?.setCompoundDrawables(null, null, drawableM, null  )
                tv_stp_03?.setCompoundDrawables(null, null, drawableM, null  )
                sortWay = if (isOrder3) "desc" else "asc"
                isOrder3 = !isOrder3
                isOrder1 = false
                isOrder2 = false
                sortBy = "3"
                onFRefresh()
            }
            else -> {
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {
        /*mDatePicker1 = CustomDatePicker(this, object : CustomDatePicker.CallbackListen {
            override fun onTimeSelected(timestamp: Long, timeType: Int) {
                log("onTimeSelected---$timeType", "STProjectRanActivity")
                this@STProjectRanActivity.time =
                    DateFormatUtils.long2Str(timestamp, false, timeType)
                tv_ver_02.text = time.replace("-", "/")
                this@STProjectRanActivity.type = "$timeType"
                onFRefresh()

            }
        }, 5, true, false)*/

        mDatePicker1 = CustomDatePicker(this, object : CustomDatePicker.CallbackSEListen {
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
                        this@STProjectRanActivity.timestamp = startTime
                        type = "$timeType"

                        val time = DateFormatUtils.long2Str(timestamp, false, timeType)
                        this@STProjectRanActivity.time = time
                        tv_ver_02?.text = time.replace("-", "/")
                        onFRefresh()

                    }
                    else -> {
                        if (startTime == 0L || endTime == 0L) {
                            return
                        }
                        this@STProjectRanActivity.timestamp = startTime
                        type = "4"
                        beginDate = DateFormatUtils.long2Str(startTime, false, timeType)
                        endDate = DateFormatUtils.long2Str(endTime, false, timeType)

                        tv_ver_02?.text =
                            "${beginDate.replace("-", "/")}\n${endDate.replace("-", "/")}"
                        onFRefresh()
                    }
                }

            }

        }, 4, true, true)

        // 不允许点击屏幕或物理返回键关闭
//        mDatePicker1?.setCancelable(false)
        // 不显示时和分
        mDatePicker1?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mDatePicker1?.setScrollLoop(true)
        // 不允许滚动动画
        mDatePicker1?.setCanShowAnim(false)
    }
    var beginDate = ""
    var endDate = ""
    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
//        map["time"] = time
        map["type"] = type

        when (type) {
            "4" -> {
                map["beginDate"] = beginDate
                map["endDate"] = endDate
            }
            else -> {
                map["time"] = time
            }
        }
        map["item"] = item //留电数:1,到访数:2,成交量:3,成交额:4"
        map["sortWay"] = sortWay//",asc:正序;desc:倒序"
        map["sortBy"] = sortBy //完成：1，目标：2，完成率：3"
        map["page"] = "$page"      //	当前页	否	int	默认1
        map["pageSize"] = "10"       //	分页大小	否	int	默认10
        return map
    }

    override fun getUrl(url_type: Int): String {
        return PerformStateImp.API_PER_PROJECT_PERFORMANCE

    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mDatePicker1?.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
