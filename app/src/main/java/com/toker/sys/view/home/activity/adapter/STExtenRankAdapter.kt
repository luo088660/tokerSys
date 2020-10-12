package com.toker.sys.view.home.activity.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.mvp.BaseAdapter
import com.toker.sys.view.home.bean.STExtenRankBean

class STExtenRankAdapter(
    val mContext: Context,
    var mBeans: MutableList<STExtenRankBean.Record>
) :RecyclerView.Adapter<STExtenRankAdapter.STERViewHolder>(){

    fun refreshData(mBeans: MutableList<STExtenRankBean.Record>) {
        this.mBeans = mBeans
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): STERViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_ster_rank,p0,false)
        return STERViewHolder(view)
    }

    override fun getItemCount(): Int {

        return mBeans.size
    }

    override fun onBindViewHolder(p0: STERViewHolder, p1: Int) {
        val data = mBeans[p1]
        p0.tv01.text = "${p1+1}"
        p0.tv02.text = data.userName
        p0.tv03.text = if (!TextUtils.isEmpty(data.groupName))data.groupName else "--"
        p0.tv04.text = if (!TextUtils.isEmpty(data.projectName))data.projectName else "--"
        p0.tv05.text = if (!TextUtils.isEmpty(data.curNum))data.curNum else "--"
    }


    class STERViewHolder(v:View):RecyclerView.ViewHolder(v){
        val tv01 = v.findViewById<TextView>(R.id.tv_ster_rank_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_ster_rank_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_ster_rank_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_ster_rank_04)
        val tv05 = v.findViewById<TextView>(R.id.tv_ster_rank_05)

    }
}