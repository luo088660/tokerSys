package com.toker.sys.view.home.fragment.my.item.TeamMberTnq.littlebe

import android.os.Bundle
import com.google.gson.Gson
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.my.item.bean.LittleBeBean
import org.json.JSONObject

/**
 * @author yyx
 */

class LittleBePresenter : BasePresenter<LittleBeContract.View>(), LittleBeContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson  = jsonObject.toString()
        val toBean  = GsonUtil.GsonToBean(toJson, LittleBeBean.LittleBean::class.java)
        if (toBean!!.isSuccess()) {
            mView?.showDataList(toBean.data)
        }


    }

    override fun loadRepositories() {
        reqData(1)
    }
}