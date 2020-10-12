package com.toker.sys.view.home.activity.my.teammem

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.my.bean.GroupProjectBean
import com.toker.sys.view.home.bean.ProjectListBean
import com.toker.sys.view.home.fragment.my.item.bean.ExtensionBean
import com.toker.sys.view.home.fragment.my.item.bean.LittleBeBean
import org.json.JSONObject

/**
 * @author yyx
 */

class TeamMemPresenter : BasePresenter<TeamMemContract.View>(), TeamMemContract.Presenter {


    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, ExtensionBean.ExtensiBean::class.java)

                if (toBean!!.isSuccess()) {
                    mView?.showEDataList(toBean.data)
                }
            }
            2 -> {
                val toBean  = GsonUtil.GsonToBean(toJson, LittleBeBean.LittleBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showLDataList(toBean.data)
                }

            }
            3->{
                val toBean  = GsonUtil.GsonToBean(toJson, GroupProjectBean.GroupPjtBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showGroupProject(toBean.data)
                }
            }
            4->{
                val toBean = GsonUtil.GsonToBean(toJson, ProjectListBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showProjectDate(toBean.data)
                }
            }

            else -> {
            }
        }


    }
    override fun getGroupByProjectId() {
        reqData(3)
    }
    override fun loadRepositories() {
        getGroupByProjectId()
        reqData(4)
    }
    override fun loadRepositories(type: Int) {
        reqData(type + 1)
    }
}