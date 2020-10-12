package com.toker.sys.view.home.activity.task.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.utils.tools.TimeUtils
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import com.toker.sys.view.home.activity.task.bean.ExtenRecordBean
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import com.toker.sys.view.home.fragment.custom.event.MyCusEvent
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class ExtenRecordAdapter(
    val mContext: Context,
    var mBeans: MutableList<ExtenRecordBean.PageData>
) :RecyclerView.Adapter<ExtenRecordAdapter.ERViewHolder>(){

    fun refreshData(mBeans: MutableList<ExtenRecordBean.PageData>) {
        this.mBeans = mBeans
        notifyDataSetChanged()
    }
    private val sdf = SimpleDateFormat("yyyy/MM/dd")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ERViewHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_custo_detail,null)
        return ERViewHolder(view)
    }

    override fun getItemCount(): Int {

        return mBeans.size
    }

    override fun onBindViewHolder(p0: ERViewHolder, p1: Int) {
        val data = mBeans[p1]
        p0.tv01.text = data.name
        p0.tv02.text = data.phone
        val day = TimeUtils.getDaySub(data.createTime).toString()

        p0.tv03.text = Html.fromHtml(String.format(mContext.getString(R.string.tip_task_day), day))

        p0.tv05.text = sdf.format(Date(data.createTime))
        p0.tv06.text = data.userName
//        data.status = "2"
        p0.tv07.setTextColor(mContext.resources.getColor(if (data.status != "1") R.color.c_txt_tjchenggong else R.color.c_red_4))
        p0.tv07.setBackgroundColor(mContext.resources.getColor(if (data.status != "1") R.color.c_bg_tjchenggong else R.color.c_red_5))
        p0.tv07.text = if (data.status == "1") "未推荐" else "已推荐"
        p0.tv08.visibility = if (data.status != "1") View.VISIBLE else View.GONE
        p0.tv04.setOnClickListener {

            EventBus.getDefault().post(  MyCusEvent(
                1,
                FollowCTBean.PageData(data.phone, data.id, data.tableTag)
            ))
        }
    }


    class ERViewHolder(v: View):RecyclerView.ViewHolder(v){
        val tv01 = v.findViewById<TextView>(R.id.tv_custo_detail_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_custo_detail_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_custo_detail_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_custo_detail_04)
        val tv05 = v.findViewById<TextView>(R.id.tv_custo_detail_05)
        val tv06 = v.findViewById<TextView>(R.id.tv_custo_detail_06)
        val tv07 = v.findViewById<TextView>(R.id.tv_custo_detail_07)
        val tv08 = v.findViewById<TextView>(R.id.tv_custo_detail_08)
    }
}