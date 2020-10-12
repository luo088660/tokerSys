package com.toker.sys.view.home.activity.task.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.fragment.my.item.bean.LittleBeeBean
import java.text.SimpleDateFormat

class LittleBeEAdapter(
    val context: Context,
    var mBeans: MutableList<LittleBeeBean.PageData>
) :RecyclerView.Adapter<LittleBeEAdapter.LBeViewHolder>(){

    private val sdf = SimpleDateFormat("yyyy/MM/dd")
    fun refreshData(mBeans: MutableList<LittleBeeBean.PageData>) {
        this.mBeans=mBeans
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): LBeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_little_bee_02,null)
        return LBeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: LBeViewHolder, p1: Int) {
        val data = mBeans[p1]
        p0.tv01.text = data.beeName
        p0.tv02.text = data.beePhone
        p0.tv03.text = sdf.format(data.createTime)

    }


    class LBeViewHolder(v:View):RecyclerView.ViewHolder(v){
        val tv01 = v.findViewById<TextView>(R.id.tv_lit_be_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_lit_be_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_lit_be_03)

    }
}