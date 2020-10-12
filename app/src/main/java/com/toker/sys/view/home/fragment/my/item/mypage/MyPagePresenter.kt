package com.toker.sys.view.home.fragment.my.item.mypage

import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.my.item.bean.GroupListBean
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean
import com.toker.sys.view.home.fragment.my.item.bean.ReadCountBean
import org.json.JSONObject

/**
 * @author yyx
 */

class MyPagePresenter : BasePresenter<MyPageContract.View>(), MyPageContract.Presenter {
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, ProjectListBean.ProjectBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showListData(toBean.data)
                }
            }
            3 -> {
                val toBean = GsonUtil.GsonToBean(toJson, ReadCountBean.Bean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showDataNumBer(toBean.data)
                }else{
                    mView?.showDataNumBer(ReadCountBean.Data(0))
                }
            }
            else -> {
            }
        }


    }

    override fun loadRepositories() {
        reqData()
    }

    //获取我的项目
    override fun ProjectList() {
        reqData(1)
    }
    //未读消息统计
    override fun noReadCount() {
        reqData(3)
    }

}