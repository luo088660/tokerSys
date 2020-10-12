package com.toker.sys.view.home.fragment.my.item.adapter

import android.content.Context
import android.opengl.Visibility
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.fragment.my.item.bean.UnreadNewsBean
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat

class UnreadNewsAdapter(
    val mContext: Context,
    var mBeans: MutableList<UnreadNewsBean.Records>,
    val type: Int
) : RecyclerView.Adapter<UnreadNewsAdapter.UNViewHolder>() {


    val sdf = SimpleDateFormat("yyyy/MM/dd")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UNViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_read_news, null)
        return UNViewHolder(view)

    }

    fun refreshData(mBeans: MutableList<UnreadNewsBean.Records>) {
        this.mBeans = mBeans
        LogUtils.d("UnreadNewsAdapter", "mBeans:$mBeans ");
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mBeans.size

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(p0: UNViewHolder, p1: Int) {
        val data = mBeans[p1]
        if (data.id.isEmpty()&&data.sendId.isEmpty()){
            p0.tv01.text = data.content//sdf.format(data.sendTime)
            p0.ll01.visibility = View.GONE
        }else{
            p0.tv01.visibility = View.GONE
            p0.tv02.text = if(!TextUtils.isEmpty(data.title))data.title else data.content
            p0.tv03.visibility = if (type == 1) View.VISIBLE else View.GONE
        }

        p0.tv05.visibility =  if (data.msgType == "7"||data.msgType == "4") View.VISIBLE else View.GONE

        if (data.msgType == "7"){
            p0.tv04.text ="${data.content},请留意哦~"
            p0.tv04.isEnabled = false
        }else{
            p0.tv04.text = "点击查看明细 >"
            p0.tv04.isEnabled = true
            p0.tv04.setTextColor(mContext.resources.getColor(R.color.c_blue_5,null))
        }

        //标记已读
        p0.tv03.setOnClickListener {
            data.type = 0
            EventBus.getDefault().post(data)
        }
        //点击查看详情
        p0.tv04.setOnClickListener {
            data.type = 1
            EventBus.getDefault().post(data)
        }
        //工作轨迹异常
        p0.tv05.setOnClickListener {
            data.type = 2
            EventBus.getDefault().post(data)
        }
    }

    class UNViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_read_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_read_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_read_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_read_04)
        val tv05 = v.findViewById<TextView>(R.id.tv_read_05)
        val ll01 = v.findViewById<LinearLayout>(R.id.ll_read_01)
    }
}
