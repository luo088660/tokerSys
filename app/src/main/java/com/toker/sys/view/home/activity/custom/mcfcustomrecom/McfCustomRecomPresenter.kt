package com.toker.sys.view.home.activity.custom.mcfcustomrecom

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import com.toker.sys.view.home.activity.custom.bean.MCFcustomBean
import com.toker.sys.view.home.activity.custom.newcustom.NewCustomPresenter
import com.toker.sys.view.home.bean.ProjectListBean
import org.json.JSONObject

/**
 * @author yyx
 */

class McfCustomRecomPresenter : BasePresenter<McfCustomRecomContract.View>(),
    McfCustomRecomContract.Presenter {

    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, FCustomDetailTBean.Bean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showData(toBean.data)
                }
            }
            2 -> {
                val toBean = GsonUtil.GsonToBean(toJson, MCFcustomBean.Bean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showDatarActivate()
                } else {
                    mView?.showErrorrActivate(toBean.desc)
                }
            }
            4 -> {
                val toBean = GsonUtil.GsonToBean(toJson, ProjectListBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showProjectDate(toBean.data)
                }
            }
            3->{
                val toBean = GsonUtil.GsonToBean(toJson, NewCustomPresenter.Bean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showSuccess(toBean.desc)
                } else {
                    mView?.showError(toBean.desc)
                }
            }
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }

    override fun customerActivate() {
        reqData(2)
    }

    override fun customerRecommend() {
        reqData(3)
    }

    override fun showProjectDate() {
        reqData(4)
    }


}