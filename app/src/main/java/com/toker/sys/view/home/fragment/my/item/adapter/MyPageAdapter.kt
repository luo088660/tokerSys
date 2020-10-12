package com.toker.sys.view.home.fragment.my.item.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean
import org.greenrobot.eventbus.EventBus

class MyPageAdapter(
    val mContext: Context,
    var mBeans: MutableList<ProjectListBean.Data>,
    val isfo:Boolean
) : RecyclerView.Adapter<MyPageAdapter.MPViewHodler>() {

    fun refreshView(mBeans: MutableList<ProjectListBean.Data>) {
        this.mBeans = mBeans
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MPViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_my_page, null)
        return MPViewHodler(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: MPViewHodler, p1: Int) {
        val data = mBeans[p1]
        p0.name.text = data.projectName
        p0.img.visibility = if (isfo) View.VISIBLE else View.INVISIBLE
        p0.itemView.setOnClickListener {
            EventBus.getDefault().post(data)
        }
    }

    class MPViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val name = v.findViewById<TextView>(R.id.tv_page_name)
        val img = v.findViewById<ImageView>(R.id.img_page_name)
    }
}