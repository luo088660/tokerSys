package com.toker.sys.view.home.fragment.task.adapter

import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.toker.sys.R
import com.toker.sys.utils.view.CircleImageView
import com.toker.sys.view.home.fragment.task.bean.TaskTrajectoryBean
import org.greenrobot.eventbus.EventBus

class TaskTrajectoryAdapter(
    val context: FragmentActivity,
    var mBeans: MutableList<TaskTrajectoryBean.Trace>
) : RecyclerView.Adapter<TaskTrajectoryAdapter.TTViewHodler>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TTViewHodler {

        val view = LayoutInflater.from(context).inflate(R.layout.item_layout_task_trajectory, null)
        return TTViewHodler(view)
    }

    //刷新数据
    fun refreshView(mBeans: MutableList<TaskTrajectoryBean.Trace>) {
        this.mBeans = mBeans
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: TTViewHodler, p1: Int) {
        val trace = mBeans[p1]
        if (!TextUtils.isEmpty(trace.pic)){
            Glide.with(context).load(trace.pic)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(25)))//圆角半径
                .into(p0.img)
        }

        p0.tv01.text = trace.userName
        p0.tv02.text = if (trace.status == "0") {
            if (TextUtils.isEmpty(trace.remark)) "轨迹异常" else trace.remark
        } else "轨迹正常"
//        p0.tv02.text = if (TextUtils.isEmpty(trace.remark)) "轨迹正常" else trace.remark
        p0.itemView.setOnClickListener {
            EventBus.getDefault().post(trace)
        }

    }


    class TTViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val img = v.findViewById<CircleImageView>(R.id.img_miss_item)
        val tv01 = v.findViewById<TextView>(R.id.tv_miss_item_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_miss_item_02)

    }
}