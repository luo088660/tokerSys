package com.toker.sys.view.home.activity.sheet.stextenrank

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.bean.ProjectListBean
import com.toker.sys.view.home.bean.STExtenRankBean
import com.toker.sys.view.home.fragment.my.item.bean.GroupListBean
import org.json.JSONObject

/**
 * @author yyx
 */

class STExtenRankPresenter : BasePresenter<STExtenRankContract.View>(),
    STExtenRankContract.Presenter {
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, STExtenRankBean.STExtenBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showListData(toBean.data)
                }
            }
            2->{
                val toBean = GsonUtil.GsonToBean(toJson, ProjectListBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showProjectDate(toBean.data)
                }
            }
            3->{
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

    override fun getProjectList() {
        reqData(2)
        getGroupList()
    }
    override fun getGroupList() {
        reqData(3)
    }

}