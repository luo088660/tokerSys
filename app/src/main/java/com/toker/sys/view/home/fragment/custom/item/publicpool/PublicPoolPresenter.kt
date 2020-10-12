package com.toker.sys.view.home.fragment.custom.item.publicpool

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.custom.bean.PublicPoolBean
import org.json.JSONObject

/**
 * @author yyx
 */

class PublicPoolPresenter : BasePresenter<PublicPoolContract.View>(), PublicPoolContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(toJson, PublicPoolBean.PublicBean::class.java)
        if (toBean?.isSuccess()!!) {
            mView?.onShowData(toBean.data)
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }
}