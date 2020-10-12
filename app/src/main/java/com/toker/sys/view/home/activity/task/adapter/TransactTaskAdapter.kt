package com.toker.sys.view.home.activity.task.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.activity.task.bean.TransactABean
import com.toker.sys.view.home.activity.task.bean.TransactBean
import java.text.SimpleDateFormat
import java.util.*

class TransactTaskAdapter(
    val mContext: Context,
    var mBeans: MutableList<TransactABean.Data>
) : RecyclerView.Adapter<TransactTaskAdapter.TTkViewHodler>() {

    private val sdf = SimpleDateFormat("HH:mm")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TTkViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_admini_tran_detail_04, null)
        return TTkViewHodler(view)

    }

    //刷新
    fun refreshData( mBeans: MutableList<TransactABean.Data>){
        this.mBeans = mBeans
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: TTkViewHodler, p1: Int) {

        val data = mBeans[p1]

        p0.tv01.text = data.locateTime
        p0.tv02.text = if(!TextUtils.isEmpty(data.locateAddress))data.locateAddress else "未定位到地址"
        p0.tv02.setTextColor(if(!TextUtils.isEmpty(data.locateAddress)) mContext.resources.getColor(R.color.c_black_6) else mContext.resources.getColor(R.color.btn_red))
        p0.img.text = "${data.order}"
        p0.img.background = mContext.resources.getDrawable(if (data.status.toInt()==1)R.mipmap.icon_orange else R.mipmap.icon_red)


    }
    class TTkViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_item_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_item_02)
        val img = v.findViewById<TextView>(R.id.img_item)

    }
}