package com.toker.sys.view.home.fragment.custom.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.mvp.BaseAdapter
import com.toker.sys.utils.tools.TimeUtils
import com.toker.sys.view.home.fragment.custom.bean.FollowCustBean
import com.toker.sys.view.home.fragment.custom.bean.Record
import com.toker.sys.view.home.fragment.custom.event.MyCusEvent
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class FollowCustAdapter(
    val context: Context,
    var mBeans: MutableList<FollowCustBean.PageData>
) : BaseAdapter<FollowCustBean.PageData, FollowCustAdapter.FCViewHodlr>(context, mBeans) {
    private val sdf1 = SimpleDateFormat("yyyy-MM-dd")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FCViewHodlr {


        if (mHeaderView != null && p1 == TYPE_HEADER) {
            return FCViewHodlr(mHeaderView!!)
        }
        val view = LayoutInflater.from(context).inflate(R.layout.layout_custo_detail, null)

        return FCViewHodlr(view)
    }

    override fun onBindViewHolder(p0: FCViewHodlr, p1: Int) {
        if (getItemViewType(p1) == TYPE_HEADER) {
            return
        }
        val position = getRealPosition(p0)
        val data = mData[position]
        p0.tv01.text = data.name
        p0.tv02.text = data.phone
//        val day = TimeUtils.getDaySub(data.createTime).toString()

        p0.tv03.text = Html.fromHtml(String.format(context.getString(R.string.tip_task_day), data.notFollowDays))
        p0.img.visibility = if (data.beeFlag == "0") View.VISIBLE else View.GONE
        p0.tv05.text = sdf1.format(Date(data.createTime))
        p0.tv06.text = data.userName
//        data.status = "2"
        p0.tv07.setTextColor(context.resources.getColor(if (data.status != "1") R.color.c_txt_tjchenggong else R.color.c_red_4))
        p0.tv07.setBackgroundColor(context.resources.getColor(if (data.status != "1") R.color.c_bg_tjchenggong else R.color.c_red_5))
        p0.tv07.text = if (data.status == "1") "未推荐" else "已推荐"
        p0.tv08.visibility = if (data.status != "1") View.VISIBLE else View.GONE

        p0.tv04.setOnClickListener {
            //跟进记录
            EventBus.getDefault().post(MyCusEvent( data.status.toInt(),data))
        }
    }

    inner class FCViewHodlr(v: View) : RecyclerView.ViewHolder(v) {
        val img = v.findViewById<ImageView>(R.id.img_custo_detail)
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