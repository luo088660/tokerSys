package com.toker.sys.view.home.activity.custom.choocustneed.adapter

import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.toker.sys.R
import com.toker.sys.view.home.activity.custom.event.ChCustStatuEvent
import org.greenrobot.eventbus.EventBus

class ChCustStatuAdapter(val mContext: Context, val mBeans: Array<String>, val type: Int) :
    RecyclerView.Adapter<ChCustStatuAdapter.CCSViewHodler>() {

    private val isChecks = mutableMapOf<Int, Boolean>()

    init {
        for (i in 0..mBeans.size) {
            isChecks[i] = false
        }
    }

    fun refeshData(mBeans: MutableList<String>) {
        if (mBeans.size==0) return
        for (i in 0 until this.mBeans.size) {
          for (j in   mBeans){
              if (this.mBeans[i] == j) {
                  isChecks[i] = true
              }
          }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CCSViewHodler {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_layout_chcust_stute, null)
        return CCSViewHodler(view)
    }

    override fun getItemCount(): Int {
        return mBeans.size
    }

    override fun onBindViewHolder(p0: CCSViewHodler, p1: Int) {
        p0.tvItem.text = mBeans[p1]
        //type 1.产品类型 2.面积段 3.户型 4.购房关注
        p0.tvItem.isChecked = isChecks[p1]!!

        p0.tvItem.setOnClickListener {

            isChecks[p1] = !isChecks[p1]!!
            val sb = StringBuffer()
            isChecks.forEach{ (t, u) ->
                if (u) {
                    sb.append("${mBeans[t]},")
                }
            }

            EventBus.getDefault().post(ChCustStatuEvent(type,if (!TextUtils.isEmpty(sb))"$sb" else "0"))
//            EventBus.getDefault().post(ChCustStatuEvent(type,mBeans[p1]))
            p0.tvItem.isChecked = true
            notifyDataSetChanged()
        }

    }

    class CCSViewHodler(v: View) : RecyclerView.ViewHolder(v) {
        val tvItem = v.findViewById<CheckBox>(R.id.tv_chcust_stute)
    }
}