package com.toker.sys.view.home.fragment.my.item.mynews.unreadnews

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.my.item.bean.MsgBean
import com.toker.sys.view.home.fragment.my.item.bean.UnreadNewsBean
import org.json.JSONObject

/**
 * @author yyx
 */

class UnreadNewsPresenter : BasePresenter<UnreadNewsContract.View>(), UnreadNewsContract.Presenter {
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, UnreadNewsBean.NewsBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showDataList(toBean.data)
                }
            }
            2->{
                val toBean = GsonUtil.GsonToBean(toJson, MsgBean.Bena::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.noReadCount(1)
                }else{
                    mView?.noReadCount(0)
                }
            }
            else -> {
                val toBean = GsonUtil.GsonToBean(toJson, MsgBean.Bena::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showDataSuccess(toBean.desc)
                }
            }
        }

    }

    override fun loadRepositories() {
        reqData(1)
    }

    override fun readMsg() {
        reqData(2)
    }
    override fun noSendTraceMsg() {
        reqData(3)
    }
}