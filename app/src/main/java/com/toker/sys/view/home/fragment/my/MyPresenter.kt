package com.toker.sys.view.home.fragment.my

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.my.item.bean.GroupListBean
import org.json.JSONObject

/**
 * @author yyx
 */

class MyPresenter : BasePresenter<MyContract.View>(), MyContract.Presenter {
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val toJson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(toJson, GroupListBean.GroupLBean::class.java)
        if (toBean?.isSuccess()!!) {
            mView?.showGroupList(toBean.data)
        }

    }

    override fun loadRepositories() {
        reqData(1)
    }



}