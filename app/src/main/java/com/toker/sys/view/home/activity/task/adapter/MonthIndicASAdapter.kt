package com.toker.sys.view.home.activity.task.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.view.home.activity.task.bean.MonthLBean
import com.toker.sys.view.home.activity.task.viewdaymiss.ViewDayMissActivity
import kotlinx.android.synthetic.main.layout_month_indic_03.*
import org.greenrobot.eventbus.EventBus
import java.lang.Exception
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class MonthIndicASAdapter(val mContext: Context, var mBeans: MutableList<MonthLBean.TaskTarget>) :
    RecyclerView.Adapter<MonthIndicASAdapter.MASViewHolder>() {

    private val sdf1 = SimpleDateFormat("yyyy/MM/dd")
    private var mGroup: MutableMap<Int, Boolean> = HashMap()
    fun refreshData(mBeans: MutableList<MonthLBean.TaskTarget>) {
        this.mBeans = mBeans
        for (i in 0..mBeans.size) {
            mGroup[i] = false
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MASViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_month_indic_as, null)
        return MASViewHolder(view)

    }

    override fun getItemCount(): Int {
        return mBeans.size

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(p0: MASViewHolder, p1: Int) {

        val target = mBeans[p1]
        //任务分解

        try {
            p0.tv03.text =
                "${sdf1.format(Date(target.startTime))}\t至\t${sdf1.format(Date(target.endTime))}"

            val curTurnover = DecimalFormat("0.00").format(target.curTurnover.toFloat() / 10000)
            p0.tv04.text = target.objectList.joinToString { "${it.objectName}" }
            p0.tv05.text = "${target.curPhoneNum}/${target.phoneNum}"
            p0.tv06.text = "${target.curVisitNum}/${target.visitNum}"

            p0.tv07.text = "${target.curDealNum}/${target.dealNum}"
            p0.tv08.text = "$curTurnover/${target.turnover}"

            p0.rView.visibility = if (mGroup[p1]!!) View.VISIBLE else View.GONE
            val drawable =
                mContext.resources.getDrawable(if (mGroup[p1]!!) R.mipmap.icon_more_00 else R.mipmap.icon_more_01)
            p0.btnM01.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
            p0.btnM01.text = if (mGroup[p1]!!) "收起" else "展开任务明细"

            p0.rView.layoutManager = GridLayoutManager(mContext, 1)
            p0.btnM01.setOnClickListener {

                mGroup.forEach { (t, u) ->
                    if (p1 == t) {
                        mGroup[p1] = !this.mGroup[p1]!!
                    } else {
                        mGroup[t] = false
                    }
                }
                notifyDataSetChanged()
            }
            p0.btnM02.visibility = if (target.objectList.isNotEmpty()) View.VISIBLE else View.GONE
            p0.rView.adapter = TaskDecomAdapter(mContext, target.objectList)
            p0.btnM02.setOnClickListener {
                EventBus.getDefault().post(target)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    class MASViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tv03 = v.findViewById(R.id.tv_indic3_03) as TextView
        val tv04 = v.findViewById(R.id.tv_indic3_04) as TextView
        val tv05 = v.findViewById(R.id.tv_indic3_05) as TextView
        val tv06 = v.findViewById(R.id.tv_indic3_06) as TextView
        val tv07 = v.findViewById(R.id.tv_indic3_07) as TextView
        val tv08 = v.findViewById(R.id.tv_indic3_08) as TextView
        val btnM01 = v.findViewById(R.id.tv_indic3_more) as TextView
        val btnM02 = v.findViewById(R.id.btn_month_indic_01) as TextView
        val rView = v.findViewById(R.id.rv_indic3) as RecyclerView
    }
}