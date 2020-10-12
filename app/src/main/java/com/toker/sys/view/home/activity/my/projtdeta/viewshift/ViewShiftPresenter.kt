package com.toker.sys.view.home.activity.my.projtdeta.viewshift

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.my.bean.ViewShiftBean
import org.json.JSONObject

/**
 * @author yyx
 */

class ViewShiftPresenter : BasePresenter<ViewShiftContract.View>(), ViewShiftContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, Bean.TimeBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showTime(toBean.data)
                }else{
                    mView?.onErrorData(toBean.desc!!)
                }
            }
            2 -> {
                val toBean = GsonUtil.GsonToBean(toJson, ViewShiftBean.ViewShiftDBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.DetailByDay(toBean.data)
                }else{
                    mView?.onErrorVData()
                }
            }
            else -> {
            }
        }

    }

    override fun loadRepositories() {
        reqData(1)
        showTime()

    }

    override fun showTime() {
        reqData(2)
    }


    class Bean {
        data class TimeBean(
            val `data`: MutableList<String>,
            val code: String,
            val desc: String
        ) {
            fun isSuccess(): Boolean {
                return code == "1"
            }
        }
    }

}