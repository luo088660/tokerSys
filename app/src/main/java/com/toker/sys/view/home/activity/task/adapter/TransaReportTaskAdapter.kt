package com.toker.sys.view.home.activity.task.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.toker.sys.R
import com.toker.sys.view.home.activity.task.event.TransaReportTaskEvent
import com.toker.sys.view.home.fragment.event.IamgeBitmap
import org.greenrobot.eventbus.EventBus

class TransaReportTaskAdapter(
    val mContext: Context,
    var mBeas: MutableList<String>
) : RecyclerView.Adapter<TransaReportTaskAdapter.TRTViewHodler>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TRTViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_trans_img, p0,false)
        return TRTViewHodler(view)
    }

    //刷新
    fun reFreshData(mBeas: MutableList<String>){
        this.mBeas = mBeas
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return mBeas.size
    }
    override fun onBindViewHolder(p0: TRTViewHodler, p1: Int) {
        Glide.with(mContext).load(mBeas[p1]).into(p0.img);
        p0.imgItem.setOnClickListener{
            //删除图片
            EventBus.getDefault().post(TransaReportTaskEvent(p1))
        }
        p0.img.setOnClickListener{
            EventBus.getDefault().post(IamgeBitmap( mBeas[p1]))
        }
    }
    class TRTViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val img=v.findViewById<ImageView>(R.id.img_transa_report_)
        val imgItem=v.findViewById<ImageView>(R.id.img_item_trans)

    }
}