package com.toker.sys.view.home.fragment.my.item.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.toker.sys.R
import com.toker.sys.mvp.BaseAdapter
import com.toker.sys.view.home.fragment.my.item.bean.ExtensionBean

class ExtensionAdapter(
    val mContext: Context,
    var mBeans: MutableList<ExtensionBean.PageData>
) : BaseAdapter<ExtensionBean.PageData,ExtensionAdapter.EXViewHodler>(mContext,mBeans) {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EXViewHodler {
        if (mHeaderView != null && p1 == TYPE_HEADER) {
            return EXViewHodler(mHeaderView!!)
        }
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_exten, null)
        return EXViewHodler(view)
    }

    override fun onBindViewHolder(p0: EXViewHodler, p1: Int) {
        if (getItemViewType(p1) == TYPE_HEADER) {
            return
        }
        val position = getRealPosition(p0)
        val data = mData[position]
        p0.tv01.text = data.userName
        p0.tv02.text = data.userPhone
        p0.tv03.text = when (data.position.toInt()) {
            3 -> "拓客组长"
            else -> "拓客员"
        }
        p0.tv04.text = data.groupName
        p0.tv04.setOnClickListener {
            Toast.makeText(mContext,data.groupName,Toast.LENGTH_LONG).show()
        }

    }

    class EXViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_item_exten_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_item_exten_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_item_exten_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_item_exten_04)

    }
}