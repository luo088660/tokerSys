package com.toker.sys.view.home.fragment.custom.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.mvp.BaseAdapter
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.tools.TimeUtils
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import com.toker.sys.view.home.fragment.custom.bean.FollowCustBean
import com.toker.sys.view.home.fragment.custom.bean.Record
import com.toker.sys.view.home.fragment.custom.event.MyCusEvent
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class PublicPoCustAdapter(
    val mContext: Context,
    val mBeans: MutableList<FollowCustBean.PageData>,
    val type: Int
) :
    BaseAdapter<FollowCustBean.PageData, PublicPoCustAdapter.PPCViewHodler>(mContext, mBeans) {
    private val sdf1 = SimpleDateFormat("yyyy-MM-dd")

    private val TAG = "PublicPoCustAdapter";
    //多选
    private val mMaps = HashMap<Int, Boolean>()

    init {
        for (i in 0 until mBeans.size) {
            mMaps[i] = false//记录状态
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PPCViewHodler {
        if (mHeaderView != null && p1 == TYPE_HEADER) {
            return PPCViewHodler(mHeaderView!!)
        }
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_public_po_cust, p0, false)

        return PPCViewHodler(view)
    }


    override fun onBindViewHolder(p0: PPCViewHodler, p1: Int) {
        if (getItemViewType(p1) == TYPE_HEADER) {
            return
        }
        val position = getRealPosition(p0)
        val data = mData[position]
        p0.cb01.text = data.name
        p0.tv02.text = data.phone
//        val day = TimeUtils.getDaySub(data.updateTime).toString()
        p0.tv03.text = Html.fromHtml(
            String.format(
                mContext.getString(R.string.tip_task_day),
                data.notFollowDays
            )
        )

        p0.tv05.text = sdf1.format(Date(data.createTime))
        p0.tv06.text = if (data.userName == null) {
            p0.tv06.setTextColor(mContext.resources.getColor(R.color.c_red_4))
            "待分配"
        } else {
            p0.tv06.setTextColor(mContext.resources.getColor(R.color.c_black_6))
            data.userName
        }
        p0.ll10.visibility = if (type == 1) View.GONE else View.VISIBLE
        p0.tv10.text = data.invalidReason
        p0.tv07.setTextColor(mContext.resources.getColor(if (data.status != "1") R.color.c_txt_tjchenggong else R.color.c_red_4))
        p0.tv07.setBackgroundColor(mContext.resources.getColor(if (data.status != "1") R.color.c_bg_tjchenggong else R.color.c_red_5))
        p0.tv07.text = if (data.status == "1") "未推荐" else "已推荐"

//        p0.tv09.visibility = if (data.userName == null) View.VISIBLE else View.GONE

        p0.tv04.setOnClickListener {
            //跟进记录
            EventBus.getDefault().post(
                MyCusEvent(
                    1,
                    FollowCTBean.PageData(data.id, data.tableTag, data.notFollowDays.toInt())
                )
            )
        }

        p0.cb01.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            mMaps[p1] = isChecked
        })

        if (mMaps[p1] == null) {
            mMaps[p1] = false
        }
        //分配拓客员
        p0.tv09.setOnClickListener {
            EventBus.getDefault().post(data)

        }
        LogUtils.d(TAG, "mMaps:$mMaps ");
        p0.cb01.isChecked = mMaps[p1]!!
    }

    inner class PPCViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val cb01 = v.findViewById<CheckBox>(R.id.cb_item_po_cust_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_custo_detail_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_custo_detail_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_custo_detail_04)
        val tv05 = v.findViewById<TextView>(R.id.tv_custo_detail_05)
        val tv06 = v.findViewById<TextView>(R.id.tv_custo_detail_06)
        val tv07 = v.findViewById<TextView>(R.id.tv_custo_detail_07)
        val tv08 = v.findViewById<TextView>(R.id.tv_custo_detail_08)
        val tv09 = v.findViewById<TextView>(R.id.tv_custo_detail_09)
        val tv10 = v.findViewById<TextView>(R.id.tv_custo_detail_10)
        val ll10 = v.findViewById<LinearLayout>(R.id.ll_custo_detail_10)

    }

    public var onItemClickListener: RecyclerViewOnItemClickListener? = null

    //回调的接口
    fun setItemClickListener(onItemClickListener: RecyclerViewOnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    //接口回调设置点击事件
    interface RecyclerViewOnItemClickListener {
        //点击事件
        fun onItemClickListener(view: View, position: Int)
    }

    //全选方法
    fun All() {
        /*val entries = mMaps.entries
        var shouldall = false
        for (entry in entries) {
            val value = entry.value
            if ((!value)!!) {
                shouldall = true
                break
            }
        }
        for (entry in entries) {
            entry.setValue(shouldall)
        }*/
        for (i in 1 until mBeans.size + 1) {
            mMaps[i] = true//记录状态
        }
        notifyDataSetChanged()
    }

    //反选
    fun neverall() {
        /*val entries = mMaps.entries
        for (entry in entries) {
            entry.setValue(!entry.value)
        }*/
        for (i in 1 until mBeans.size + 1) {
            mMaps[i] = false//记录状态
        }
        notifyDataSetChanged()
    }

    //多选
    fun MultiSelection(position: Int) {
        //对当前状态取反
        mMaps[position] = !mMaps[position]!!
        notifyItemChanged(position)
    }

    //获取最终的map存储数据
    fun getMap(): Map<Int, Boolean> {
        return mMaps
    }
}