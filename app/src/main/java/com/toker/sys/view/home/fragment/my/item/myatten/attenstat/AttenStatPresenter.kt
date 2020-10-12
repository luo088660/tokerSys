package com.toker.sys.view.home.fragment.my.item.myatten.attenstat

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.my.bean.AttenDetailBean
import com.toker.sys.view.home.fragment.my.item.bean.AttenStatBean
import org.json.JSONObject

/**
 * @author yyx
 */

class AttenStatPresenter : BasePresenter<AttenStatContract.View>(), AttenStatContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, AttenStatBean.AListBean::class.java)
                if (toBean!!.isSuccess()) {
                    if (!toBean.data.isNullOrEmpty()) {
                        mView?.showListData(toBean.data)
                    }else{
                        mView?.showErrorListData()
                    }
                }

            }
            2 -> {
                val toBean = GsonUtil.GsonToBean(toJson, AttenDetailBean.AttenDataBean::class.java)
                if (toBean?.isSuccess()!!) {
                    if (toBean!!.data != null) {
                        mView?.showDataList(toBean!!.data)
                    }else{
                        mView?.showErrorData()
                    }
                }
            }
            else -> {
            }
        }

    }

    override fun loadRepositories() {
        reqData(1)
        ByTeam()
    }

    override fun ByTeam() {
        reqData(2)

    }
}