package com.toker.sys.view.home.fragment.task.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.mvp.BaseAdapter
import com.toker.sys.view.home.fragment.event.TaskHomeEvent
import com.toker.sys.view.home.fragment.task.bean.PageData
import org.greenrobot.eventbus.EventBus
import java.text.DecimalFormat
import java.util.*

class TStartAdapter(
    val context: Context,
    var mBeans: MutableList<PageData>
) : BaseAdapter<PageData, TStartAdapter.TSViewHodler>(context, mBeans) {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TSViewHodler {
        if (mHeaderView != null && p1 == TYPE_HEADER) {
            return TSViewHodler(mHeaderView!!)
        }
        val view = LayoutInflater.from(context).inflate(R.layout.layout_t_entire_02, null)

        return TSViewHodler(view)
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(p0: TSViewHodler, p1: Int) {
        if (getItemViewType(p1) == TYPE_HEADER) {
            return
        }
        val position = getRealPosition(p0)
        val data = mData[position]
        p0.tv01.text = data.taskName
        p0.tv03.text = data.projectName
        p0.tv04.text =
            when (data.taskType.toInt()) {
                1 -> "周任务"
                2 -> "日任务"
                else -> "月任务"
            }
        p0.tv05.text =
            when (data.status.toInt()) {
                1 -> {
                    p0.tv05.background = context.resources.getDrawable(R.color.c_bg_weikaishi, null)
                    "未开始"
                }
                2 -> {
                    p0.tv05.background = context.resources.getDrawable(R.color.c_bg_jinxingzhong, null)
                    "进行中"
                }
                3 -> {
                    p0.tv05.background = context.resources.getDrawable(R.color.c_bg_yijieshu, null)
                    "已结束"
                }
                else -> "草稿"

            }
        p0.tv05.setTextColor( context.resources.getColor(
            when (data.status.toInt()) {
                1 -> R.color.c_txt_weikaishi
                2 -> R.color.c_txt_jinxingzhong
                else -> R.color.c_txt_yijieshu

            })
        )
        p0.tv07.text = data.creator
        val objectList = data.objectList
        p0.tv08.text = if (objectList[0].objectName.contains("团队")){
            objectList[0].objectName
        }else {
            if (objectList.size>1) "${objectList[0].objectName}等${objectList.size}人" else "${objectList[0].objectName}"
        }
        p0.tv09.text =  sdfs.format(Date(data.createTime))//data.createTime

        p0.tv10.text =
            "${sdf.format(Date(data.startTime))}至${sdf.format(Date(data.endTime))}".replace("-", "/")
        val curTurnover = DecimalFormat("0.00").format(data.curTurnover.toFloat() / 10000)
        p0.tvPb01.text = spanned(data.curPhoneNum.toString(), data.phoneNum.toString())
        p0.tvPb02.text = spanned(data.curVisitNum.toString(), data.visitNum.toString())
        p0.tvPb03.text = spanned(data.curDealNum.toString(), data.dealNum.toString())
        p0.tvPb04.text = spanned(curTurnover.toString(), data.turnover.toString())

        p0.tvPb011.layoutParams = LinearLayout.LayoutParams(0,0,0f)
        p0.tvPb022.layoutParams = LinearLayout.LayoutParams(0,0,0f)
        p0.tvPb033.layoutParams = LinearLayout.LayoutParams(0,0,0f)
        p0.tvPb044.layoutParams = LinearLayout.LayoutParams(0,0,0f)
//        p0.tvPb011.text = "${data.curPhoneNum}%"
//        p0.tvPb022.text = "${data.curVisitNum}%"
//        p0.tvPb033.text = "${data.curDealNum}%"
//        p0.tvPb044.text = "${data.curTurnover}%"
        p0.pb01.progress = data.curPhoneNum
        p0.pb01.max = data.phoneNum

        p0.pb02.progress = data.curVisitNum
        p0.pb02.max = data.visitNum

        p0.pb03.progress = data.curDealNum
        p0.pb03.max = data.dealNum

        p0.pb04.progress = curTurnover.toDouble().toInt()
        p0.pb04.max = data.turnover.toInt()
        p0.ll02.visibility = View.GONE
        p0.ll03.visibility = View.GONE

        p0.tv02.setOnClickListener {
            //跳转详情
            EventBus.getDefault().post(TaskHomeEvent(2, data))
        }
    }

    private fun spanned(num1: String, num2: String) ="完成$num1/目标$num2"

    inner class TSViewHodler(v: View) : RecyclerView.ViewHolder(v) {


        val tv01 = v.findViewById<TextView>(R.id.tv_entire_see_deta_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_entire_see_deta_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_entire_see_deta_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_entire_see_deta_04)
        val tv05 = v.findViewById<TextView>(R.id.tv_entire_see_deta_05)
        val tv07 = v.findViewById<TextView>(R.id.tv_entire_see_deta_07)
        val tv08 = v.findViewById<TextView>(R.id.tv_entire_see_deta_08)
        val tv09 = v.findViewById<TextView>(R.id.tv_entire_see_deta_09)
        val tv10 = v.findViewById<TextView>(R.id.tv_entire_see_deta_10)


        val tvPb01 = v.findViewById<TextView>(R.id.tv_pb_01)
        val tvPb02 = v.findViewById<TextView>(R.id.tv_pb_02)
        val tvPb03 = v.findViewById<TextView>(R.id.tv_pb_03)
        val tvPb04 = v.findViewById<TextView>(R.id.tv_pb_04)
        val tvPb011 = v.findViewById<TextView>(R.id.tv_pb_011)
        val tvPb022 = v.findViewById<TextView>(R.id.tv_pb_022)
        val tvPb033 = v.findViewById<TextView>(R.id.tv_pb_033)
        val tvPb044 = v.findViewById<TextView>(R.id.tv_pb_044)

        val pb01 = v.findViewById<ProgressBar>(R.id.pb_bar_01)
        val pb02 = v.findViewById<ProgressBar>(R.id.pb_bar_02)
        val pb03 = v.findViewById<ProgressBar>(R.id.pb_bar_03)
        val pb04 = v.findViewById<ProgressBar>(R.id.pb_bar_04)
        val ll02 = v.findViewById<LinearLayout>(R.id.ll_t_entire2)
        val ll03 = v.findViewById<LinearLayout>(R.id.ll_t_entire3)

    }
}