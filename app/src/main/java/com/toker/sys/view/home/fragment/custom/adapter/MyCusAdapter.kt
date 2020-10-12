package com.toker.sys.view.home.fragment.custom.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.mvp.BaseAdapter
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import com.toker.sys.view.home.fragment.custom.event.MyCusEvent
import com.toker.sys.view.home.fragment.event.MyOCusEvent
import org.greenrobot.eventbus.EventBus
import java.util.*

class MyCusAdapter(val mContext: Context, var mBeans :MutableList<FollowCTBean.PageData>) :
    BaseAdapter<FollowCTBean.PageData,MyCusAdapter.MyCusViewHodler>(mContext,mBeans) {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyCusViewHodler {
        if (mHeaderView != null && p1 == TYPE_HEADER) {
            return MyCusViewHodler(mHeaderView!!)
        }
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_custo_detail, null)
        return MyCusViewHodler(view)
    }


    override fun onBindViewHolder(p0: MyCusViewHodler, p1: Int) {
        if (getItemViewType(p1) == TYPE_HEADER) {
            return
        }
        val position = getRealPosition(p0)
        val data = mData[position]

        p0.llCusto.visibility = View.VISIBLE
        p0.img.visibility = if (data.beeFlag == "0") View.VISIBLE else View.GONE

        p0.tv01.text = data.name
        p0.tv02.text = data.phone
//        val day = TimeUtils.getDaySub(data.updateTime).toString()

        p0.tv03.text = Html.fromHtml(String.format(mContext.getString(R.string.tip_task_day), data.notFollowDays))

        p0.tv05.text = sdf.format(Date(data.createTime))
        p0.tv06.text = data.userName
        p0.tv07.setTextColor(mContext.resources.getColor(if (data.status != "1") R.color.c_blue_6 else R.color.c_red_4))
        p0.tv07.setBackgroundColor(mContext.resources.getColor(if (data.status != "1") R.color.c_txt_yituijian else R.color.c_red_5))
        p0.tv07.text = if (data.status == "1") "未推荐" else "已推荐"

        p0.btn06.setOnClickListener {
            val bean1 = FCustomDetailTBean.IntentionCustomerVO(
                data.tableTag,
                data.id,
                data.id,
                data.createTime.toString(),
                if (data.userName.isNullOrEmpty())" " else data.userName,
                if (data.registerAddress.isNullOrEmpty())" " else data.registerAddress,
                if (data.name.isNullOrEmpty())" " else data.name,
                if (data.phone.isNullOrEmpty())" " else data.phone,
                if (data.registerAddress.isNullOrEmpty())" " else data.registerAddress,
                if (data.remark.isNullOrEmpty())" " else data.remark,
                if (data.status.isNullOrEmpty())" " else data.status,
                if (data.userName.isNullOrEmpty())" " else data.userName,
               0L,
               " "," "
            )

            val bean2 = FCustomDetailTBean.ReCustomerVO(
                "d",
                "s",
                if (data.houseType.isNullOrEmpty())"2" else data.houseType,
                if (data.intentionLevel.isNullOrEmpty())"2" else data.intentionLevel,
                if (data.productAcreage.isNullOrEmpty())"2" else data.productAcreage,
                if (data.productType.isNullOrEmpty())"2" else data.productType,
                "d",
                if (data.purchaseFocus.isNullOrEmpty())"2" else data.purchaseFocus,
                "d",
                if (data.customerProvince.isNullOrEmpty())"2" else data.customerProvince,
                if (data.sixNumbers.isNullOrEmpty())"2" else data.sixNumbers,
                " "
            )
            val aBean = FCustomDetailTBean.Data(bean1,bean2)

            EventBus.getDefault().post(MyCusEvent(3,aBean))
        }


        p0.tv08.visibility = if (data.status != "1") View.VISIBLE else View.GONE
        p0.tv04.setOnClickListener {
            EventBus.getDefault().post(MyCusEvent(1,data))
        }
        p0.itemView.setOnClickListener {
            EventBus.getDefault().post(MyCusEvent(1,data))
        }
        //明细
        p0.tv08.setOnClickListener {
            //跟进记录
//            EventBus.getDefault().post(MyCusEvent(1,data))
            EventBus.getDefault().post(MyOCusEvent(data.phone))
        }
        p0.imgPhone.setOnClickListener {
            //拨打电话
            EventBus.getDefault().post(MyCusEvent(2,data))
        }

    }

    inner class MyCusViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val llCusto = v.findViewById<LinearLayout>(R.id.ll_custo_detail)
        val imgPhone = v.findViewById<ImageView>(R.id.img_phone_custo_detail)

        val tv01 = v.findViewById<TextView>(R.id.tv_custo_detail_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_custo_detail_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_custo_detail_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_custo_detail_04)
        val tv05 = v.findViewById<TextView>(R.id.tv_custo_detail_05)
        val tv06 = v.findViewById<TextView>(R.id.tv_custo_detail_06)
        val tv07 = v.findViewById<TextView>(R.id.tv_custo_detail_07)
        val tv08 = v.findViewById<TextView>(R.id.tv_custo_detail_08)
        val btn06 = v.findViewById<TextView>(R.id.tv_my_cus_06)
        val img = v.findViewById<ImageView>(R.id.img_custo_detail)
    }
}
