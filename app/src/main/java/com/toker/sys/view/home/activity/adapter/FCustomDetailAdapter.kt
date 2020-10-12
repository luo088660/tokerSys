package com.toker.sys.view.home.activity.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import com.toker.sys.view.home.activity.custom.bean.TbCustomerRecord
import java.text.SimpleDateFormat
import java.util.*

class FCustomDetailAdapter(
    val mContext: Context,
    val mBeans: MutableList<FCustomDetailTBean.CustomerRecordsVO>
) : RecyclerView.Adapter<FCustomDetailAdapter.FCDViewHodler>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FCDViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_fcdetail,null)
        return FCDViewHodler(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: FCDViewHodler, p1: Int) {
        val record = mBeans[p1]
        if (p1 == 0){
            p0.img01.setImageResource(R.drawable.spot_04_select)
        }
        if (p1 == mBeans.size-1) {
            p0.img02.visibility = View.GONE
        }
        p0.tv01.text =  record.createTime
        p0.tv02.text = record.record
        p0.tv03.text = record.tkName

    }

    inner class FCDViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_fcdetail_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_fcdetail_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_fcdetail_03)
        val img01 = v.findViewById<ImageView>(R.id.img_item_01)
        val img02 = v.findViewById<ImageView>(R.id.img_item_02)
    }
}