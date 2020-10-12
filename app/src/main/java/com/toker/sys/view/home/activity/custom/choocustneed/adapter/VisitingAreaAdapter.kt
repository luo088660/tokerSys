package com.toker.sys.view.home.activity.custom.choocustneed.adapter

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.EventLog
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.activity.custom.bean.ArriveAreaBean
import com.toker.sys.view.home.activity.custom.choocustneed.visitingarea.bean.VisitingAreaBean
import org.greenrobot.eventbus.EventBus

class VisitingAreaAdapter(
    val mContext: Context,
    var mBeans: MutableList<ArriveAreaBean.Data>
) :
    RecyclerView.Adapter<VisitingAreaAdapter.VAViewHodler>() {

    val map = mutableMapOf<Int,Boolean>()

    fun refeshData(mBeans: MutableList<ArriveAreaBean.Data>){
        this.mBeans = mBeans
        for (i in 0..mBeans.size){
            map[i] = false
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VAViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_visit_area_layout, null)
        return VAViewHodler(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: VAViewHodler, p1: Int) {
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


    inner class VAViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val title = v.findViewById(R.id.title_visit) as TextView
        val img = v.findViewById(R.id.img_visit) as ImageView
    }
}