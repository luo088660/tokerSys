package com.toker.sys.view.home.activity.my.projtdeta

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.AttendSchedulImp
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.my.bean.ProjectByTeamBean
import com.toker.sys.view.home.activity.my.bean.ProjectFormanBean
import com.toker.sys.view.home.activity.my.bean.SchedMonthBean
import com.toker.sys.view.home.activity.my.projtdeta.viewattendan.ViewAttendanActivity
import com.toker.sys.view.home.activity.my.projtdeta.viewshift.ViewShiftActivity
import com.toker.sys.view.home.activity.my.teammem.TeamMemActivity
import com.toker.sys.view.home.activity.sheet.stextenrank.STExtenRankActivity
import com.toker.sys.view.home.activity.sheet.stteamrank.STTeamRankActivity
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean
import kotlinx.android.synthetic.main.layout_attend_statis.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_my_performance.*
import kotlinx.android.synthetic.main.layout_overall_perfor.*
import kotlinx.android.synthetic.main.layout_perfor.*
import kotlinx.android.synthetic.main.layout_schedul_plan.*
import kotlinx.android.synthetic.main.layout_team_member.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 项目详情
 * @author yyx
 */

class ProjtDetaActivity : BaseActivity<ProjtDetaContract.View, ProjtDetaPresenter>(),
    ProjtDetaContract.View {

    override var mPresenter: ProjtDetaPresenter = ProjtDetaPresenter()
    private var mDatePicker1: CustomDatePicker? = null
    private var mDatePicker2: CustomDatePicker? = null
    private var timeType = 3
    private val thisTimestamp = System.currentTimeMillis()
    var data: ProjectListBean.Data? = null
    private var type = "2"
    private var timestamp = System.currentTimeMillis()
    private var time = DateFormatUtils.long2Str(System.currentTimeMillis(), false, 2)
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val sdfKY = SimpleDateFormat("yyyy")
    private val sdfKM = SimpleDateFormat("MM")
    private val sdfPb = SimpleDateFormat("yyyy-MM")
    //切换 考勤统计或排班计划的时间选择
    var isScheAtt = false

    override fun layoutResID(): Int = R.layout.activity_projt_deta
    //排班计划 时间
    private var monthPB = ""
    private var year = ""
    private var month = ""
    override fun initView() {
        data = intent.getSerializableExtra("event") as ProjectListBean.Data
        LogUtils.d(TAG, "data:$data ");

        tv_title.text = data?.projectName
        monthPB = sdfPb.format(Date(System.currentTimeMillis()))
        year = sdfKY.format(Date(System.currentTimeMillis()))
        month = sdfKM.format(Date(System.currentTimeMillis()))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {


        mPresenter.loadRepositories()
        setOnClickListener(img_back)
        setOnClickListener(tv_perfor_01)
        setOnClickListener(tv_perfor_02)
        setOnClickListener(ll_all_per_01)
        setOnClickListener(tv_team_mber_03)
        setOnClickListener(tv_team_mber_04)
        setOnClickListener(tv_attend_sta_05)
        setOnClickListener(tv_schedul_pl_01)
        setOnClickListener(tv_schedul_pl_06)
        setOnClickListener(tv_attend_sta_01)
        //初始化时间
        initDatePicker()
        tv_all_per_02.text = time.replace("-","/")
        tv_schedul_pl_01.text = monthPB
        tv_attend_sta_01.text = monthPB
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        //当前时间
        when (v) {
            img_back -> {
                finish()
            }
            //团队排名
            tv_perfor_01 -> {

                val intent = Intent(this, STTeamRankActivity::class.java)
                intent.putExtra("projectId", data?.projectId)
                intent.putExtra("projectName", data?.projectName)
                startActivity(intent)
            }
            //拓客员排名
            tv_perfor_02 -> {
                val intent = Intent(this, STExtenRankActivity::class.java)
                intent.putExtra("projectId", data?.projectId)
                intent.putExtra("projectName", data?.projectName)
                startActivity(intent)
            }
            //业绩时间
            //开始时间
            //结束时间
            ll_all_per_01 -> {
                mDatePicker1!!.show(timestamp)
            }
            //排班计划 时间选择
            tv_schedul_pl_01 -> {
                isScheAtt = false
                mDatePicker2!!.show(thisTimestamp)
            }
            //考勤统计 时间选择
            tv_attend_sta_01 -> {
                isScheAtt = true
                mDatePicker2!!.show(thisTimestamp)
            }

            //拓客员
            tv_team_mber_03 -> {//团队成员查询
                val intent = Intent(this, TeamMemActivity::class.java)
                intent.putExtra("type", 0)
                intent.putExtra("data", data)
                startActivity(intent)

            }
            //小蜜蜂
            tv_team_mber_04 -> {//TeamMberTnqActivity
                val intent = Intent(this, TeamMemActivity::class.java)
                intent.putExtra("type", 1)
                intent.putExtra("data", data)
                startActivity(intent)
            }
            //查看考勤明细 ViewAttendan
            tv_attend_sta_05 -> {
                val intent = Intent(this, ViewAttendanActivity::class.java)
                intent.putExtra("projectId", data?.projectId)
                intent.putExtra("projectName", data?.projectName)
                startActivity(intent)
            }
            //查看排班明细 ViewShift
            tv_schedul_pl_06 -> {
                val intent = Intent(this, ViewShiftActivity::class.java)
                intent.putExtra("projectId", data?.projectId)
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    /**
     * 时间选择
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {

        // 通过时间戳初始化日期，毫秒级别
        /*mDatePicker1 = CustomDatePicker(this, object : CustomDatePicker.CallbackListen {
            override fun onTimeSelected(timestamp: Long, timeType: Int) {
                this@ProjtDetaActivity.timestamp = timestamp
                type = "$timeType"
                val time = DateFormatUtils.long2Str(timestamp, false, timeType)
                this@ProjtDetaActivity.time = time
                tv_all_per_02.text = time.replace("-","/")
                mPresenter.getMyPerformance()
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
                        this@ProjtDetaActivity.timestamp = startTime
                        type = "$timeType"

                        val time = DateFormatUtils.long2Str(timestamp, false, timeType)
                        this@ProjtDetaActivity.time = time
                        tv_all_per_02?.text = time.replace("-", "/")
                        mPresenter.getMyPerformance()

                    }
                    else -> {

                        if (startTime == 0L || endTime == 0L) {
                            return
                        }
                        this@ProjtDetaActivity.timestamp = startTime
                        type = "4"
                        beginDate = DateFormatUtils.long2Str(startTime, false, timeType)
                        endDate = DateFormatUtils.long2Str(endTime, false, timeType)

                        tv_all_per_02?.text =
                            "${beginDate.replace("-", "/")}\n${endDate.replace("-", "/")}"
                        mPresenter.getMyPerformance()
                    }
                }

            }

        }, 4, true, true)

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker2 = CustomDatePicker(this, object : CustomDatePicker.CallbackSEListen {
            override fun onTimeSelected(startTime: Long, endTime: Long, timeType: Int) {
                LogUtils.d(TAG, "timeType:$timeType ");
                (if (isScheAtt) tv_attend_sta_01 else tv_schedul_pl_01).text =
                    DateFormatUtils.long2Str(startTime, false, 2)
                if (isScheAtt) {
                    year = sdfKY.format(Date(startTime))
                    month = sdfKM.format(Date(startTime))
                    mPresenter.AttendanceByTeam()
                } else {
                    monthPB = DateFormatUtils.long2Str(startTime, false, 2)
                    mPresenter.schedulingByMonth()
                }

            }
        }, 2, isVisiTab = false, isSEndTime = false)
        mDatePicker2?.setCanShowPrecise(2)

        // 不允许点击屏幕或物理返回键关闭
//        mDatePicker1?.setCancelable(false)
        // 不显示时和分
        mDatePicker1?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mDatePicker1?.setScrollLoop(true)
        // 不允许滚动动画
        mDatePicker1?.setCanShowAnim(false)


        // 不允许点击屏幕或物理返回键关闭
//        mDatePicker2?.setCancelable(false)
        // 不显示时和分
        mDatePicker2?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mDatePicker2?.setScrollLoop(true)
        // 不允许滚动动画
        mDatePicker2?.setCanShowAnim(false)

    }
    var beginDate = ""
    var endDate = ""
    //团队成员
    //拓客管理员
    override fun showGroupAdmin(userName: String) {
        tv_team_mber_01.text = userName.replace("；", ",")
    }

    //统计拓客团队信息数量
    override fun showCountGroup(data: ProjtDetaPresenter.CountGroup.Data) {
        /*userSum	拓客员数量
                beeSum	小蜜蜂数量*/
        ll_team_mber_02.visibility = if (AppApplication.TYPE == Constants.RESCUE1) GONE else VISIBLE
        tv_team_mber_02.text = "${data.groupSum}"
        tv_team_mber_03.text = "${data.userSum}"
        tv_team_mber_04.text = "${data.beeSum}"
    }

    //用户的业绩查询
    override fun preForMan(data: ProjectFormanBean.Data) {
            tv_ly_perfor_01.text = "${if (!TextUtils.isEmpty(data.curPhoneNum))data.curPhoneNum else "0"}"
            tv_ly_perfor_02.text = "${if (!TextUtils.isEmpty(data.curVisitNum))data.curVisitNum else "0"}"
            tv_ly_perfor_03.text = "${if (!TextUtils.isEmpty(data.curDealNum))data.curDealNum else "0"}"
            tv_ly_perfor_04.text = "${if (!TextUtils.isEmpty(data.curTurnover))data.curTurnover else "0"}"
    }

    override fun preErrorForMan() {
        tv_ly_perfor_01.text = "0"
        tv_ly_perfor_02.text = "0"
        tv_ly_perfor_03.text = "0"
        tv_ly_perfor_04.text = "0"

    }

    //排班计划
    override fun showDataSched(data: SchedMonthBean.Data) {
        tv_schedul_pl_02.text = "${data.workNum}"
        tv_schedul_pl_03.text = "${data.personalLeaveNum}"
        tv_schedul_pl_04.text = "${data.sickLeaveNum}"
        tv_schedul_pl_05.text = "${data.restedNum}"
    }

    //无排班情况
    override fun onErrorSched(desc: String) {

        if (desc != "该项目当月尚未排班"){
            toast(desc)
        }
        tv_schedul_pl_02.text = "0"
        tv_schedul_pl_03.text = "0"
        tv_schedul_pl_04.text = "0"
        tv_schedul_pl_05.text = "0"
    }

    //项目或者团队考勤统计
    override fun byTeamData(data: ProjectByTeamBean.Data) {
        tv_attend_sta_02.text = if (data.totalPeople.isNullOrEmpty()) "0" else "${data.totalPeople}"
        tv_attend_sta_03.text = if (data.regularNum.isNullOrEmpty()) "0" else "${data.regularNum}"
        tv_attend_sta_04.text =
            if (data.exceptionNum.isNullOrEmpty()) "0" else "${data.exceptionNum}"
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                map["month"] = monthPB// "2019-08"
                map["projectId"] = data?.projectId!!
            }
            2 -> {
                map["projectId"] = data?.projectId!!//"122"
//                map["groupId"] =  data?.groupId//"154a504e-79e2-4d79-87ba-424e10dfdca7"
                map["year"] = year
                map["month"] = month
            }
            3 -> {
                map["projectId"] = data?.projectId!!
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
            }
            4, 5 -> {
                map["projectId"] = data?.projectId!!
            }

            else -> {
            }
        }

        return map
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {

            1 -> AttendSchedulImp.API_CUST_ATTEND_BY_MONTH
            2 -> AttendSchedulImp.API_CUST_ATTEND_BY_TEAM
            3 -> PerformStateImp.API_PER_STATE_PER_FORMANCE
            4 -> PerformStateImp.API_PER_GET_GROUP_ADMIN
            5 -> PerformStateImp.API_PER_COUNT_GROUP
            else -> {
                ""
            }
        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
}
