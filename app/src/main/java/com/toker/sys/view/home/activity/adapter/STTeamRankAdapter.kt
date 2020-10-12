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
import com.toker.sys.view.home.activity.my.bean.STTeamRankBean
import com.toker.sys.view.home.bean.STExtenRankBean

class STTeamRankAdapter(
    val mContext: Context,
    var mBeans: MutableList<STExtenRankBean.Record>
) :BaseAdapter<STExtenRankBean.Record,STTeamRankAdapter.STTRViewHodler>(mContext,mBeans){
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): STTRViewHodler {
        if (mHeaderView != null && p1 == TYPE_HEADER) {
            return STTRViewHodler(mHeaderView!!)
        }
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_st_team_rank,null)
        return STTRViewHodler(view)

    }

    override fun onBindViewHolder(p0: STTRViewHodler, p1: Int) {
        if (getItemViewType(p1) == TYPE_HEADER) {
            return
        }
        val position = getRealPosition(p0)
        val data = mData[position]
        p0.tv01.text = "$p1"
        p0.tv02.text = if (!TextUtils.isEmpty(data.groupName))data.groupName else "--"
        p0.tv03.text = if (!TextUtils.isEmpty(data.projectName))data.projectName else "--"
        p0.tv04.text = if (!TextUtils.isEmpty(data.curNum))data.curNum else "--"

    }

    class STTRViewHodler(v:View):RecyclerView.ViewHolder(v){
        val tv01= v.findViewById<TextView>(R.id.tv_stteam_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_stteam_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_stteam_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_stteam_04)
    }
}