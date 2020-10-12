package com.toker.sys.view.home.fragment.custom.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.R
import com.toker.sys.mvp.BaseAdapter
import com.toker.sys.view.home.fragment.task.bean.PageData

class InvalidAdapter (
    val context: Context,
    var mBeans: MutableList<PageData>
) : BaseAdapter<PageData, InvalidAdapter.InViewHodlr>(context, mBeans) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): InViewHodlr {
        if (mHeaderView != null && p1 == TYPE_HEADER) {
            return InViewHodlr(mHeaderView!!)
        }
        val view = LayoutInflater.from(context).inflate(R.layout.layout_custo_detail, null)

        return InViewHodlr(view)
    }

    override fun onBindViewHolder(p0: InViewHodlr, p1: Int) {
        if (getItemViewType(p1) == TYPE_HEADER) {
            return
        }
        val position = getRealPosition(p0)
        val data = mData[position]
    }


    inner class InViewHodlr(v: View) : RecyclerView.ViewHolder(v) {

    }

}