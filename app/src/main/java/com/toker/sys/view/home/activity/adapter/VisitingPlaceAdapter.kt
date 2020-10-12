package com.toker.sys.view.home.activity.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.activity.custom.bean.VisiPlaceBean
import org.greenrobot.eventbus.EventBus

class VisitingPlaceAdapter(val mContext:Context,var mBeans:MutableList<VisiPlaceBean.Data>):RecyclerView.Adapter<VisitingPlaceAdapter.VPViewHolder>() {
    val map = mutableMapOf<Int,Boolean>()

    fun refeshData(mBeans: MutableList<VisiPlaceBean.Data>){
        this.mBeans = mBeans
        for (i in 0..mBeans.size){
            map[i] = false
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VPViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_visit_area_layout, null)
        return VPViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: VPViewHolder, p1: Int) {
        val classify = mBeans[p1]
        p0.title.text = classify.arriveArea
        val b = map[p1]!!
        p0.title.setTextColor( mContext.resources.getColor(if (b) R.color.btn_red else R.color.c_black_6 ))
        p0.img.visibility = if (b)View.VISIBLE else View.GONE

        p0.title.setOnClickListener {
            for (i in 0..mBeans.size){
                map[i] = false
            }
            map[p1] = true
            notifyDataSetChanged()
            EventBus.getDefault().post(classify)
        }
    }


    class VPViewHolder(v:View):RecyclerView.ViewHolder(v){
        val title = v.findViewById(R.id.title_visit) as TextView
        val img = v.findViewById(R.id.img_visit) as ImageView
    }
}