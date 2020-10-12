package com.toker.sys.view.home.activity.custom.newcustom

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.custom.bean.ArriveAreaBean
import com.toker.sys.view.home.activity.custom.bean.CityBean
import com.toker.sys.view.home.activity.custom.bean.CustomerRecBean
import com.toker.sys.view.home.activity.custom.bean.ProjectOnCityBean
import com.toker.sys.view.home.bean.ProjectListBean
import org.json.JSONObject

/**
 * @author yyx
 */

class NewCustomPresenter : BasePresenter<NewCustomContract.View>(), NewCustomContract.Presenter {
    override fun onSuccessData(
        jsonObject: JSONObject,
        url_type: Int,
        load_type: Int,
        bundle: Bundle?
    ) {
        val toJson = jsonObject.toString()


        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, Bean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showSuccess(toBean.desc)
                } else {
                    mView?.showError(toBean.desc)
                }
            }
            2 -> {
                val toBean = GsonUtil.GsonToBean(toJson, ProjectListBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showProjectDate(toBean.data)
                }
            }
            3->{
                val toBean = GsonUtil.GsonToBean(toJson, CustomerRecBean.CuRecBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showSuccess(toBean.desc)
                } else {
                    mView?.showError(toBean.desc)
                }

            }
            4->{
                val toBean = GsonUtil.GsonToBean(toJson, CityBean.CityTBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showCityData(toBean.data)
                }
            }
            5->{
                val toBean = GsonUtil.GsonToBean(toJson, ProjectOnCityBean.ProjectCityBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showProjectCityData(toBean.data)
                }
            }
            6->{
                val toBean = GsonUtil.GsonToBean(toJson, ArriveAreaBean.ArriveArBean::class.java)
                if (toBean?.isSuccess()!!) {
                    mView?.showArriveAreaData(toBean.data)
                }
            }
            else -> {
            }
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }

    override fun getProjectList() {
        reqData(2)
    }

    override fun customerRecommend() {
        reqData(3)
    }

    //获取所有省份
    override fun getCities() {
        reqData(4)
    }

    //根据省份ID获取省份下的项目
    override fun getProjectOnCityId() {
        reqData(5)
    }

    //获取联销项目地区
    override fun getArriveArea() {
        reqData(6)
    }

    //获取联销项目地点
    override fun getArriveAddr() {
        reqData(7)
    }
    class Bean(
        val `data`: String,
        val code: String,
        val desc: String
    ) {
        fun isSuccess(): Boolean {
            return code == "1"
        }
    }
}