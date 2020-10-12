package com.toker.sys.mvp

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import java.text.SimpleDateFormat

abstract class BaseAdapter<T,V : RecyclerView.ViewHolder?>(val ctx: Context, var mData: MutableList<T>) : RecyclerView.Adapter<V>() {

    val sdf = SimpleDateFormat("yyyy-MM-dd")

    val sdfs = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    var mHeaderView: View? = null
    val TYPE_HEADER = 0  //说明是带有Header的
    var firstNum:String = ""
    var secondNum:String = ""
    val TYPE_NORMAL = 2  //说明是不带有header和footer的

   fun setHeaderView(headerView: View) {
        mHeaderView = headerView
        notifyItemInserted(0)
    }
    //TODO 数据同步
    fun getRealPosition(holder: RecyclerView.ViewHolder): Int {
        val position = holder.layoutPosition
        return if (mHeaderView == null) position else position - 1
    }

    //刷新
    fun refreshData(mBeans: MutableList<T>) {
        this.mData = mBeans
        notifyDataSetChanged()
    }
    //刷新
    fun refreshData(mBeans: MutableList<T>,firstNum:String,secondNum:String) {
        this.mData = mBeans
        this.firstNum = firstNum
        this.secondNum = secondNum
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            //第一个item应该加载Header
            TYPE_HEADER
        } else TYPE_NORMAL


    }

    fun setCViewHolder(){

    }
    override fun getItemCount(): Int {

        return mData.size + 1
    }


}