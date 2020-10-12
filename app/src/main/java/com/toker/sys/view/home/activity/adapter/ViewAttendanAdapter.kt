package com.toker.sys.view.home.activity.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.my.bean.ViewAttenListBean
import com.toker.sys.view.home.activity.my.bean.ViewAttenTeamBean
import org.greenrobot.eventbus.EventBus

class ViewAttendanAdapter(val mContext: Context, var mBeans: MutableList<ViewAttenTeamBean.PageData>) :
    RecyclerView.Adapter<ViewAttendanAdapter.VAViewHodler>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VAViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_view_attendan, null)
        return VAViewHodler(view)
    }

    fun refreshData(mBeans: MutableList<ViewAttenTeamBean.PageData>) {
        this.mBeans = mBeans
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: VAViewHodler, p1: Int) {
        val data = mBeans[p1]
        p0.tv01.text = data.userName
        p0.tv02.text = "${data.checkInNum}"
        p0.tv03.text = "${data.checkOutNum}"
        p0.tv04.text = "${data.regularNum}"
        p0.tv05.text = "${data.exceptionNum}"


        p0.ll.setOnClickListener {
            LogUtils.d("AttendAdapter", "setOnClickListener:setOnClickListener ");
            EventBus.getDefault().post(ViewAttenListBean.BeanEvent(data.userName ,"${data.year}-${data.month}-01", data.userId))
        }
    }


    class VAViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_item_view_atten_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_item_view_atten_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_item_view_atten_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_item_view_atten_04)
        val tv05 = v.findViewById<TextView>(R.id.tv_item_view_atten_05)
        val ll = v.findViewById<LinearLayout>(R.id.ll_item_view_atten_)

    }
}