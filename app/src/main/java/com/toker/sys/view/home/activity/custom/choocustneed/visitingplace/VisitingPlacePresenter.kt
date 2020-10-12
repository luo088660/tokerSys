package com.toker.sys.view.home.activity.custom.choocustneed.visitingplace

import android.os.Bundle
import com.google.gson.Gson
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.custom.bean.VisiPlaceBean
import org.json.JSONObject

/**
 * @author yyx
 */

class VisitingPlacePresenter : BasePresenter<VisitingPlaceContract.View>(), VisitingPlaceContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(toJson, VisiPlaceBean.VisiPBean::class.java)
        if (toBean?.isSuccess()!!) {
            mView?.showData(toBean.data)
        }

    }

    override fun loadRepositories() {
        reqData(1)
    }
}