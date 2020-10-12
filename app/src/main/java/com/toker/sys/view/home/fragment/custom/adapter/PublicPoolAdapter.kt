package com.toker.sys.view.home.fragment.custom.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.mvp.BaseAdapter
import com.toker.sys.view.home.fragment.custom.bean.PublicPoolBean
import com.toker.sys.view.home.fragment.task.bean.PageData

class PublicPoolAdapter(
    val context: Context,
    var mBeans: MutableList<PublicPoolBean.PageData>
) : BaseAdapter<PublicPoolBean.PageData, PublicPoolAdapter.PPlViewHodlr>(context, mBeans) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PPlViewHodlr {
        if (mHeaderView != null && p1 == TYPE_HEADER) {
            return PPlViewHodlr(mHeaderView!!)
        }
        val view = LayoutInflater.from(context).inflate(R.layout.layout_public_pool_02, null)

        return PPlViewHodlr(view)
    }

    override fun onBindViewHolder(p0: PPlViewHodlr, p1: Int) {
        if (getItemViewType(p1) == TYPE_HEADER) {
            return
        }
        val position = getRealPosition(p0)
        val data = mData[position]
        p0.tv01.text = "${position+1}"
        p0.tv02.text = "${data.projectName}"
        p0.tv03.text = "${data.countNum}"
    }


    inner class PPlViewHodlr(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_pool_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_pool_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_pool_03)
    }

}