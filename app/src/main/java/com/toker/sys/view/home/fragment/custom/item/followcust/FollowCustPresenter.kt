package com.toker.sys.view.home.fragment.custom.item.followcust

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.custom.bean.FollowCustBean
import org.json.JSONObject

/**
 * @author yyx
 */

class FollowCustPresenter : BasePresenter<FollowCustContract.View>(), FollowCustContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(toJson, FollowCustBean.FollowCtBean::class.java)

        if (toBean!!.isSuccess()){
            mView?.showData(toBean.data)

        }
    }

    override fun loadRepositories() {
        reqData(1)
    }
}