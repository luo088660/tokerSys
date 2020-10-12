package com.toker.sys.view.home.fragment.sheet.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.home.fragment.my.event.MyEvent
import com.toker.sys.view.home.fragment.sheet.event.SheetEvent
import com.toker.sys.view.home.fragment.sheet.param.SheentTopBean
import org.greenrobot.eventbus.EventBus

class SheentTopAdapter(
    private val mContext: Context,
    private var mBeans: ArrayList<SheentTopBean>
) : RecyclerView.Adapter<SheentTopAdapter.STViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): STViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_sheent_top_layout, null)
        return STViewHolder(view)
    }

    fun refreshData(mBeans: ArrayList<SheentTopBean>) {
        this.mBeans = mBeans
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(holder: STViewHolder, position: Int) {
        val bean = mBeans[position]
        holder.tvItem.text = bean.name
        holder.imgItem.setImageResource(bean.resImg)

        holder.tvMyms.visibility =
            if (bean.name == "我的任务" && bean.resNum > 0) View.VISIBLE else View.GONE
        holder.tvMyms.text = "${bean.resNum}"
        holder.imgItem.setOnClickListener {
            when (bean.name) {


                //我的考勤，团队考勤，排班计划-->我的项目
                mContext.resources.getString(R.string.tip_Team_attendance),
                mContext.resources.getString(R.string.tip_Myteam),
                mContext.resources.getString(R.string.tip_Schedule_Rule)
                    //拓客组长 团队考勤--> 团队考勤
                -> EventBus.getDefault().post(
                    if (AppApplication.TYPE == Constants.RESCUE4) SheetEvent(10) else SheetEvent(6, bean.name)
                )
                //客户地图
                mContext.resources.getString(R.string.tip_Customer_Map)
                -> EventBus.getDefault().post(MainEvent(4))
                //考勤打卡
                mContext.resources.getString(R.string.tip_Attendance_Card)
                -> EventBus.getDefault().post(MainEvent(5))
                //我的排班
                mContext.resources.getString(R.string.tip_My_Schedule)
                -> EventBus.getDefault().post(MainEvent(6))
                //我的任务
                mContext.resources.getString(R.string.tip_MyMission)
                -> EventBus.getDefault().post(MainEvent(8))
                else -> {
                }
            }

        }
    }

    inner class STViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvItem: TextView = v.findViewById(R.id.tv_sheent_item)
        val imgItem: ImageView = v.findViewById(R.id.img_sheent_item)
        val tvMyms: TextView = v.findViewById(R.id.tv_my_ms_01)
    }
}