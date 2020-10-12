package com.toker.sys.view.home.activity.sheet.stprojectran

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.bean.STExtenRankBean
import org.json.JSONObject

/**
 * @author yyx
 */

class STProjectRanPresenter : BasePresenter<STProjectRanContract.View>(), STProjectRanContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()

        val toBean = GsonUtil.GsonToBean(toJson, STExtenRankBean.STExtenBean::class.java)
        if (toBean?.isSuccess()!!) {
            mView?.showListData(toBean.data)
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }
}