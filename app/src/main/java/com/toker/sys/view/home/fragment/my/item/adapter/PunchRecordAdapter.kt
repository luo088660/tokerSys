package com.toker.sys.view.home.fragment.my.item.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.toker.sys.R
import com.toker.sys.view.home.activity.task.bean.PunchRecordBean
import com.toker.sys.view.home.fragment.event.IamgeBitmap
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat

class PunchRecordAdapter(
    val mContext: Context,
   var mBeans: MutableList<PunchRecordBean.PageData>
) :RecyclerView.Adapter<PunchRecordAdapter.PRViewHolder>() {
    fun reFreshData(mBeans: MutableList<PunchRecordBean.PageData>) {
        this.mBeans = mBeans
        notifyDataSetChanged()

    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PRViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_punch_record_01,null)
        return PRViewHolder(view)
    }

    override fun getItemCount(): Int {

        return mBeans.size
    }

    override fun onBindViewHolder(p0: PRViewHolder, p1: Int) {
        val data = mBeans[p1]

        p0.tv01.text = data.beeName
        p0.tv02.text = data.beePhone
        p0.tv03.text = data.createTime
        p0.tv04.text = "${data.dailyAttendanceNum}"
        
        p0.rvRecord.layoutManager = GridLayoutManager(mContext,1)
        p0.rvRecord.adapter = RecordItemAdapter(mContext,data.attendanceRecordList)


    }

    class PRViewHolder(v: View):RecyclerView.ViewHolder(v){
        val tv01 = v.findViewById<TextView>(R.id.tv_punch_record_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_punch_record_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_punch_record_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_punch_record_04)
        val tv05 = v.findViewById<TextView>(R.id.tv_punch_record_05)
        val rvRecord = v.findViewById<RecyclerView>(R.id.rv_item_record)

    }
    
    class RecordItemAdapter(val mContext: Context,val mBeans:MutableList<PunchRecordBean.AttendanceRecord>):RecyclerView.Adapter<RecordItemAdapter.RIViewHolder>(){

        val sdfs = SimpleDateFormat("HH:mm")
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RIViewHolder {
            val view = LayoutInflater.from(mContext).inflate(R.layout.item_record_item,null)
            return RIViewHolder(view)
        }

        override fun getItemCount(): Int {
            return mBeans.size
            
        }

        override fun onBindViewHolder(p0: RIViewHolder, p1: Int) {
            val record = mBeans[p1]
            p0.tv01.text ="${ sdfs.format(record.attendanceTime)} | 签到"
            p0.tv02.text = record.address
            p0.rvIamge.layoutManager = GridLayoutManager(mContext,5)
            p0.rvIamge.adapter = ImageItemAdapter(mContext,record.pictureUrlList)
        }

        class RIViewHolder(v:View):RecyclerView.ViewHolder(v){
            val tv01 = v.findViewById<TextView>(R.id.tv_01)
            val tv02 = v.findViewById<TextView>(R.id.tv_02)
            val rvIamge = v.findViewById<RecyclerView>(R.id.img_item)

        }
        
        class ImageItemAdapter(  val mContext: Context,
                                 val fileList: MutableList<String>):RecyclerView.Adapter<ImageItemAdapter.IIViewHodler>(){
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): IIViewHodler {
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_rv_list1,null)
                return IIViewHodler(view)
            }

            override fun getItemCount(): Int {
                return fileList.size
            }

            override fun onBindViewHolder(p0: IIViewHodler, p1: Int) {
                Glide.with(mContext).load(fileList[p1]).into(p0.tv04)
                p0.tv04.setOnClickListener {
                    EventBus.getDefault().post(IamgeBitmap( fileList[p1]))
                }
            }

            class IIViewHodler(v:View):RecyclerView.ViewHolder(v){
                val tv04 = v.findViewById<ImageView>(R.id.tv_item_admini_04)
            }
            
        }
        
        
    }
}