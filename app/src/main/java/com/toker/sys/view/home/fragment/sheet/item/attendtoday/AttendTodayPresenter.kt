package com.toker.sys.view.home.fragment.sheet.item.attendtoday

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.my.item.myatten.bean.MeAttenBean
import org.json.JSONObject

/**
 * @author yyx
 */

class AttendTodayPresenter : BasePresenter<AttendTodayContract.View>(),
    AttendTodayContract.Presenter {
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {

        val toJson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(toJson, MeAttenBean.MeAttenTBean::class.java)
        if (toBean!!.isSuccess()) {
            mView?.showData(toBean.data)
        } else {
            mView?.onShowError(toBean.desc)
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }
}