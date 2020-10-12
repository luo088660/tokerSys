package com.toker.sys.view.home.activity.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
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
import com.toker.sys.view.home.activity.my.bean.ViewAttenListBean1
import org.greenrobot.eventbus.EventBus

class AttendAdapter(val mContext: Context, var mBeans: MutableList<ViewAttenListBean1.Data>) :
    RecyclerView.Adapter<AttendAdapter.AAViewHodler>() {


    //刷新数据
    fun refreshData(mBeans: MutableList<ViewAttenListBean1.Data>) {
        this.mBeans = mBeans
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AAViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_attent, null)
        return AAViewHodler(view)
    }

    override fun getItemCount(): Int {

        return mBeans.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(p0: AAViewHodler, p1: Int) {
        val data = mBeans[p1]
        p0.tv01.text = data.userName
        p0.tv02.text = data.checkInRemark
        p0.tv03.text = data.checkOutRemark

        p0.tv02.setTextColor(mContext.getColor(if (data.checkInStatus == "1") R.color.c_black_6 else R.color.btn_red))
        p0.tv03.setTextColor(mContext.getColor(if (data.checkOutStatus == "1") R.color.c_black_6 else R.color.btn_red))

        p0.ll.setOnClickListener {
            LogUtils.d("AttendAdapter", "setOnClickListener:setOnClickListener ");
            EventBus.getDefault().post(ViewAttenListBean.BeanEvent(data.userName,data.date, data.userId))
        }
    }

    class AAViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_layout_atten_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_layout_atten_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_layout_atten_03)
        val ll = v.findViewById<LinearLayout>(R.id.ll_layout_atten_)
    }
}