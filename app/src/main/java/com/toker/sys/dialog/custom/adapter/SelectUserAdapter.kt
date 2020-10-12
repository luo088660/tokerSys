package com.toker.sys.dialog.custom.adapter

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.view.home.fragment.custom.bean.SelectUserList


class SelectUserAdapter(val mContext: Context, var mBeans: MutableList<SelectUserList.PageData>) :
    RecyclerView.Adapter<SelectUserAdapter.SUViewHodler>() {

    private var selected = -1
    fun refreshData(record: MutableList<SelectUserList.PageData>) {
        this.mBeans = record
        notifyDataSetChanged()
    }

    public fun setSelected(i: Int) {
        this.selected = i;
    }


    private var mOnItemClickLitener: OnItemLitener? = null

    public interface OnItemLitener {
        fun onItemClick(record: SelectUserList.PageData, position: Int);
    }

    public fun setOnItemkLitener(mOnItemClickLitener: OnItemLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SUViewHodler {

        val view =
            LayoutInflater.from(mContext).inflate(R.layout.layout_item_select_user_dialog, null)
        return SUViewHodler(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(p0: SUViewHodler, p1: Int) {
        val record = mBeans[p1]
        p0.tv.text = record.userName

        if (selected == p1) {
            p0.img.setImageResource(R.mipmap.icon_path_3);
        } else {
            p0.img.setImageResource(0)
        }
        p0.itemView.setOnClickListener {
            mOnItemClickLitener?.onItemClick(record, p1)
        }

    }


    class SUViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tv = v.findViewById<TextView>(R.id.tv_item_select)
        val img = v.findViewById<ImageView>(R.id.img_item_select)

    }
}