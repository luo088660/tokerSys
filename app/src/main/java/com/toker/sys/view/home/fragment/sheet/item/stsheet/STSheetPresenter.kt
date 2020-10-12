package com.toker.sys.view.home.fragment.sheet.item.stsheet

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.my.bean.ProjectFormanBean
import com.toker.sys.view.home.bean.ProjectListBean
import com.toker.sys.view.home.fragment.my.item.bean.MainCountBean
import com.toker.sys.view.home.fragment.my.item.bean.ReadCountBean
import org.json.JSONObject

/**
 * @author yyx
 */

class STSheetPresenter : BasePresenter<STSheetContract.View>(), STSheetContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, ProjectFormanBean.ProjectForBean::class.java)
                if (toBean!!.isSuccess() &&toBean.data != null) {
                    mView?.preForMan(toBean.data)
                }else{
                    mView?.preErrorForMan()
                }
            }
            2 -> {
                val toBean = GsonUtil.GsonToBean(toJson, ReadCountBean.Bean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showDataNumBer(toBean.data)
                }else{
                    mView?.showDataNumBer(ReadCountBean.Data(0))
                }
            }
            3->{
                val toBean = GsonUtil.GsonToBean(toJson, MainCountBean.MainCBean::class.java)
                if (toBean?.isSuccess()!!){
                    mView?.showDataCount(toBean.data)
                }
            }
            else -> {
            }
        }
    }

    override fun getMyPerformance() {
        //TODO 我的业绩查询
        reqData(1)
    }
    override fun loadRepositories() {
        getMyPerformance()
        //TODO 未读信息统计
        reqData(2)
        //TODO 获取首页统计数据
        reqData(3)
    }
}