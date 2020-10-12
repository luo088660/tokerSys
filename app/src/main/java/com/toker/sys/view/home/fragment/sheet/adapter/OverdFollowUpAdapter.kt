package com.toker.sys.view.home.fragment.sheet.adapter

import android.content.Context
import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.util.EventLog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.mvp.BaseAdapter
import com.toker.sys.utils.tools.TimeUtils
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import com.toker.sys.view.home.bean.CustoDatailBean
import com.toker.sys.view.home.bean.OverdFollowBean
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import com.toker.sys.view.home.fragment.custom.event.MyCusEvent
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class OverdFollowUpAdapter(
    val mContext: Context,
    var mBeans: MutableList<OverdFollowBean.Record>
) : BaseAdapter<OverdFollowBean.Record, OverdFollowUpAdapter.OFUViewholder>(mContext, mBeans) {

    private val sdf1 = SimpleDateFormat("yyyy-MM-dd")
    var isTytp:Boolean = false

    constructor( mContext: Context,
                 mBeans: MutableList<OverdFollowBean.Record>,isTytp:Boolean):this(mContext,mBeans){
        this.isTytp = isTytp
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OFUViewholder {
        if (mHeaderView != null && p1 == TYPE_HEADER) {
            return OFUViewholder(mHeaderView!!)
        }
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_custo_detail, null)
        return OFUViewholder(view)
    }

    override fun onBindViewHolder(p0: OFUViewholder, p1: Int) {
        if (getItemViewType(p1) == TYPE_HEADER) {
            return
        }
        val position = getRealPosition(p0)
        val data = mData[position]
        p0.tv01.text = data.customerName
        p0.tv02.text = data.phone
        p0.tv08.visibility = if (data.status != "1") View.VISIBLE else View.GONE
        p0.llCusto.visibility = View.VISIBLE
//        val day = TimeUtils.getDaySub(data.createTime).toString()

        p0.tv03.text = Html.fromHtml(String.format(mContext.getString(if (isTytp)R.string.tip_task_day1 else R.string.tip_task_day), data.count))
        p0.img.visibility = View.GONE
        p0.tv05.text = sdf1.format(Date(data.createTime))
        p0.tv06.text = data.userName
//        data.status = "2"
        p0.tv07.setTextColor(mContext.resources.getColor(if (data.status != "1") R.color.c_txt_tjchenggong else R.color.c_red_4))
        p0.tv07.setBackgroundColor(mContext.resources.getColor(if (data.status != "1") R.color.c_bg_tjchenggong else R.color.c_red_5))
        p0.tv07.text = if (data.status == "1") "未推荐" else "已推荐"
        p0.tv08.visibility = if (data.status != "1") View.VISIBLE else View.GONE


        p0.btn06.setOnClickListener {
            val bean1 = FCustomDetailTBean.IntentionCustomerVO(
                data.tableTag,
                data.customerId,
                data.customerId,
                data.createTime.toString(),
                " ",
                " ",
                data.customerName,
                data.phone,
                " ",
                " ",
                " ",
                " ",
                0L,
                " ", " "
            )

            val bean2 = FCustomDetailTBean.ReCustomerVO(
                "d",
                "s",
                "2",
                "2",
                "2",
                "2",
                "d",
                "2",
                "d",
                "2",
                "2",
                " "
            )
            val aBean = FCustomDetailTBean.Data(bean1, bean2)

            EventBus.getDefault().post(MyCusEvent(3, aBean))
        }


        p0.tv04.setOnClickListener {
            EventBus.getDefault().post(data)
        }

        p0.tv04.setOnClickListener {
            EventBus.getDefault().post(
                MyCusEvent(
                    1,
                    FollowCTBean.PageData(data.phone, data.customerId, data.tableTag)
                )
            )
        }
        p0.itemView.setOnClickListener {
            EventBus.getDefault().post(
                MyCusEvent(
                    1,
                    FollowCTBean.PageData(data.phone, data.customerId, data.tableTag)
                )
            )
        }
        p0.tv08.setOnClickListener {

            //跟进记录
            EventBus.getDefault().post(
                MyCusEvent(
                    1,
                    FollowCTBean.PageData(data.phone, data.customerId, data.tableTag)
                )
            )
        }
        p0.imgPhone.setOnClickListener {
            //拨打电话
            EventBus.getDefault().post(
                MyCusEvent(
                    2,
                    FollowCTBean.PageData(data.phone, data.customerId, data.tableTag)
                )
            )
        }
    }


    class OFUViewholder(v: View) : RecyclerView.ViewHolder(v) {

        val llCusto = v.findViewById<LinearLayout>(R.id.ll_custo_detail)
        val imgPhone = v.findViewById<ImageView>(R.id.img_phone_custo_detail)

        val tv01 = v.findViewById<TextView>(R.id.tv_custo_detail_01)
        val img = v.findViewById<ImageView>(R.id.img_custo_detail)
        val tv02 = v.findViewById<TextView>(R.id.tv_custo_detail_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_custo_detail_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_custo_detail_04)
        val tv05 = v.findViewById<TextView>(R.id.tv_custo_detail_05)
        val tv06 = v.findViewById<TextView>(R.id.tv_custo_detail_06)
        val tv07 = v.findViewById<TextView>(R.id.tv_custo_detail_07)
        val tv08 = v.findViewById<TextView>(R.id.tv_custo_detail_08)
        val btn06 = v.findViewById<TextView>(R.id.tv_my_cus_06)
    }
}