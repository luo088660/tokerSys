package com.toker.sys.view.home.activity.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.activity.custom.fcustomdetail.CustomerStateChange
import java.text.SimpleDateFormat
import java.util.*

class FCustomDetaYAdapter(
    val mContext: Context,
    val mBeans: List<CustomerStateChange>
) : RecyclerView.Adapter<FCustomDetaYAdapter.FCDViewYHodler>() {

    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FCDViewYHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_fcdetail, null)
        return FCDViewYHodler(view)
    }

    override fun onBindViewHolder(p0: FCDViewYHodler, p1: Int) {

        val record = mBeans[p1]
        p0.tv01.text = sdf.format(Date(record.changeTime))
        p0.tv03.text = record.userName
        p0.tv02.text = when (record.status.toInt()) {
            1 -> {
                p0.tv02.setTextColor(mContext.resources.getColor(R.color.c_txt_tjchenggong))
                "推荐成功"
            }
            2 -> {
                p0.tv02.setTextColor(mContext.resources.getColor(R.color.c_txt_yidaofang))
                "已到访"
            }
            3 -> {
                p0.tv02.setTextColor(mContext.resources.getColor(R.color.c_txt_yirenchou))
                "已认筹"
            }
            4 -> {
                p0.tv02.setTextColor(mContext.resources.getColor(R.color.c_txt_yirengou))
                "已认购"
            }
            5 -> {
                p0.tv02.setTextColor(mContext.resources.getColor(R.color.c_txt_yiqianyue))
                "已签约"
            }
            else -> {
                p0.tv02.setTextColor(mContext.resources.getColor(R.color.c_txt_yiguoqi))
                "已过期"
            }
        }
        if (p1 == 0){
            p0.img01.setImageResource(R.drawable.spot_03_select)
        }
        if (p1 == mBeans.size-1) {
            p0.img02.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }


    inner class FCDViewYHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_fcdetail_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_fcdetail_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_fcdetail_03)
        val img01 = v.findViewById<ImageView>(R.id.img_item_01)
        val img02 = v.findViewById<ImageView>(R.id.img_item_02)

    }
}