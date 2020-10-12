package com.toker.sys.view.home.fragment.custom.item.publicpocust

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.custom.bean.FollowCBean
import com.toker.sys.view.home.fragment.custom.bean.FollowCustBean
import com.toker.sys.view.home.fragment.custom.bean.SelectUserList
import org.json.JSONObject

/**
 * @author yyx
 */

class PublicPoCustPresenter : BasePresenter<PublicPoCustContract.View>(), PublicPoCustContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()


        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, FollowCustBean.FollowCtBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showData(toBean.data)
                }
            }
            2 -> {
                val toBean = GsonUtil.GsonToBean(toJson, SelectUserList.SelectUserBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showUserList(toBean.data.pageData)
                }
            }
            3->{
                val toBean = GsonUtil.GsonToBean(toJson, FollowCBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showAllocation()
                }else{
                    mView?.showAllocationError(toBean.desc)
                }
            }

        }

//        val msg = jsonObject.optString("msg")
//        val status = jsonObject.optInt("status")
//        var user = Util_0.parseObject(jsonObject, User::class.java, "data") ?: User()
//        mView?.onSuccessData(url_type,load_type,msg,status)
    }

    override fun loadRepositories() {
        reqData(1)
    }

    override fun selectUserEvent() {
        reqData(2)
    }

    override fun publicPoCust() {
        reqData(3)
    }
}