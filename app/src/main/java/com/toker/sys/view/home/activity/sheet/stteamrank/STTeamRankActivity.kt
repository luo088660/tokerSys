package com.toker.sys.view.home.activity.sheet.stteamrank

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.dialog.ProjectListDialog
import com.toker.sys.dialog.sheet.TurnoverDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.adapter.STTeamRankAdapter
import com.toker.sys.view.home.activity.sheet.event.STProjectRanEvent
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.bean.STExtenRankBean
import kotlinx.android.synthetic.main.activity_s_t_team_rank.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 * 团队业绩排名
 * @author yyx
 */

class STTeamRankActivity : BaseActivity<STTeamRankContract.View, STTeamRankPresenter>(),
    STTeamRankContract.View {

    override var mPresenter: STTeamRankPresenter = STTeamRankPresenter()
    private var mDatePicker1: CustomDatePicker? = null
    private var mBeans: MutableList<STExtenRankBean.Record> = mutableListOf()
    private var adapter: STTeamRankAdapter? = null
    private var tv01: TextView? = null
    private var tv02: TextView? = null
    private var tv03: TextView? = null
    private var lltime: LinearLayout? = null
    private var page = 1
    private var item = 1
    private var sortWay: String = "asc"

    private var sortBy: String = "1"
    private var time = ""
    private var type = "2"
    private var projectId = ""
    private var thisTimestamp = System.currentTimeMillis()
    val sdf = SimpleDateFormat("yyyy-MM")
    var isOrder = false
    var drawableS: Drawable?= null
    var drawableX: Drawable?= null
    var pjtData: MutableList<Data>? = null

    private var btnKhzl: TextView? = null
    override fun layoutResID(): Int = R.layout.activity_s_t_team_rank

    override fun initView() {
        EventBus.getDefault().register(this)
        tv_title.text = "团队业绩排名"
        rv_team_rank.layoutManager = GridLayoutManager(this, 1)



    }


    @SuppressLint("NewApi")
    override fun initData() {


        time = sdf.format(Date(thisTimestamp))
        val view = LayoutInflater.from(this).inflate(R.layout.layout_s_t_team_rank, null)
        tv01 = view.findViewById<TextView>(R.id.tv_ver_01)

        tv02 = view.findViewById<TextView>(R.id.tv_ver_02)
        tv03 = view.findViewById<TextView>(R.id.tv_ver_03)
        btnKhzl = view.findViewById<TextView>(R.id.tv_khzl)
        lltime = view.findViewById<LinearLayout>(R.id.ll_ver_03)

        // 使用代码设置drawableleft
        drawableS = resources.getDrawable( R.mipmap.icon_xspx)
        drawableX = resources.getDrawable( R.mipmap.icon_xxpx)
        // / 这一步必须要做,否则不会显示.
        drawableS!!.setBounds(0, 0, drawableS!!.minimumWidth,drawableS!!.minimumHeight )
        drawableX!!.setBounds(0, 0, drawableX!!.minimumWidth,drawableX!!.minimumHeight )

        adapter = STTeamRankAdapter(this, mBeans)
        rv_team_rank.adapter = adapter
        initSpringView(sp_team_rank)
        adapter!!.setHeaderView(view)
        tv03?.text =  time.replace("-", "/")
        setOnClickListener(img_back)
        setOnClickListener(btnKhzl!!)
        setOnClickListener(tv01!!)
        setOnClickListener(tv02!!)
        setOnClickListener(lltime!!)
        initDatePicker()

        try {
            projectId = intent.getStringExtra("projectId")
            Log.e(TAG, "projectId:$projectId ");
            val projectName = intent.getStringExtra("projectName")
            tv01!!.text = projectName
        }catch (e:Exception){
            e.printStackTrace()
        }
        mPresenter.loadRepositories()
        mPresenter.getProjectList()
    }

    /**
     * 业绩项
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun STProjectRanEvent(event: STProjectRanEvent) {
        log("${event.name}-----", "STProjectRanActivity")
        item = event.type
        tv02?.text = event.name
        onFRefresh()
    }

    //数据展示
    override fun showDataList(data: STExtenRankBean.Data) {
        mBeans.addAll(data.records)
        adapter?.refreshData(mBeans)
    }

    override fun onFRefresh() {
        super.onFRefresh()
        this.page = 1
        mBeans.clear()
        mPresenter.loadRepositories()
    }

    override fun onFLoadmore() {
        super.onFLoadmore()
        this.page++
        mPresenter.loadRepositories()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            //全部项目
            tv01 -> {
                ProjectListDialog(this, pjtData!!)
            }
            //成交额
            tv02 -> {
                TurnoverDialog(this)
            }
            //时间筛选
            lltime -> {
                mDatePicker1!!.show(thisTimestamp)
            }
            btnKhzl->{
                btnKhzl?.setCompoundDrawables(null,null,if (isOrder)drawableS else drawableX,null)
                sortWay = if (isOrder) "desc" else "asc"
                isOrder = !isOrder
                onFRefresh()
            }
            else -> {
            }
        }
    }

    //我的项目
    override fun showProjectDate(data: MutableList<Data>) {
        this.pjtData = data
    }

    /**
     * 选项目
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun dataProject(event: com.toker.sys.view.home.bean.Data) {
        tv01!!.text = event.projectName
        projectId = event.projectId
        onFRefresh()
    }
    var beginDate = ""
    var endDate = ""
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {
        // 通过时间戳初始化日期，毫秒级别
       /* mDatePicker1 = CustomDatePicker(this, object : CustomDatePicker.CallbackListen {
            override fun onTimeSelected(timestamp: Long, timeType: Int) {
                log("onTimeSelected---$timeType", "STProjectRanActivity")
                this@STTeamRankActivity.time = DateFormatUtils.long2Str(timestamp, false, timeType)
                tv03?.text = time.replace("-", "/")
                this@STTeamRankActivity.type = "$timeType"
                mBeans.clear()
                mPresenter.loadRepositories()

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
                        this@STTeamRankActivity.thisTimestamp = startTime
                        type = "$timeType"

                        val time = DateFormatUtils.long2Str(thisTimestamp, false, timeType)
                        this@STTeamRankActivity.time = time
                        tv03?.text = time.replace("-", "/")
                        onFRefresh()

                    }
                    else -> {
                        if (startTime == 0L || endTime == 0L) {
                            return
                        }
                        this@STTeamRankActivity.thisTimestamp = startTime
                        type = "4"
                        beginDate = DateFormatUtils.long2Str(startTime, false, timeType)
                        endDate = DateFormatUtils.long2Str(endTime, false, timeType)

                        tv03?.text =
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

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                if (!TextUtils.isEmpty(projectId)) {
                    map["projectId"] = projectId
                }
//                map["time"] = time
                when (type) {
                    "4" -> {
                        map["beginDate"] = beginDate
                        map["endDate"] = endDate
                    }
                    else -> {
                        map["time"] = time
                    }
                }
                map["type"] = type
                map["item"] = "$item" //留电数:1,到访数:2,成交量:3,成交额:4"
                map["sortWay"] = sortWay//"sortBy不能为空,asc:正序;desc:倒序"
                map["sortBy"] = sortBy //完成：1，目标：2，完成率：3"
                map["page"] = "$page"      //	当前页	否	int	默认1
                map["pageSize"] = "10"       //	分页大小	否	int	默认10
            }
            else -> {
            }
        }
//
        return map
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> {
                PerformStateImp.API_PER_GROUP_PER_FORMANCE
            }
            else -> {
                SystemSettImp.API_SYSTEM_PROJECT_LIST
            }
        }
    }


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mDatePicker1?.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
