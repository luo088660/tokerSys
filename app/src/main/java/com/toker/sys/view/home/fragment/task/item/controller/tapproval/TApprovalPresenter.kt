package com.toker.sys.view.home.fragment.task.item.controller.tapproval

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.task.bean.TEntireBean
import org.json.JSONObject

/**
 * @author yyx
 */

class TApprovalPresenter : BasePresenter<TApprovalContract.View>(), TApprovalContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, TEntireBean::class.java)
                if (toBean!!.isSuccess()){
                    mView?.targetTaskPage(toBean.data)
                }
            }
            else -> {
            }
        }

        val msg = jsonObject.optString("msg")
        val status = jsonObject.optInt("status")
//        var user = Util_0.parseObject(jsonObject, User::class.java, "data") ?: User()
        mView?.onSuccessData(url_type,load_type,msg,status)
    }

    override fun loadRepositories() {
        reqData(1)
    }
}