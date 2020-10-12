package com.toker.sys.view.home.activity.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.bean.STExtenRankBean

class STProjectRanAdapter(
    val mContext: Context,
    var mBeans: MutableList<STExtenRankBean.Record>
) :RecyclerView.Adapter<STProjectRanAdapter.STPViewHolder>(){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): STPViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_stp_project,p0,false)
         return STPViewHolder(view)
    }
    fun refreshData(mBeans: MutableList<STExtenRankBean.Record>) {
        this.mBeans = mBeans
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: STPViewHolder, p1: Int) {
        val data = mBeans[p1]
        p0.tv01.text = "${p1+1}"
        p0.tv02.text = if (!TextUtils.isEmpty(data.projectName))data.projectName else "--"
        p0.tv03.text = if (!TextUtils.isEmpty(data.curNum))data.curNum else "--"
        p0.tv04.text = if (!TextUtils.isEmpty(data.targetNum))data.targetNum else "--"
        p0.tv05.text = if (!TextUtils.isEmpty(data.percent))"${(data.percent.toFloat()*100).toInt()}%" else "--"

    }


    class STPViewHolder(v:View):RecyclerView.ViewHolder(v){
        val tv01 = v.findViewById<TextView>(R.id.tv_stp_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_stp_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_stp_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_stp_04)
        val tv05 = v.findViewById<TextView>(R.id.tv_stp_05)
    }
}