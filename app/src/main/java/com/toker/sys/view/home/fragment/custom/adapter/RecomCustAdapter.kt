package com.toker.sys.view.home.fragment.custom.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.mvp.BaseAdapter
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import com.toker.sys.view.home.activity.custom.bean.RecomCustBean
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import com.toker.sys.view.home.fragment.custom.bean.Record
import com.toker.sys.view.home.fragment.custom.event.MyCusEvent
import com.toker.sys.view.home.fragment.task.bean.PageData
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class RecomCustAdapter(
    val context: Context,
    var mBeans: MutableList<RecomCustBean.PageData>
) : BaseAdapter<RecomCustBean.PageData, RecomCustAdapter.RCViewHodlr>(context, mBeans) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RCViewHodlr {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        if (mHeaderView != null && p1 == TYPE_HEADER) {
            return RCViewHodlr(mHeaderView!!)
        }
        val view = LayoutInflater.from(context).inflate(R.layout.layout_recom_sust_02, null)

        return RCViewHodlr(view)
    }

    override fun onBindViewHolder(p0: RCViewHodlr, p1: Int) {
        if (getItemViewType(p1) == TYPE_HEADER) {
            return
        }
        val position = getRealPosition(p0)
        val data = mData[position]
        p0.tv01.text = data.name
        p0.tv02.text = data.phone
        p0.tv04.text = data.recommendProject
        p0.tv05.text = data.createTime
//        p0.tv05.text = data.visitTime

//        statusVal	已推荐的客户的状态	String	客户状态（1：报备，2：到访，3：认筹，4：认购，5：签约，6：过期）
       p0.tv06.text = when (data.status.toInt()) {
            1 -> {
                p0.tv06.setTextColor(context.resources.getColor(R.color.c_txt_tjchenggong))
                p0.tv06.setBackgroundColor(context.resources.getColor(R.color.c_bg_tjchenggong))
                "已推荐"
            }
            2 -> {
                p0.tv06.setTextColor(context.resources.getColor(R.color.c_txt_yidaofang))
                p0.tv06.setBackgroundColor(context.resources.getColor(R.color.c_bg_yidaofang))
                "已到访"
            }
            3 -> {
                p0.tv06.setTextColor(context.resources.getColor(R.color.c_txt_yirenchou))
                p0.tv06.setBackgroundColor(context.resources.getColor(R.color.c_bg_yirenchou))
                "已认筹"
            }
            4 -> {
                p0.tv06.setTextColor(context.resources.getColor(R.color.c_txt_yirengou))
                p0.tv06.setBackgroundColor(context.resources.getColor(R.color.c_bg_yirengou))
                "已认购"
            }
            5 -> {
                p0.tv06.setTextColor(context.resources.getColor(R.color.c_txt_yiqianyue))
                p0.tv06.setBackgroundColor(context.resources.getColor(R.color.c_bg_yiqianyue))
                "已签约"
            }
            else -> {
                p0.tv06.setTextColor(context.resources.getColor(R.color.c_txt_yiguoqi))
                p0.tv06.setBackgroundColor(context.resources.getColor(R.color.c_bg_yiguoqi))
                "已过期"
            }
        }
        p0.ll.visibility = View.GONE
        p0.tv07.text = data.userName
        p0.tv03.setOnClickListener{
            EventBus.getDefault().post(data)
        }
        p0.itemView.setOnClickListener{
            EventBus.getDefault().post(data)
        }
        if(AppApplication.TYPE == Constants.RESCUE2){
            p0.imgPhone.visibility = View.GONE
            p0.btn06.visibility = View.GONE
        }
        p0.imgPhone.setOnClickListener {
            //拨打电话
            EventBus.getDefault().post(MyCusEvent(2,FollowCTBean.PageData(data.phone,data.id,data.tableTag)))
        }
        p0.btn06.setOnClickListener {
            val bean1 = FCustomDetailTBean.IntentionCustomerVO(
                data.tableTag,
                data.customerId,
                data.customerId,
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
                " "," ")

            val bean2 = FCustomDetailTBean.ReCustomerVO(
                " ",
                " ",
                if (data.houseType.isNullOrEmpty())" " else data.houseType,
                if (data.intentionLevel.isNullOrEmpty())" " else data.intentionLevel,
                if (data.productAcreage.isNullOrEmpty())" " else data.productAcreage,
                if (data.productType.isNullOrEmpty())" " else data.productType,
                " ",
                if (data.purchaseFocus.isNullOrEmpty())" " else data.purchaseFocus,
                " ",
                " ",
                if (data.sixNumbers.isNullOrEmpty())" " else data.sixNumbers,
                " "
            )
            val aBean = FCustomDetailTBean.Data(bean1,bean2)

            EventBus.getDefault().post(MyCusEvent(3,aBean))
        }
    }


    inner class RCViewHodlr(v: View) : RecyclerView.ViewHolder(v) {
        val imgPhone = v.findViewById<ImageView>(R.id.img_phone_custo_detail)
        val btn06 = v.findViewById<TextView>(R.id.tv_my_cus_06)

        val tv01 = v.findViewById<TextView>(R.id.tv_custo_detail_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_custo_detail_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_custo_detail_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_custo_detail_04)
        val tv05 = v.findViewById<TextView>(R.id.tv_custo_detail_05)
        val tv06 = v.findViewById<TextView>(R.id.tv_custo_detail_06)
        val tv07 = v.findViewById<TextView>(R.id.tv_custo_detail_07)
        val ll = v.findViewById<LinearLayout>(R.id.ll_recon_sust)

    }

}