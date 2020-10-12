package com.toker.sys.view.home.activity.sheet.stextenrank

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.toker.reslib.sm.Text
import com.toker.sys.R
import com.toker.sys.dialog.ProjectListDialog
import com.toker.sys.dialog.sheet.TurnoverDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.adapter.STExtenRankAdapter
import com.toker.sys.view.home.activity.sheet.event.STProjectRanEvent
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.bean.STExtenRankBean
import com.toker.sys.view.home.fragment.my.item.bean.GroupListBean
import kotlinx.android.synthetic.main.activity_s_t_exten_rank.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_my_performance.*
import kotlinx.android.synthetic.main.layout_pion_perfor_rank.*
import kotlinx.android.synthetic.main.layout_t_received_01.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception
import java.text.SimpleDateFormat

/**
 * 拓客员业绩排名
 * @author yyx
 */

class STExtenRankActivity : BaseActivity<STExtenRankContract.View, STExtenRankPresenter>(),
    STExtenRankContract.View {

    override var mPresenter: STExtenRankPresenter = STExtenRankPresenter()
    private var mDatePicker1: CustomDatePicker? = null
    private var mDatePicker2: CustomDatePicker? = null
    private val sdfPb = SimpleDateFormat("yyyy-MM")
    private var time: String = sdfPb.format(System.currentTimeMillis())
    private var type: String = "2"
    private var item: String = "4"
    private var sortWay: String = "asc"
    private var sortBy: String = "1"
    private var page = 1
    private var projectId = ""
    private var groupId = ""
    private var timestamp = System.currentTimeMillis()
    private var mBeans: MutableList<STExtenRankBean.Record> = mutableListOf()
    var adapter: STExtenRankAdapter? = null

    var drawableS: Drawable? = null
    var drawableX: Drawable? = null
    var drawableN: Drawable? = null
    var isOrder = false

    override fun layoutResID(): Int = R.layout.activity_s_t_exten_rank

    override fun initView() {
        EventBus.getDefault().register(this)
        tv_title.text = "拓客员业绩排名"
        rv_t_exten_rank.layoutManager = GridLayoutManager(this, 1)
    }

    @SuppressLint("NewApi")
    override fun initData() {

        // 使用代码设置drawableleft
        tv_receivde1_05.text = time.replace("-", "/")
        drawableS = resources.getDrawable(R.mipmap.icon_xspx)
        drawableX = resources.getDrawable(R.mipmap.icon_xxpx)
        drawableN = resources.getDrawable(R.mipmap.icon_shuaxuan)
        // / 这一步必须要做,否则不会显示.
        drawableN!!.setBounds(0, 0, drawableN!!.minimumWidth, drawableN!!.minimumHeight)
        drawableS!!.setBounds(0, 0, drawableS!!.minimumWidth, drawableS!!.minimumHeight)
        drawableX!!.setBounds(0, 0, drawableX!!.minimumWidth, drawableX!!.minimumHeight)
        tv_receivde1_07.visibility = GONE
        adapter = STExtenRankAdapter(this, mBeans)
        rv_t_exten_rank.adapter = adapter
        setOnClickListener(img_back)
        setOnClickListener(tv_receivde1_01)
        setOnClickListener(tv_receivde1_02)
        setOnClickListener(tv_receivde1_03)
        setOnClickListener(tv_receivde1_05)
        setOnClickListener(tv_receivde1_06)
        setOnClickListener(tv_st_exten_rank_02)
        initDatePicker()

        initSpringView(sv_t_exten_rank)
        tv_receivde1_02.text = "全部团队"
        tv_receivde1_03.text = "成交额(万元)"
        ll_received_start_time.visibility = GONE
        tv_receivde1_06.visibility = GONE
        tv_receivde1_05.hint = "选择时间"
        tv_receivde1_05?.setCompoundDrawables(
            null,
            null,
            drawableN,
            null
        )
        try {
            projectId = intent.getStringExtra("projectId")
            Log.d(TAG, "projectId:$projectId ");
            val projectName = intent.getStringExtra("projectName")
            tv_receivde1_01.text = projectName
        } catch (e: Exception) {
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
        tv_receivde1_03.text = event.name
        item = "${event.type}"
        onFRefresh()
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            //全部项目
            tv_receivde1_01 -> {
                ProjectListDialog(this, dataProjectLists, 1)
            }
            //全部团队
            tv_receivde1_02 -> {
                ProjectListDialog(this, dataGrouplists, true)
            }
            //成交额
            tv_receivde1_03 -> {
                TurnoverDialog(this)
            }
            //结束时间
            tv_receivde1_06,
            tv_receivde1_05 -> {
                mDatePicker1!!.show(timestamp)
            }
            //完成
            tv_st_exten_rank_02 -> {
                tv_st_exten_rank_02?.setCompoundDrawables(
                    null,
                    null,
                    if (isOrder) drawableS else drawableX,
                    null
                )
                sortWay = if (isOrder) "asc" else "desc"
                isOrder = !isOrder
                onFRefresh()
            }
            else -> {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: Data) {
        when (event.stuta) {
            //全部团队
            0 -> {
                tv_receivde1_02.text = event.projectName
                groupId = event.id
            }
            //我的项目
            1 -> {
                tv_receivde1_01!!.text = event.projectName
                projectId = event.projectId
                tv_receivde1_02.text = "全部团队"
                groupId = ""
                mPresenter.getGroupList()
            }
            else -> {
            }
        }
        onFRefresh()

    }


    //数据展示
    override fun showListData(data: STExtenRankBean.Data) {
        val records = data.records
        mBeans.addAll(records)
        adapter?.refreshData(mBeans)
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

    var dataProjectLists: MutableList<Data> = mutableListOf()
    var dataGrouplists: MutableList<Data> = mutableListOf()

    //项目
    override fun showProjectDate(data: MutableList<Data>) {
        dataProjectLists.addAll(data)
    }

    override fun showGroupList(data: MutableList<GroupListBean.Data>) {
        dataGrouplists.clear()
        data.forEach {
            dataGrouplists.add(Data(it.id, it.projectId, it.groupName, ""))
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {
        // 通过时间戳初始化日期，毫秒级别
       /* mDatePicker1 = CustomDatePicker(this, object : CustomDatePicker.CallbackListen {
            override fun onTimeSelected(timestamp: Long, timeType: Int) {
                LogUtils.d(TAG, "timeType:$timeType ");
                this@STExtenRankActivity.type = "$timeType"
                val long2Str = DateFormatUtils.long2Str(timestamp, false, timeType)
                this@STExtenRankActivity.time = long2Str
                tv_receivde1_05.text = long2Str.replace("-", "/")
                onFRefresh()
            }
        }, 4, isVisiTab = true, isSEndTime = false)*/


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
                        this@STExtenRankActivity.timestamp = startTime
                        type = "$timeType"

                        val time = DateFormatUtils.long2Str(timestamp, false, timeType)
                        this@STExtenRankActivity.time = time
                        tv_receivde1_05?.text = time.replace("-", "/")
                        onFRefresh()

                    }
                    else -> {
                        if (startTime == 0L || endTime == 0L) {
                            return
                        }
                        this@STExtenRankActivity.timestamp = startTime
                        type = "4"
                        beginDate = DateFormatUtils.long2Str(startTime, false, timeType)
                        endDate = DateFormatUtils.long2Str(endTime, false, timeType)

                        tv_receivde1_05?.text =
                            "${beginDate.replace("-", "/")}-${endDate.replace("-", "/")}"
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
        when (url_type) {
            1 -> {
                if (!TextUtils.isEmpty(projectId)) {
                    map["projectId"] = projectId
                }
                if (!TextUtils.isEmpty(groupId)) {
                    map["groupId"] = groupId
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
                map["item"] = item //留电数:1,到访数:2,成交量:3,成交额:4"
                map["sortWay"] = sortWay//"sortBy不能为空,asc:正序;desc:倒序"
                map["sortBy"] = sortBy //完成：1，目标：2，完成率：3"
                map["page"] = "$page"      //	当前页	否	int	默认1
                map["pageSize"] = "10"       //	分页大小	否	int	默认10
            }
            3 -> {
                if (!TextUtils.isEmpty(projectId)) {
                    map["projectId"] = projectId
                }
            }
            else -> {
            }
        }
        return map
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> PerformStateImp.API_PER_STATE_BY_GROUP
            2 -> SystemSettImp.API_SYSTEM_PROJECT_LIST
            3 -> PerformStateImp.API_PER_CUR_GROUP_LIST
            else -> {
                ""
            }
        }


    }


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        mDatePicker1?.onDestroy()
        mDatePicker2?.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
