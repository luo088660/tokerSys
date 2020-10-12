package com.toker.sys.view.home.fragment.task.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.mvp.BaseAdapter
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.fragment.event.TaskHomeEvent
import com.toker.sys.view.home.fragment.task.item.controller.treceived.PageData
import org.greenrobot.eventbus.EventBus
import java.util.*

class TRTaskPageAdapter(
    val context: Context,
    var mBeans: MutableList<PageData>

) : BaseAdapter<PageData, TRTaskPageAdapter.TRTViewHodler>(context, mBeans) {
    private val TAG = "TRTaskPageAdapter";
    var isfo = true
    var isChange = false

    constructor(
        context: Context,
        mBeans: MutableList<PageData>, isfo: Boolean
    ) : this(context, mBeans) {
        this.isfo = isfo
    }
    constructor(
        context: Context,
        mBeans: MutableList<PageData>, isfo: Boolean, isChange:Boolean
    ) : this(context, mBeans) {
        this.isfo = isfo
        this.isChange = isChange
    }

    fun refreshData(mBeans: MutableList<PageData>, isfo: Boolean) {
        this.isfo = isfo
        this.mBeans = mBeans
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TRTViewHodler {
        if (mHeaderView != null && p1 == TYPE_HEADER) {
            return TRTViewHodler(mHeaderView!!)
        }
        val view = LayoutInflater.from(context).inflate(R.layout.layout_t_entire_03, null)
        return TRTViewHodler(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: TRTViewHodler, p1: Int) {
        if (getItemViewType(p1) == TYPE_HEADER) {
            return
        }

        val position = getRealPosition(p0)
        val data = mData[position]
        LogUtils.d(TAG, "data:$data ");

        p0.tv01.text = data.taskName
        p0.tv011.text = data.projectName
        p0.tv02.text = data.creator
        p0.tv03.text = when (data.status.toInt()) {
            1 -> {
                p0.tv03.setTextColor(context.resources.getColor(R.color.c_txt_weikaishi))
                p0.tv03.setBackgroundColor(context.resources.getColor(R.color.c_bg_weikaishi))
                "未开始"

            }
            2 -> {
                p0.tv03.setTextColor(context.resources.getColor(R.color.c_txt_yirenchou))
                p0.tv03.setBackgroundColor(context.resources.getColor(R.color.c_bg_yirenchou))
                "进行中"
            }
            3 -> {
                p0.tv03.setTextColor(context.resources.getColor(R.color.c_txt_tjchenggong))
                p0.tv03.setBackgroundColor(context.resources.getColor(R.color.c_bg_tjchenggong))
                "已完成"
            }
            else -> {
                "草稿中"
            }
        }
        p0.tv06.text = sdfs.format(Date(data.createTime))
        p0.tv066.text = "${data.objectList[0].objectName}等${data.objectList.size}人"
        p0.tv07.text =
            "${sdf.format(Date(data.startDate))}至${sdf.format(Date(data.endDate))}\t${data.startTime}~${data.endTime}"
        p0.tv08.text = data.address
        p0.tv09.text = data.content
        p0.ll10.visibility = View.GONE
        p0.imgJi.visibility = if (data.level=="1") View.VISIBLE else View.GONE


        p0.tv11.text = Html.fromHtml(String.format(context.resources.getString(R.string.tip_task_number_03),data.recordNum,data.unRecordNum))//"已汇报${data.recordNum}人;未汇报${data.unRecordNum}人"
        p0.tv12.text = Html.fromHtml(String.format(context.resources.getString(R.string.tip_task_number_04),data.rightNum,data.errorNum))//"正常${data.rightNum}人;异常${data.errorNum}人"
        val isHasRecord = data.hasRecord == "1"//已经填写报告
        LogUtils.d(TAG, "isfo:$isfo ");
        LogUtils.d(TAG, "isHasRecord:$isHasRecord ");
        p0.btnEn01.text = if (isfo||isHasRecord) "查看任务汇报" else "填写任务汇报"
        //未开始，已完成任务 不能进行任务汇报
        if ((AppApplication.TYPE== Constants.RESCUE4&&!isfo&&isHasRecord)||(isChange&&!isfo)) {
            //拓客组长 我的任务 已填写任务汇报
            p0.btnEn01.text = "填写任务汇报"
            p0.btnEn01.isEnabled = false
            p0.btnEn01.background = context.resources.getDrawable(R.drawable.btn_bg_login_normal)
        }
        p0.btnEn01.setOnClickListener {
            EventBus.getDefault().post(TaskHomeEvent(if (isfo||isHasRecord) 4 else 6, data.id, data.tableTag))
        }
        p0.btnEn02.setOnClickListener {
            EventBus.getDefault().post(TaskHomeEvent(if (isfo) 5 else 7, data.id, data.tableTag, data.updateTime))
        }
        p0.tvData.setOnClickListener {
//            EventBus.getDefault().post(TaskHomeEvent(if (isfo) 4 else 7, data.id, data.tableTag))
            EventBus.getDefault().post(TaskHomeEvent(if (isfo) 5 else 7, data.id, data.tableTag, data.updateTime))
        }
    }


    class TRTViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_t_entire3_01)
        val tv011 = v.findViewById<TextView>(R.id.btn_t_entire3_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_t_entire3_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_t_entire3_03)
        val tv06 = v.findViewById<TextView>(R.id.tv_t_entire3_06)
        val tv066 = v.findViewById<TextView>(R.id.tv_t_entire3_066)
        val tv07 = v.findViewById<TextView>(R.id.tv_t_entire3_07)
        val tv08 = v.findViewById<TextView>(R.id.tv_t_entire3_08)
        val tv09 = v.findViewById<TextView>(R.id.tv_t_entire3_09)
        val ll10 = v.findViewById<LinearLayout>(R.id.ll_t_entire3_10)
        val tv11 = v.findViewById<TextView>(R.id.tv_t_entire3_11)
        val tv12 = v.findViewById<TextView>(R.id.tv_t_entire3_12)
        val imgJi = v.findViewById<ImageView>(R.id.img_icon_ji)

        val tvData = v.findViewById<TextView>(R.id.tv_entire_se03_deta)
        val btnEn01 = v.findViewById<TextView>(R.id.btn_t_entire_03_01)
        val btnEn02 = v.findViewById<TextView>(R.id.btn_t_entire_03_02)

    }
}