package com.toker.sys.view.register.enternewpwd

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.my.bean.PWdManageBean
import org.json.JSONObject

/**
 * @author yyx
 */

class EnterNewPwdPresenter : BasePresenter<EnterNewPwdContract.View>(), EnterNewPwdContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(toJson, PWdManageBean.PWdManBean::class.java)
        if (toBean?.isSuccess()!!) {
            mView?.showSeccess()
        }else{
            mView?.showError(toBean.desc)
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }
}