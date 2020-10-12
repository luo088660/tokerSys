package com.toker.sys.view.home.fragment.sheet

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.my.bean.ProjectFormanBean
import com.toker.sys.view.home.bean.ProjectListBean
import com.toker.sys.view.home.fragment.my.item.bean.GroupListBean
import org.json.JSONObject

/**
 * @author yyx
 */

class SheetPresenter : BasePresenter<SheetContract.View>(), SheetContract.Presenter {
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, ProjectListBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showProjectDate(toBean.data)
                }
            }
            else -> {
                val toBean = GsonUtil.GsonToBean(toJson, GroupListBean.GroupLBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showGroupList(toBean.data)
                }
            }
        }

    }

    override fun loadRepositories() {
        reqData(1)
        reqData(2)
    }
}