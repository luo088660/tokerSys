package com.toker.sys.view.home.activity.task.monthindicass

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.text.TextUtils
import android.view.View
import com.toker.reslib.Main
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.common.request.MonthIndicImp
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.view.home.activity.task.adapter.MonthIndicASAdapter
import com.toker.sys.view.home.activity.task.bean.MonthLBean
import com.toker.sys.view.home.activity.task.viewdaymiss.ViewDayMissActivity
import kotlinx.android.synthetic.main.activity_month_indic_as.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_month_indic_01.*
import kotlinx.android.synthetic.main.layout_month_indic_02.*
import kotlinx.android.synthetic.main.layout_progress_01.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class MonthIndicASActivity : BaseActivity<MonthIndicAssContract.View, MonthIndicAssPresenter>(),
    MonthIndicAssContract.View {
    override var mPresenter: MonthIndicAssPresenter = MonthIndicAssPresenter()
    var mBean: MonthLBean.Data? = null
    private val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm")
    private val sdf1 = SimpleDateFormat("yyyy-MM-dd")
    private var bean: MonthIndicImp? = null
    var mBeans: MutableList<MonthLBean.TaskTarget> = mutableListOf()
    var adapter: MonthIndicASAdapter? = null
    override fun layoutResID(): Int = R.layout.activity_month_indic_as


    override fun initView() {
        EventBus.getDefault().register(this)
        bean = intent.getSerializableExtra(Constants.BEANDATA) as MonthIndicImp
        setOnClickListener(img_back)
        setOnClickListener(btn_month_indic_01)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            btn_month_indic_01 -> {
                //日分解任务
                val intent = Intent(this, ViewDayMissActivity::class.java)
                intent.putExtra(Constants.BEANDATA, MonthIndicImp(mBean?.id!!, mBean!!.tableTag))
                startActivity(intent)
            }
            else -> {
                finish()
            }
        }
        finish()
    }

    override fun initData() {
        mPresenter.loadRepositories()
//        tv_title.text = "任务详情"
        rv_month_indic.layoutManager = GridLayoutManager(this, 1)
        adapter = MonthIndicASAdapter(this, mBeans)
        rv_month_indic.adapter = adapter
        rv_month_indic.addItemDecoration(getItemDecoration())

    }

    /**
     * 查看日任务
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun MonthLBean(event: MonthLBean.TaskTarget) {
        val intent = Intent(this, ViewDayMissActivity::class.java)
        intent.putExtra(Constants.BEANDATA, MonthIndicImp(event.id, event.tableTag))
        startActivity(intent)
    }


    override fun showData(data: MonthLBean.Data) {
        this.mBean = data
        tv_title.text = data.taskName
        tv_indic_01.text = data.projectName

        tv_indic_02.text =
            when (data.taskType.toInt()) {
                1 -> "周任务"
                2 -> {
                    btn_month_indic_01.visibility = GONE
                    tv_month_indic_zwfjrw.visibility = VISIBLE
                    ll_month_indic_.visibility = GONE
                    "日任务"
                }
                else -> "月任务"
            }

        tv_indic_03.text =
            when (data.status.toInt()) {
                1 -> {
                    tv_indic_03.background = resources.getDrawable(R.color.c_bg_weikaishi)
                    tv_indic_03.setTextColor(resources.getColor(R.color.c_txt_weikaishi))
                    "未开始"
                }
                2 -> {
                    tv_indic_03.background = resources.getDrawable(R.color.c_bg_yirenchou)
                    tv_indic_03.setTextColor(resources.getColor(R.color.c_txt_yirenchou))
                    "进行中"
                }
                3 -> {
                    tv_indic_03.background = resources.getDrawable(R.color.c_bg_yijieshu)
                    tv_indic_03.setTextColor(resources.getColor(R.color.c_txt_yijieshu))
                    "已结束"
                }
                else -> "草稿"

            }
        tv_indic_04.text = data.creator
        tv_indic_05.text = sdf.format(Date(data.createTime))

        tv_indic_06.text = data.objectList.joinToString { "${it.objectName}" }
        tv_indic_07.text = "${sdf1.format(Date(data.startTime))}至${sdf1.format(Date(data.endTime))}"
        val curTurnover = DecimalFormat("0.00").format(data.curTurnover.toFloat() / 10000)
        pb_bar_01.progress = data.curPhoneNum
        pb_bar_01.max = data.phoneNum

        pb_bar_02.progress = data.curVisitNum
        pb_bar_02.max = data.visitNum

        pb_bar_03.progress = data.curDealNum
        pb_bar_03.max = data.dealNum

        pb_bar_04.progress = curTurnover.toDouble().toInt()
        pb_bar_04.max = data.turnover

        tv_pb_011.visibility = GONE
        tv_pb_022.visibility = GONE
        tv_pb_033.visibility = GONE
        tv_pb_044.visibility = GONE
        tv_pb_01.text = spanned(data.curPhoneNum.toString(), data.phoneNum.toString())
        tv_pb_02.text = spanned(data.curVisitNum.toString(), data.visitNum.toString())
        tv_pb_03.text = spanned(data.curDealNum.toString(), data.dealNum.toString())
        tv_pb_04.text = spanned(curTurnover.toString(), data.turnover.toString())

        //任务审批
        //审批状态 0：未审批，1：审批通过，2：审批不通过
        ll_indic_02.visibility =
            if (data.approvalStatus == "0" || data.approvalStatus == "3") GONE else VISIBLE
        tv_indic2_01.text = data.approve
        tv_indic2_02.text = sdf1.format(Date(data.approveTime))
        tv_indic2_03.text =
            if (!TextUtils.isEmpty(data.approveRemark)) data.approveRemark else when (data.approvalStatus) {
                "0" -> "未审批"
                "1" -> "审批通过"
                else -> "审批不通过"
            }

        tv_indic3_01.text = data.creator
        tv_indic3_02.text = sdf.format(Date(data.createTime))

        if (data.taskTargets.isEmpty() || data.taskTargets[0] == null || data.taskTargets[0].toString() == "null") {
            tv_month_indic_zwfjrw.visibility = VISIBLE
            ll_month_indic_.visibility = GONE
            return
        }
        if (data.taskType.toInt() != 2 && !data.taskTargets.isNullOrEmpty()) {
            adapter?.refreshData(data.taskTargets)
        }
    }

    private fun spanned(num1: String, num2: String) =
        Html.fromHtml(getString(if (num1.toFloat()>=num2.toFloat())R.string.tip_task_num1 else R.string.tip_task_num, num1, num2))


    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["taskId"] = bean!!.id
        map["tableTag"] = bean!!.tableTag
        return map
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun getUrl(url_type: Int): String {
        return TaskManageImp.API_TASK_TARGET_TASK
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}