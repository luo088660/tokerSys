package com.toker.sys.dialog.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.activity.custom.bean.CityBean
import com.toker.sys.view.home.activity.custom.bean.ProjectOnCityBean

class ProjectCityAdapter (val mContext:Context, val mBeans:MutableList<ProjectOnCityBean.Data>):RecyclerView.Adapter<ProjectCityAdapter.NPViewHolder>(){


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NPViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_project_list, null)
        return NPViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: NPViewHolder, p1: Int) {
        val data = mBeans[p1]
        p0.tv.text = data.projectName
        p0.itemView.setOnClickListener {
            listener!!.itemListener( mBeans[p1])
        }
    }

    class NPViewHolder(v:View):RecyclerView.ViewHolder(v){

        val tv = v.findViewById<TextView>(R.id.tv_list)
    }

    private var listener: ProCityListener? = null

    fun setListener(listener: ProCityListener) {
        this.listener = listener
    }

    public interface ProCityListener {
        fun itemListener(bean:ProjectOnCityBean.Data)
    }
}