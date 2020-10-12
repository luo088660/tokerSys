package com.toker.sys.view.home.fragment.sheet.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.bean.STExtenRankBean

class CurrentTaskAdapter(
    val mContext: Context,
    var mBeans: MutableList<STExtenRankBean.Record>,
    var item: String
) : RecyclerView.Adapter<CurrentTaskAdapter.CTViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CTViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_current_task, null)
        return CTViewHolder(view)
    }

    fun refreshData(mBeans: MutableList<STExtenRankBean.Record>, item: String) {
        this.mBeans = mBeans
        this.item = item
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mBeans.size

    }
    /*  你选了成交额，curNum就是成交额

      你选了留电数curNum就是留电数
      targetNum就是目标
      percent就是完成率*/

    override fun onBindViewHolder(p0: CTViewHolder, p1: Int) {
//        留电数:1,到访数:2,成交量:3,成交额:4"
        val data = mBeans[p1]
        p0.tv01.text = "${p1 + 1}"
        p0.tv02.text = if (!TextUtils.isEmpty(data.userName)) data.userName else "--"
        p0.tv03.text = when (item.toInt()) {
            1 -> if (!TextUtils.isEmpty(data.curNum)) data.curNum else "--"
            2 -> if (!TextUtils.isEmpty(data.curNum)) data.curNum else "--"
            3 -> if (!TextUtils.isEmpty(data.curNum)) data.curNum else "--"
            else -> if (!TextUtils.isEmpty(data.curNum)) data.curNum else "--"
        }

    }

    class CTViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_item_current_tsk_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_item_current_tsk_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_item_current_tsk_03)

    }
}