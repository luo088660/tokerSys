package com.toker.sys.view.home.activity.custom.choocustneed.visitingarea

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.custom.bean.ArriveAreaBean
import org.json.JSONObject

/**
 * @author yyx
 */

class VisitingAreaPresenter : BasePresenter<VisitingAreaContract.View>(), VisitingAreaContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(toJson, ArriveAreaBean.ArriveArBean::class.java)
        if (toBean?.isSuccess()!!) {
            mView?.showArriveAreaData(toBean.data)
        }

    }

    override fun loadRepositories() {
        reqData(1)
    }
}