package com.toker.sys.view.home.activity.task.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.toker.sys.R
import com.toker.sys.view.home.activity.task.bean.MonthLBean
import com.toker.sys.view.home.activity.task.bean.TaskTarget

class TaskObjectAdapter(
    val mContext: Context,
    val mBeans: MutableList<MonthLBean.TaskTarget>
) : RecyclerView.Adapter<TaskObjectAdapter.TOViewHodler>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TOViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_task_decom, null)
        return TOViewHodler(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: TOViewHodler, p1: Int) {
        val bean = mBeans[p1]
        p0.tv00.text = "${bean.updator}"
        p0.tv01.text = "${bean.curPhoneNum}/${bean.phoneNum}"
        p0.tv02.text = "${bean.curVisitNum}/${bean.visitNum}"
        p0.tv03.text = "${bean.curTurnover}/${bean.turnover}"
        p0.tv04.text = "${bean.curDealNum}/${bean.dealNum}"

        p0.tv00.setOnClickListener {
            Toast.makeText(mContext,bean.updator, Toast.LENGTH_SHORT).show()
        }

    }

    class TOViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tv00 = v.findViewById<TextView>(R.id.tv_item_indic3_)
        val tv01 = v.findViewById<TextView>(R.id.tv_item_indic3_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_item_indic3_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_item_indic3_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_item_indic3_04)
    }
}