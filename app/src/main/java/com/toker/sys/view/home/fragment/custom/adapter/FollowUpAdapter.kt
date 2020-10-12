package com.toker.sys.view.home.fragment.custom.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.mvp.BaseAdapter
import com.toker.sys.view.home.bean.FollowUpBean
import com.toker.sys.view.home.bean.ProjectNoBean
import com.toker.sys.view.home.bean.ProjectToBean
import com.toker.sys.view.home.fragment.task.bean.PageData

class FollowUpAdapter(
    val context: Context,
    var mBeans: MutableList<FollowUpBean.Record>
) : BaseAdapter<FollowUpBean.Record, FollowUpAdapter.FUViewHodlr>(context, mBeans) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FUViewHodlr {
        if (mHeaderView != null && p1 == TYPE_HEADER) {
            return FUViewHodlr(mHeaderView!!)
        }
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout_foll_up, null)

        return FUViewHodlr(view)
    }

    override fun onBindViewHolder(p0: FUViewHodlr, p1: Int) {
        if (getItemViewType(p1) == TYPE_HEADER) {
            return
        }
        val position = getRealPosition(p0)
        val data = mData[position]
        p0.tv01.text = "${p1}"
        p0.tv02.text = data.projectName
        p0.tv03.text = when (firstNum) {
            "noRecommendNum" -> {
                "${if (data.noRecommendNum > 0) data.noRecommendNum else "0"}"
            }
            "recommendNum" -> {
                "${if (data.recommendNum > 0) data.recommendNum else "0"}"
            }
            else -> {
                //客户总数默认
                "${if (data.totalNum > 0) data.totalNum else "0"}"
            }
        }
        p0.tv04.text =when (secondNum) {
            "reportNum" -> {"${if (data.reportNum > 0) data.reportNum else "0"}"
            }
            "visitNum"-> {"${if (data.visitNum > 0) data.visitNum else "0"}"
            }
            "recruitNum" -> {"${if (data.recruitNum > 0) data.recruitNum else "0"}"
            }
            "subscriptionNum"-> {"${if (data.subscriptionNum > 0) data.subscriptionNum else "0"}"
            }
            "signingNum" -> {"${if (data.signingNum > 0) data.signingNum else "0"}"
            }
            "expiredNum" -> {"${if (data.expiredNum > 0) data.expiredNum else "0"}"
            }
            else -> {
                //已推荐客户 默认
                "${if (data.recommendNum > 0) data.recommendNum else "0"}"
            }
        }

    }


    inner class FUViewHodlr(v: View) : RecyclerView.ViewHolder(v) {
        val tv01 = v.findViewById<TextView>(R.id.tv_foll_up_01)
        val tv02 = v.findViewById<TextView>(R.id.tv_foll_up_02)
        val tv03 = v.findViewById<TextView>(R.id.tv_foll_up_03)
        val tv04 = v.findViewById<TextView>(R.id.tv_foll_up_04)

    }
}