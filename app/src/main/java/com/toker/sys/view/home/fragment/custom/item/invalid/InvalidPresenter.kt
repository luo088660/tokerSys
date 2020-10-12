package com.toker.sys.view.home.fragment.custom.item.invalid

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import org.json.JSONObject

/**
 * @author yyx
 */

class InvalidPresenter : BasePresenter<InvalidContract.View>(), InvalidContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val msg = jsonObject.optString("msg")
        val status = jsonObject.optInt("status")
//        var user = Util_0.parseObject(jsonObject, User::class.java, "data") ?: User()
        mView?.onSuccessData(url_type,load_type,msg,status)
    }

    override fun loadRepositories() {
        reqData()
    }
}