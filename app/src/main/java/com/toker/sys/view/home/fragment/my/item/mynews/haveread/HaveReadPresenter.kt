package com.toker.sys.view.home.fragment.my.item.mynews.haveread

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.my.item.bean.UnreadNewsBean
import org.json.JSONObject

/**
 * @author yyx
 */

class HaveReadPresenter : BasePresenter<HaveReadContract.View>(), HaveReadContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, UnreadNewsBean.NewsBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showDataList(toBean.data)
                }
            }
            else -> {
            }
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }
}