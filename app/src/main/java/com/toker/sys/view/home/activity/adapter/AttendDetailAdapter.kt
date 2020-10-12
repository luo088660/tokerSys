package com.toker.sys.view.home.activity.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.utils.tools.DataUtil
import com.toker.sys.view.home.activity.my.bean.AttenDetailBean
import java.text.SimpleDateFormat
import java.util.*

class AttendDetailAdapter(
    val mContext: Context,
    var mBeans: MutableList<AttenDetailBean.TraceVO>
) : RecyclerView.Adapter<AttendDetailAdapter.ADeViewHodler>() {
    private val sdf = SimpleDateFormat("HH:mm")
    val formatType = "yyyy-MM-dd HH:mm:ss"
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ADeViewHodler {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.layout_admini_tran_detail_04, null)
        return ADeViewHodler(view)
    }

    //刷新
    fun refreshData(mBeans: MutableList<AttenDetailBean.TraceVO>) {
        this.mBeans = mBeans
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return mBeans.size
    }


    override fun onBindViewHolder(p0: ADeViewHodler, p1: Int) {
        val data = mBeans[p1]
        p0.tv01.text = sdf.format(Date(DataUtil.stringToLong(data.locateTime, formatType)))
        p0.tv02.text = data.locateAddress
        p0.img.text = "${mBeans.size - p1}"
        if (!TextUtils.isEmpty(data.locateAddress)) {

            p0.tv01.setTextColor(mContext.getColor(R.color.c_black_6))
            p0.tv02.setTextColor(mContext.getColor(R.color.c_black_6))
            p0.img.background =
                mContext.resources.getDrawable(R.mipmap.icon_orange)
//                mContext.resources.getDrawable(if (data.status.toInt() == 1) R.mipmap.icon_orange else R.mipmap.icon_red)
        } else {
            p0.tv02.text = "未定位到地址"
            p0.tv01.setTextColor(mContext.getColor(R.color.btn_red))
            p0.tv02.setTextColor(mContext.getColor(R.color.btn_red))
            p0.img.background =
                mContext.resources.getDrawable(R.mipmap.icon_red)
        }
    }
    class ADeViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_item_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_item_02)
        val img = v.findViewById<TextView>(R.id.img_item)

    }
}