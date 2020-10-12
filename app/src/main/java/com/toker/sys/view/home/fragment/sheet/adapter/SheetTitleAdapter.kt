package com.toker.sys.view.home.fragment.sheet.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.home.fragment.sheet.param.SheentTopBean
import org.greenrobot.eventbus.EventBus

class SheetTitleAdapter(private val mContext: Context, var mBeans: ArrayList<SheentTopBean>) :
    RecyclerView.Adapter<SheetTitleAdapter.STtViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): STtViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_sheent_title_layout, null)

        return STtViewHolder(view)
    }

    fun refreshData(mBeans: ArrayList<SheentTopBean>){
        this.mBeans = mBeans
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: STtViewHolder, p1: Int) {
        val topBean = mBeans[p1]
        p0.tvItme2.text = topBean.name

//       tip_fol_up_cus 跟进中的客户
//       task_await_appr 待审批任务
//       custom_foll_up 待跟进客户
//       task_foll_up 待跟进任务
//       custom_be_assig 待分配客户
//       group_foll_task 小组待跟进任务

        p0.tvItme1.text = "${topBean.resImg}"
        p0.itemView.setOnClickListener {
            when (topBean.name) {
                //待审批任务
                mContext.resources.getString(R.string.task_await_appr)
                -> EventBus.getDefault().post(MainEvent(9))
                //待跟进客户
                //逾期未跟进客户
                mContext.resources.getString(R.string.custom_foll_up1),
                mContext.resources.getString(R.string.custom_foll_up)
                -> EventBus.getDefault().post(MainEvent(10))

                //待分配客户
                mContext.resources.getString(R.string.custom_be_assig)
                -> EventBus.getDefault().post(MainEvent(11))

                //待跟进任务
                mContext.resources.getString(R.string.task_foll_up)
                -> EventBus.getDefault().post(MainEvent(12))
                //小组待跟进任务
                mContext.resources.getString(R.string.group_foll_task)
                -> EventBus.getDefault().post(MainEvent(8))
                else -> {
                }
            }
        }
    }


    inner class STtViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvItme1 = v.findViewById<TextView>(R.id.tv_sheent_tt_item1)
        val tvItme2 = v.findViewById<TextView>(R.id.tv_sheent_tt_item2)

    }
}