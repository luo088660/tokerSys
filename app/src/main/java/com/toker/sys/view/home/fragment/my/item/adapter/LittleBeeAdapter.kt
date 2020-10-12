package com.toker.sys.view.home.fragment.my.item.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.mvp.BaseAdapter
import com.toker.sys.view.home.fragment.my.item.bean.LittleBeBean

class LittleBeeAdapter(
    val mContext: Context,
    var mBeans: MutableList<LittleBeBean.PageData>
) :BaseAdapter<LittleBeBean.PageData,LittleBeeAdapter.LBViewHodler>(mContext, mBeans){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): LBViewHodler {
        if (mHeaderView != null && p1 == TYPE_HEADER) {
            return LBViewHodler(mHeaderView!!)
        }

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_litte_bee,null)
        return LBViewHodler(view)
    }

    override fun onBindViewHolder(p0: LBViewHodler, p1: Int) {

        if (getItemViewType(p1) == TYPE_HEADER) {
            return
        }
        val position = getRealPosition(p0)
        val data = mData[position]
        p0.tv01.text = data.beeName
        p0.tv02.text = data.beePhone
        p0.tv03.text = data.userName


    }


    class LBViewHodler(v:View):RecyclerView.ViewHolder(v){
        val tv01 = v.findViewById<TextView>(R.id.tv_item_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_item_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_item_03)

    }
}