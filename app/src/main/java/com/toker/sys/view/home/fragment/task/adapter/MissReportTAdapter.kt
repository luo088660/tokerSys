package com.toker.sys.view.home.fragment.task.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.toker.sys.R
import com.toker.sys.view.home.fragment.task.bean.MissReportBean

class MissReportTAdapter(val mContext: Context,var mBeans: MutableList<MissReportBean.UnRecord> )
    :RecyclerView.Adapter<MissReportTAdapter.MRTViewHodler>() {

    private val TAG = "MissReportTAdapter";

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MRTViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_miss_report_t,null)
        return MRTViewHodler(view)
    }

    fun refreshView( mBeans: MutableList<MissReportBean.UnRecord>){
        this.mBeans = mBeans
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return mBeans.size }

    override fun onBindViewHolder(p0: MRTViewHodler, p1: Int) {
        val unRecord = mBeans[p1]
        if (!TextUtils.isEmpty(unRecord.pic)){
            Glide.with(mContext).load(unRecord.pic)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(25)))//圆角半径
                .into(p0.img)
        }

        /*Glide.with(mContext).load(unRecord.pic).asBitmap().centerCrop().into(
            object : BitmapImageViewTarget(p0.img) {
                override fun setResource(resource: Bitmap) {
                    val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(
                        mContext.resources, resource);
                    circularBitmapDrawable.isCircular = true;
                    p0.img.setImageDrawable(circularBitmapDrawable);
                }
            })*/
        p0.tv.text = unRecord.userName


    }

    class MRTViewHodler(v:View):RecyclerView.ViewHolder(v){
        val img = v.findViewById<ImageView>(R.id.img_miss_item)
        val tv = v.findViewById<TextView>(R.id.tv_miss_item)

    }
}