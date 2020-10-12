package com.toker.sys.dialog.custom.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R

class FUAllRecUserAdapter(val mContext: Context, val mBeans: Array<String>) :
    RecyclerView.Adapter<FUAllRecUserAdapter.FuruViewHodler>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FuruViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_fuall_user, null)
        return FuruViewHodler(view)

    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: FuruViewHodler, p1: Int) {
        p0.tvUser.text = mBeans[p1]
        p0.itemView.setOnClickListener { onClick?.userOnClick(mBeans[p1]) }
    }


    class FuruViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tvUser = v.findViewById<TextView>(R.id.tv_item_fuall_user_01)
    }

    interface FuAllUserClick {
        fun userOnClick(itemName: String)
    }
    private var onClick:FuAllUserClick? = null


    fun setOnClick(onClick:FuAllUserClick){
        this.onClick = onClick
    }
}

