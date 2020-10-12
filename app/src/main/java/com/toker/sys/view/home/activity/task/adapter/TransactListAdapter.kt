package com.toker.sys.view.home.activity.task.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.toker.sys.R
import com.toker.sys.view.home.activity.task.bean.TransactList
import com.toker.sys.view.home.fragment.event.IamgeBitmap
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class TransactListAdapter(val mContext: Context, var mBeans: MutableList<TransactList.Data>) :
    RecyclerView.Adapter<TransactListAdapter.TLViewHodler>() {


    private val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TLViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_admini_tran_detail_03, null)
        return TLViewHodler(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: TLViewHodler, p1: Int) {
        val data = mBeans[p1]
        p0.tv01.text = "${sdf.format(Date(data.updateTime))}\t${data.userName}"
        p0.tv03.text = data.content
        p0.tv02.visibility = View.GONE
        p0.rvList.layoutManager = GridLayoutManager(mContext,2)
        p0.rvList.adapter = RvListAdapter(mContext,data.fileList)

    }

    fun refreshData(data: MutableList<TransactList.Data>) {
        this.mBeans = data
        notifyDataSetChanged()
    }


    class TLViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tv01=v.findViewById<TextView>(R.id.tv_item_admini_01)
        val tv02=v.findViewById<TextView>(R.id.tv_item_admini_02)
        val tv03=v.findViewById<TextView>(R.id.tv_item_admini_03)
        val rvList=v.findViewById<RecyclerView>(R.id.rv_item_admini)
    }

    class RvListAdapter(
        val mContext: Context,
        val fileList: MutableList<TransactList.File>
    ) :RecyclerView.Adapter<RvListAdapter.RLViewHodler>(){
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RLViewHodler {

            val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_rv_list,null)
            return RLViewHodler(view)
        }

        override fun getItemCount(): Int {
            return fileList.size
        }

        override fun onBindViewHolder(p0: RLViewHodler, p1: Int) {
            Glide.with(mContext).load(fileList[p1].filePath).into(p0.tv04)
            p0.tv04.setOnClickListener{
                EventBus.getDefault().post(IamgeBitmap( fileList[p1].filePath))
            }
        }


        class RLViewHodler(v:View):RecyclerView.ViewHolder(v){
            val tv04 = v.findViewById<ImageView>(R.id.tv_item_admini_04)
        }
    }
}