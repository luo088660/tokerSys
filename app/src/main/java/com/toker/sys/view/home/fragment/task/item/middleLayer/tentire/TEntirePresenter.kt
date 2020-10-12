package com.toker.sys.view.home.fragment.task.item.middleLayer.tentire

import android.os.Bundle
import android.util.Log
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.task.bean.TEntireBean
import org.json.JSONObject

/**
 * @author yyx
 */

class TEntirePresenter : BasePresenter<TEntireContract.View>(), TEntireContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()
        Log.e(TAG, "toJson: $toJson");
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, TEntireBean::class.java)
                if (toBean!!.isSuccess()){
                    mView?.targetTaskPage(toBean.data)
                }

            }
            else -> {
            }
        }

    }

    override fun loadRepositories() {
        reqData(1)
    }
}