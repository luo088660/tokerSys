package com.toker.sys.view.home.fragment.my.item.TeamMberTnq.extension

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.my.item.bean.ExtensionBean
import org.json.JSONObject

/**
 * @author yyx
 */

class ExtensionPresenter : BasePresenter<ExtensionContract.View>(), ExtensionContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(toJson, ExtensionBean.ExtensiBean::class.java)

        if (toBean!!.isSuccess()) {
            mView?.showDataList(toBean.data)
        }


    }

    override fun loadRepositories() {
        reqData(1)
    }
}