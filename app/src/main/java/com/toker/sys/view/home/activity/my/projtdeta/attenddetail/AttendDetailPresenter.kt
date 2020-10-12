package com.toker.sys.view.home.activity.my.projtdeta.attenddetail

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.my.bean.AttenDetailBean
import org.json.JSONObject

/**
 * @author yyx
 */

class AttendDetailPresenter : BasePresenter<AttendDetailContract.View>(),
    AttendDetailContract.Presenter {
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {

        val toJson = jsonObject.toString()

        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, AttenDetailBean.AttenDataBean::class.java)
                if (toBean?.isSuccess()!!) {
                    if (toBean?.data!= null) {
                        mView?.showDataList(toBean?.data)
                    }else {
                        mView?.showDataError()
                    }
                }
            }
            else -> {
                val toBean = GsonUtil.GsonToBean(toJson,Bean.DataBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.onSuccessStatus()
                }else{
                    mView?.showError(toBean.desc)

                }
            }
        }


    }

    override fun loadRepositories() {
        reqData(1)
    }

    override fun updateAttendanceStatus() {
        reqData(2)
    }

   class Bean {
        data class DataBean(
            val `data`: String,
            val code: String,
            val desc: String
        ){
            fun isSuccess(): Boolean {
                return code == "1"
            }
        }
    }
}