package com.toker.sys.view.home.activity.custom.fcustomdetail

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import com.toker.sys.view.home.fragment.custom.bean.FollowCBean
import com.toker.sys.view.home.fragment.custom.bean.SelectUserList
import org.json.JSONObject

/**
 * @author yyx
 */

class FCustomDetailPresenter : BasePresenter<FCustomDetailContract.View>(), FCustomDetailContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()
        when (url_type) {
            1-> {
//                val toBean = GsonUtil.GsonToBean(toJson, FCustomDetailBean::class.java)
                val toBean = GsonUtil.GsonToBean(toJson, FCustomDetailTBean.Bean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showData(toBean.data)
                }
            }
            2 -> {
                val toBean = GsonUtil.GsonToBean(toJson, FollowCBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showAllocation()
                }else{
                    mView?.showErrorData(toBean.desc)
                }
            }
            3 -> {
                val toBean = GsonUtil.GsonToBean(toJson, SelectUserList.SelectUserBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showUserList(toBean.data.pageData)
                }
            }
            4->{

                val toBean = GsonUtil.GsonToBean(toJson, FCustomDBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.customerYDetail(toBean.data)
                }

            }

        }


//        val msg = jsonObject.optString("msg")
//        val status = jsonObject.optInt("status")
////        var user = Util_0.parseObject(jsonObject, User::class.java, "data") ?: User()
//        mView?.onSuccessData(url_type,load_type,msg,status)
    }

    override fun loadRepositories() {
        reqData(1)
    }

    override fun publicPoCust() {
        reqData(2)
    }

    override fun selectUserEvent() {
        reqData(3)

    }

    override fun customerYDetail() {
        reqData(4)
    }
}