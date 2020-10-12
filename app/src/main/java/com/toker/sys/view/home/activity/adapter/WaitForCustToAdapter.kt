package com.toker.sys.view.home.activity.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.bean.ProjectToBean
import org.greenrobot.eventbus.EventBus

class WaitForCustToAdapter(
    val mContext: Context,
    var mBeans: MutableList<ProjectToBean.Record>
) : RecyclerView.Adapter<WaitForCustToAdapter.WFCTViewholder>() {

    fun refeshData(mBeans: MutableList<ProjectToBean.Record>) {
        this.mBeans = mBeans
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): WFCTViewholder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_wait_for_t, null)
        return WFCTViewholder(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size

    }

    override fun onBindViewHolder(p0: WFCTViewholder, p1: Int) {
        val record = mBeans[p1]
        p0.tv01.text = "${p1 + 1}"
        p0.tv02.text = record.projectName
        p0.tv03.text = "${if (record.overdueNum > 0) record.overdueNum else "0"}"
        p0.tv04.text = "${if (record.toPoolNum > 0) record.toPoolNum else "0"}"
        p0.itemView.setOnClickListener {
            EventBus.getDefault().post(record)
        }

    }


    class WFCTViewholder(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_wai_for_t_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_wai_for_t_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_wai_for_t_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_wai_for_t_04)

    }
}