package com.toker.sys.view.home.activity.sheet.stteamrank

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.bean.ProjectListBean
import com.toker.sys.view.home.bean.STExtenRankBean
import org.json.JSONObject

/**
 * @author yyx
 */

class STTeamRankPresenter : BasePresenter<STTeamRankContract.View>(), STTeamRankContract.Presenter {
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
                    mView?.showDataList(toBean.data)
                }
            }

            else -> {
                val toBean = GsonUtil.GsonToBean(toJson, ProjectListBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showProjectDate(toBean.data)
                }
            }
        }


    }

    override fun loadRepositories() {
        reqData(1)

    }
    override fun getProjectList(){
        reqData(2)
    }
}