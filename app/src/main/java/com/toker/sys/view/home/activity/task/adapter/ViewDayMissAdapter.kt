package com.toker.sys.view.home.activity.task.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.activity.task.bean.Data
import com.toker.sys.view.home.activity.task.bean.ViewDayMissBean
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ViewDayMissAdapter (val mContext:Context,var  mBeans: MutableList<ViewDayMissBean.Data> ):RecyclerView.Adapter<ViewDayMissAdapter.VDViewHodler>(){


    fun refreshData(mBeans: MutableList<ViewDayMissBean.Data>){
        this.mBeans = mBeans
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VDViewHodler {

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_view_day,null)

        return VDViewHodler(view)
    }

    override fun getItemCount(): Int {
       return mBeans.size
    }

    override fun onBindViewHolder(p0: VDViewHodler, p1: Int) {
        val data = mBeans[p1]
        p0.tv.text = data.objectName
        p0.rvList.layoutManager = GridLayoutManager(mContext,1)
        p0.rvList.adapter = ViewDataItemAdapter(mContext,data.tbTaskTargetObjectRefs)


    }


    class VDViewHodler(v:View):RecyclerView.ViewHolder(v){
        val tv = v.findViewById<TextView>(R.id.tv_list_view_day)
        val rvList = v.findViewById<RecyclerView>(R.id.rv_list_view_day)

    }

    class ViewDataItemAdapter(
        val mContext: Context,
        val mBean: MutableList<ViewDayMissBean.TbTaskTargetObjectRef>
    ) :RecyclerView.Adapter<ViewDataItemAdapter.VDIViewHodler>(){
        private val sdf = SimpleDateFormat("yyyy/MM/dd")
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VDIViewHodler {

            val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_vdi,null)
            return VDIViewHodler(view)
        }

        override fun getItemCount(): Int {
            return mBean.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(p0: VDIViewHodler, p1: Int) {

            /*taskId	任务ID	String
            objectId	对象Id	String	项目id(对应的管理员), 小组id，个人id
            objectName	对象名称	String
            isApprove	审批状态	String	（1：已审批；0：未审批）
            phoneNum  	目标留电数	int
            visitNum  	目标到访数	int
            dealNum  	目标成交量	int
            turnover  	目标成交额	int
            curPhoneNum  	留电数	int
            curVisitNum  	到访数	int
            curDealNum  	成交数	int
            curTurnover  	成交额	int
            tableTag	分表字段	String*/

            val ref = mBean[p1]
            val curTurnover = DecimalFormat("0.00").format(ref.curTurnover.toFloat() / 10000)
            p0.tv01.text = sdf.format(Date(ref.date))
            p0.tv02.text = "${ref.curPhoneNum}/${ref.phoneNum}"
            p0.tv03.text = "${ref.curVisitNum}/${ref.visitNum}"
            p0.tv04.text = "${ref.curDealNum}/${ref.dealNum}"
            p0.tv05.text = "$curTurnover/${ref.turnover}"
        }

        class VDIViewHodler(v:View):RecyclerView.ViewHolder(v){
            val tv01= v.findViewById<TextView>(R.id.tv_item_vdi_01)
            val tv02= v.findViewById<TextView>(R.id.tv_item_vdi_02)
            val tv03= v.findViewById<TextView>(R.id.tv_item_vdi_03)
            val tv04= v.findViewById<TextView>(R.id.tv_item_vdi_04)
            val tv05= v.findViewById<TextView>(R.id.tv_item_vdi_05)
        }
    }
}