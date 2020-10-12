package com.toker.sys.view.home.fragment.my.item.myatten.meatten

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.my.item.myatten.bean.MeAttenBean
import org.json.JSONObject

/**
 * @author yyx
 */

class MeAttenPresenter : BasePresenter<MeAttenContract.View>(), MeAttenContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, MeAttenBean.MeAttenTBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showData(toBean.data)
                }else{
                    mView?.onShowError(toBean.desc)
                }
            }
            2, 3 ,5,6-> {
                val toBean = GsonUtil.GsonToBean(toJson, Bean.bean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.onSuccessData( url_type, load_type, "",0)
                }else{
                    mView?.onErrorData(toBean.desc)
                }
            }

            else -> {
            }
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }

    override fun checkIn() {

        reqData(2)
    }

    override fun checkOut() {
        reqData(3)
    }

    override fun uploadLocation() {
        reqData(4)
    }

    override fun updateCheck(type: Int) {
        reqData(type)
    }

    class Bean {
        data class bean(
            val `data`: Any,
            val code: String,
            val desc: String
        ) {
            fun isSuccess(): Boolean {
                return code == "1"
            }
        }
    }
}