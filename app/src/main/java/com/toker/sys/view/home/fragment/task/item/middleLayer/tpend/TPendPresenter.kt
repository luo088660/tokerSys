package com.toker.sys.view.home.fragment.task.item.middleLayer.tpend

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.task.bean.TEntireBean
import com.toker.sys.view.home.fragment.task.bean.TPendBean
import org.json.JSONObject

/**
 * @author yyx
 */

class TPendPresenter : BasePresenter<TPendContract.View>(), TPendContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val bean = GsonUtil.GsonToBean(toJson, TPendBean::class.java)
                if (bean!!.isSuccess()) {
                    mView?.showApproveCount(bean.data)
                }
            }
            2 -> {
                val toBean = GsonUtil.GsonToBean(toJson, TEntireBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.targetTaskPage(toBean.data)
                }
            }
            3->{
                val bean = GsonUtil.GsonToBean(toJson,Bean.Bean::class.java)
                if (bean!!.isSuccess()) {
                    mView?.onSuccess()
                }
            }
            else -> {
            }
        }

        val msg = jsonObject.optString("msg")
        val status = jsonObject.optInt("status")
//        var user = Util_0.parseObject(jsonObject, User::class.java, "data") ?: User()
        mView?.onSuccessData(url_type, load_type, msg, status)
    }

    override fun loadRepositories() {
//        reqData(1)
        reqData(2)
    }

    override fun approveResult() {
        reqData(3)
    }

    class Bean {
        data class Bean(
            val `data`: Any,
            val code: String,
            val desc: String
        ){
            fun isSuccess(): Boolean {
                return code == "1"
            }
        }
    }
}