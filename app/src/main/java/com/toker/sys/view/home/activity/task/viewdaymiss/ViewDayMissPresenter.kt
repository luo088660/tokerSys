package com.toker.sys.view.home.activity.task.viewdaymiss

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.task.bean.ViewDayMissBean
import org.json.JSONObject

/**
 * @author yyx
 */

class ViewDayMissPresenter : BasePresenter<ViewDayMissContract.View>(), ViewDayMissContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {
        val toJson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(toJson, ViewDayMissBean.ViewDaYMissBean::class.java)
        if (toBean!!.isSuccess()) {
            mView?.showData(toBean.data)
        }

    }

    override fun loadRepositories() {
        reqData(1)
    }

    override fun textChangerd(): TextWatcher {
        return object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                mView?.afterTextChanged()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                
            }

        }
        
    }
}