package com.toker.sys.view.home.fragment.sheet.item.topcustperfran

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.my.item.bean.GroupListBean
import org.json.JSONObject

/**
 * @author yyx
 */

class TopCustPerfRanPresenter : BasePresenter<TopCustPerfRanContract.View>(), TopCustPerfRanContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, GroupListBean.GroupLBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showGroupList(toBean.data)
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