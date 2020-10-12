package com.toker.sys.view.home.activity.task.adminitrandetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.utils.tools.DataUtil
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.tools.TimeSwitchUtils
import com.toker.sys.view.home.activity.sheet.event.TimeEvent
import com.toker.sys.view.home.activity.task.bean.TransactTaskBean
import com.toker.sys.view.home.fragment.custom.adapter.CustomAdapter
import com.toker.sys.view.home.fragment.event.CustomEvent
import com.toker.sys.view.home.fragment.task.item.missreport.MissReportFragment
import com.toker.sys.view.home.fragment.task.item.tasktrajectory.TaskTrajectoryFragment
import kotlinx.android.synthetic.main.activity_admini_tran_detail.*
import kotlinx.android.synthetic.main.layout_admini_tran_detail_01.*
import kotlinx.android.synthetic.main.layout_admini_tran_detail_02.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

/**
 * 管理员 事务详情
 * @author yyx
 */

class AdminiTranDetailActivity :
    BaseActivity<AdminiTranDetailContract.View, AdminiTranDetailPresenter>(),
    AdminiTranDetailContract.View {

    override var mPresenter: AdminiTranDetailPresenter = AdminiTranDetailPresenter()

    private val mBeans = arrayOf("任务汇报", "任务轨迹")
    var id: String = ""
    var tableTag: String = ""
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    var type = 4

    val formatType1 = "yyyy-MM-dd"
    var endDate: Long = 0
    var startDate: Long = 0

    var timeZone: TimeSwitchUtils? = null
    override fun layoutResID(): Int = R.layout.activity_admini_tran_detail

    override fun initView() {
        EventBus.getDefault().register(this)
        id = intent.getStringExtra("id")
        type = intent.getIntExtra("type", 4)
        tableTag = intent.getStringExtra("tableTag")
        mPresenter.loadRepositories()
        rv_admini_tran.layoutManager = GridLayoutManager(this, 2)
        val customAdapter = CustomAdapter(this, mBeans)
        rv_admini_tran.adapter = customAdapter
        if (type == 5) {
            customAdapter.upDataView(1)
        }
    }

    override fun initData() {
        timeZone = TimeSwitchUtils()
        val thisTime = timeZone?.thisTime()!!
        tv_time.text = thisTime
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("tableTag", tableTag)
        bundle.putString("date", thisTime)
        setIntentFragment(
            if (type == 4) MissReportFragment.newInstance() else TaskTrajectoryFragment.newInstance(),
            bundle
        )
        setOnClickListener(img_back)
        setOnClickListener(img_time_left)
        setOnClickListener(img_time_right)
    }


    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            //时间切换
            img_time_left -> {
                val updateTime = "${tv_time.text}"
                val toLong = DataUtil.stringToLong(updateTime, formatType1)
                LogUtils.d(TAG, "toLong:$toLong ");
                LogUtils.d(TAG, "startDate:$startDate ");
                if (startDate < toLong) {
                    val timeLeft = timeZone?.imgTimeLeft()!!
                    tv_time.text = timeLeft
                    EventBus.getDefault().post(TimeEvent(timeLeft, "1"))
                }
            }
            img_time_right -> {
                val updateTime = "${tv_time.text}"
                val toLong = DataUtil.stringToLong(updateTime, formatType1)
                LogUtils.d(TAG, "toLong:$toLong ");
                LogUtils.d(TAG, "endDate:$endDate ");
                if (endDate > toLong) {
                    val timeRight = timeZone?.imgTimeRight()!!
                    tv_time.text = timeRight
                    EventBus.getDefault().post(TimeEvent(timeRight, "1"))
                }
            }
            else -> {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun customEvent(event: CustomEvent) {
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("tableTag", tableTag)
        bundle.putString("date", "${tv_time.text}")
        setIntentFragment(
            when (event.name) {
                mBeans[0] -> {
                    MissReportFragment.newInstance()
                }
                else -> {
                    TaskTrajectoryFragment.newInstance()
                }
            }, bundle
        )


    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    //数据展示
    @SuppressLint("SetTextI18n")
    override fun showData(data: TransactTaskBean.Data) {
        endDate = data.endDate
        startDate = data.startDate
        tv_title.text = data.taskName
        tv_admini_tran_01.text = data.projectName
        tv_admini_tran_02.text =
            "${sdf.format(Date(data.startDate))}至${sdf.format(Date(data.endDate))}\t${data.startTime}~${data.endTime}"
        tv_admini_tran_03.text = data.address
        tv_admini_tran_04.text = data.content
        tv_admini_tran_05.text = data.objectList.joinToString { it.objectName }
        tv_admini_tran_06.text = when (data.status.toInt()) {
            1 -> {
                tv_admini_tran_06.setTextColor(resources.getColor(R.color.c_txt_weikaishi))
                tv_admini_tran_06.setBackgroundColor(resources.getColor(R.color.c_bg_weikaishi))
                "未开始"
            }
            2 -> {
                tv_admini_tran_06.setTextColor(resources.getColor(R.color.c_txt_yirenchou))
                tv_admini_tran_06.setBackgroundColor(resources.getColor(R.color.c_bg_yirenchou))
                "进行中"
            }
            3 -> {
                tv_admini_tran_06.setTextColor(resources.getColor(R.color.c_txt_tjchenggong))
                tv_admini_tran_06.setBackgroundColor(resources.getColor(R.color.c_bg_tjchenggong))
                "已完成"
            }
            else -> {
                "草稿中"
            }
        }
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                map["taskId"] = id
                map["tableTag"] = tableTag
            }
            else -> {
            }
        }

        return map
    }

    private fun setIntentFragment(fragment: Fragment, bundle: Bundle) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        beginTransaction.replace(R.id.fl_admini_tran_detail, fragment)
        beginTransaction.commitAllowingStateLoss()
    }

    override fun getUrl(url_type: Int): String {
//        countEventTaskRecord
        return when (url_type) {
            1 -> {
                TaskManageImp.API_TASK_EVENTTASK
            }

            else -> {
                ""
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
