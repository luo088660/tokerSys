package com.toker.sys.view.home.activity.my.projtdeta.viewattendan

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.my.bean.ProjectByTeamBean
import com.toker.sys.view.home.activity.my.bean.ViewAttenListBean1
import com.toker.sys.view.home.activity.my.bean.ViewAttenTeamBean
import com.toker.sys.view.home.bean.ProjectListBean
import org.json.JSONObject

/**
 * @author yyx
 */

class ViewAttendanPresenter : BasePresenter<ViewAttendanContract.View>(),
    ViewAttendanContract.Presenter {
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val toJson = jsonObject.toString()
            when (url_type) {
            1 -> {
                val toBean =
                    GsonUtil.GsonToBean(toJson, ProjectByTeamBean.ProjectTeamBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.byTeamData(toBean.data)
                }
            }
            2 -> {
                val toBean =
                    GsonUtil.GsonToBean(toJson, ViewAttenTeamBean.ViewATeamBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showDataList(toBean.data)
                }
            }
            3 -> {
                val toBean =
                    GsonUtil.GsonToBean(toJson, ViewAttenListBean1.ViewAttenBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.byDayListData(toBean.data)
                }
            }
            4 -> {
                val toBean = GsonUtil.GsonToBean(toJson, ProjectListBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showProjectDate(toBean.data)
                }
            }
            else -> {
            }
        }
    }

    override fun loadRepositories() {
        reqData(1)
        reqData(2)

    }

    override fun ListByDay() {
        reqData(3)
    }

    override fun getProjectList() {
        reqData(4)
    }
}