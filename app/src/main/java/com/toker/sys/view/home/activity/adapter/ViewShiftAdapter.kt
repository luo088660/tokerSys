package com.toker.sys.view.home.activity.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.toker.reslib.sm.Text
import com.toker.sys.R
import com.toker.sys.view.home.activity.my.bean.ViewShiftBean

class ViewShiftAdapter(val mContext: Context, var mBeans: MutableList<ViewShiftBean.Work>) :
    RecyclerView.Adapter<ViewShiftAdapter.VSViewHodler>() {
//    val options = RequestOptions()
//    .placeholder(R.drawable.img_default)//图片加载出来前，显示的图片
//    .fallback( R.drawable.img_blank) //url为空的时候,显示的图片
//    .error(drawable.img_load_failure);//图片加载失败后，显示的图片

    //刷新列表
    fun refreshView(mBeans: MutableList<ViewShiftBean.Work>){
        this.mBeans = mBeans
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): VSViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_view_shift2,null)
        return VSViewHodler(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size

    }

    override fun onBindViewHolder(p0: VSViewHodler, p1: Int) {

        val work = mBeans[p1]
        Glide.with(mContext).load(work.pic).into(p0.img)
        p0.tv01.text = work.userName
        p0.tv02.text = when (work.personalFlag) {
            "1" -> {
                "事假"
            }
            "0" -> {
                "病假"
            }
            else -> {
                ""
            }
        }
        p0.tv02.visibility = if (TextUtils.isEmpty(work.personalFlag))View.GONE else View.VISIBLE


    }

    class VSViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val img = v.findViewById<ImageView>(R.id.img_item_view_shift)
        val tv01 = v.findViewById<TextView>(R.id.tv_item_view_shift_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_item_view_shift_02)

    }
}