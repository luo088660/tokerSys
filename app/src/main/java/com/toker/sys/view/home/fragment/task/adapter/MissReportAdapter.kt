package com.toker.sys.view.home.fragment.task.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.toker.sys.R
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.fragment.event.IamgeBitmap
import com.toker.sys.view.home.fragment.task.bean.MissReportBean
import org.greenrobot.eventbus.EventBus
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MissReportAdapter(val mContext: Context, var mBeans: MutableList<MissReportBean.Record>) :
    RecyclerView.Adapter<MissReportAdapter.MRViewHodler>() {
    private val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")


    fun refreshView(mBeans: MutableList<MissReportBean.Record>) {
        this.mBeans = mBeans
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MRViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_admini_tran_detail_03, null)
        return MRViewHodler(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: MRViewHodler, p1: Int) {
        val record = mBeans[p1]
        p0.tv01.text = "${sdf.format(Date(record.updateTime))}\t${record.userName}"
        p0.tv03.text = record.content
        p0.rvList.layoutManager = GridLayoutManager(mContext,2)
        p0.rvList.adapter = RvListAdapter(mContext,record.fileList)
        p0.tv02.setOnClickListener {
            EventBus.getDefault().post(record)

        }

    }


    class MRViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_item_admini_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_item_admini_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_item_admini_03)
        val rvList = v.findViewById<RecyclerView>(R.id.rv_item_admini)

    }

    class RvListAdapter(
        val mContext: Context,
        val fileList: List<MissReportBean.File>
    ) :RecyclerView.Adapter<RvListAdapter.RLViewHodler>(){
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RLViewHodler {
           val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_rv_list,null)
            return RLViewHodler(view)
        }
        private val TAG = "MissReportAdapter";
        override fun getItemCount(): Int {
            return fileList.size
        }

        override fun onBindViewHolder(p0: RLViewHodler, p1: Int) {

          Glide.with(mContext).load(fileList[p1].filePath).into(p0.tv04)
            p0.tv04.setOnClickListener {
                LogUtils.d(TAG, "setOnClickListener:setOnClickListener ");
                EventBus.getDefault().post(IamgeBitmap( fileList[p1].filePath))
            }
        }


        class RLViewHodler(v:View):RecyclerView.ViewHolder(v){
            val tv04 = v.findViewById<ImageView>(R.id.tv_item_admini_04)
        }
    }


}