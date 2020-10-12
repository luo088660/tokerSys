package com.toker.sys.dialog.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.bean.Data

class ProjectListAdapter(val mContext: Context, var data: MutableList<Data>) :
    RecyclerView.Adapter<ProjectListAdapter.PLViewHodler>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PLViewHodler {

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_project_list, null)
        return PLViewHodler(view)
    }
    fun refreshData( data: MutableList<Data>){
        this.data = data
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        LogUtils.d("ProjectListAdapter", "data.size:${data.size} ");
        return data.size
    }

    override fun onBindViewHolder(p0: PLViewHodler, p1: Int) {

        val bean = data[p1]
        p0.tv.text = bean.projectName

        p0.itemView.setOnClickListener {
            listener!!.itemListener(bean)
        }

    }

    inner class PLViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tv = v.findViewById<TextView>(R.id.tv_list)

    }

    private var listener: ProjecListListener? = null

    fun setListener(listener: ProjecListListener) {
        this.listener = listener
    }

    public interface ProjecListListener {
        fun itemListener(bean: Data)
    }

}