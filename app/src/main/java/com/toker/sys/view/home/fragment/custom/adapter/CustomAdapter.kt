package com.toker.sys.view.home.fragment.custom.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.fragment.event.CustomEvent
import org.greenrobot.eventbus.EventBus
import android.widget.RelativeLayout


/**
 * 客户Adapter
 */
class CustomAdapter(private val mContext: Context, private val mBeans: Array<String>) :
    RecyclerView.Adapter<CustomAdapter.CTViewHodler>() {
    private var isClicks: MutableList<Boolean> = mutableListOf()

    init {
        for (i in mBeans.indices) {
            isClicks.add(false)
        }
        isClicks[0] = true
    }
    //更新数据
    fun upDataView(position: Int) {
        for (i in mBeans.indices) {
            isClicks[i] = false
        }
        isClicks[position] = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CTViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_custom_layout, null)
        return CTViewHodler(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(hodler: CTViewHodler, position: Int) {
        hodler.tvTitle.text = mBeans[position]
        hodler.viewHor.background = mContext.resources.getDrawable(R.color.white_01, null)
        hodler.tvTitle.setTextColor(mContext.resources.getColor(R.color.c_gray_9, null))
        hodler.itemView.setOnClickListener {
            for (isClick in isClicks.indices) {
                isClicks[isClick] = false
            }
            isClicks[position] = true
            notifyDataSetChanged()
            EventBus.getDefault().post(CustomEvent(mBeans[position]))
        }

        if (isClicks[position]) {
            hodler.tvTitle.setTextColor(mContext.resources.getColor(R.color.btn_red, null))
            hodler.viewHor.background = mContext.resources.getDrawable(R.color.btn_red, null)
        } else {
            hodler.tvTitle.setTextColor(mContext.resources.getColor(R.color.c_gray_9, null))
            hodler.viewHor.background = mContext.resources.getDrawable(R.color.white_01, null)
        }

    }


    inner class CTViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_item_title)
        val viewHor = itemView.findViewById<View>(R.id.view_hor)
    }
}