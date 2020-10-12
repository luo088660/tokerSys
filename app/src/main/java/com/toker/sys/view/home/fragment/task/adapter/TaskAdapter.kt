package com.toker.sys.view.home.fragment.task.adapter

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.fragment.event.TaskEvent
import org.greenrobot.eventbus.EventBus

class TaskAdapter(private val mContext: Context, private val mBeans: Array<String>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var isClicks: MutableList<Boolean> = mutableListOf()
    private var numBer: Int = 0

    init {
        for (i in mBeans.indices) {
            isClicks.add(false)
        }
        isClicks[0] = true
    }

    //更新
    fun upDataView(position: Int) {
        for (i in 0 until mBeans.size) {
            isClicks[i] = false
        }
        isClicks[position] = true
        notifyDataSetChanged()
    }

    fun upDataView(position: Int, numBer: Int) {
        for (i in mBeans.indices) {
            isClicks[i] = false
        }
        this.numBer = numBer
        isClicks[position] = true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TaskViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_custom_layout, null)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(hodler: TaskViewHolder, position: Int) {
        hodler.tvTitle.text = mBeans[position]
        hodler.viewHor.background = mContext.resources.getDrawable(R.color.white_01, null)
        hodler.tvTitle.setTextColor(mContext.resources.getColor(R.color.c_black_7, null))
        hodler.itemView.setOnClickListener {
            for (isClick in isClicks.indices) {
                isClicks[isClick] = false

            }
            isClicks[position] = true
            notifyDataSetChanged()
            EventBus.getDefault().post(TaskEvent(mBeans[position]))
        }

        if (isClicks[position]) {
            hodler.tvTitle.setTextColor(mContext.resources.getColor(R.color.c_red_4, null))
            hodler.viewHor.background = mContext.resources.getDrawable(R.color.c_red_4, null)
        } else {
            hodler.tvTitle.setTextColor(mContext.resources.getColor(R.color.c_black_7, null))
            hodler.viewHor.background = mContext.resources.getDrawable(R.color.white_01, null)
        }
        hodler.imgMsg.text = if (numBer > 99) "99+" else "$numBer"
        hodler.imgMsg.visibility =
            if (numBer != 0 && mBeans[position] == "未读") View.VISIBLE else View.GONE
    }


    inner class TaskViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_item_title)
        val viewHor = itemView.findViewById<View>(R.id.view_hor)
        val imgMsg = itemView.findViewById<TextView>(R.id.tv_my_ms_01)

    }
}