package com.toker.sys.view.home.activity.my.projtdeta

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.activity.my.bean.ProjectByTeamBean
import com.toker.sys.view.home.activity.my.bean.ProjectFormanBean
import com.toker.sys.view.home.activity.my.bean.SchedMonthBean
import org.json.JSONObject

/**
 * @author yyx
 */

class ProjtDetaPresenter : BasePresenter<ProjtDetaContract.View>(), ProjtDetaContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {

        val toJson = jsonObject.toString()
        when (url_type) {
            1 -> {
                val toBean = GsonUtil.GsonToBean(toJson, SchedMonthBean.Bean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showDataSched(toBean.data)
                }else{
                    mView?.onErrorSched(toBean.desc)
                }
            }
            2 -> {
                val toBean = GsonUtil.GsonToBean(toJson, ProjectByTeamBean.ProjectTeamBean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.byTeamData(toBean.data)
                }
            }
            3 -> {
                val toBean = GsonUtil.GsonToBean(toJson, ProjectFormanBean.ProjectForBean::class.java)
                if (toBean!!.isSuccess()&&null != toBean.data) {
                    mView?.preForMan(toBean.data)
                }else{
                    mView?.preErrorForMan()
                }
            }
            4 -> {
                val toBean = GsonUtil.GsonToBean(toJson, GroupAdmin.Bean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showGroupAdmin(toBean.data.userName)
                }
            }
            5 -> {
                val toBean = GsonUtil.GsonToBean(toJson, CountGroup.Bean::class.java)
                if (toBean!!.isSuccess()) {
                    mView?.showCountGroup(toBean.data)
                }
            }
            else -> {
            }
        }

    }

    override fun loadRepositories() {
        schedulingByMonth()
        AttendanceByTeam()

        getMyPerformance()
//        4.1.2.	根据项目id查询拓客管理员
        reqData(4)
//        4.1.5.	统计拓客团队信息数量
        reqData(5)
    }
    //排班计划
    override fun schedulingByMonth(){
        reqData(1)
    }
    //考勤统计
    override fun AttendanceByTeam(){
        reqData(2)
    }
    //当前用户的业绩查询
    override fun getMyPerformance(){
        reqData(3)
    }


    class GroupAdmin {
        data class Bean(
            val `data`: Data,
            val code: String,
            val desc: String
        ) {
            fun isSuccess(): Boolean {
                return code == "1"
            }
        }


        data class Data(
            val userName: String
        )
    }

    class CountGroup {
        data class Bean(
            val `data`: Data,
            val code: String,
            val desc: String
        ) {
            fun isSuccess(): Boolean {
                return code == "1"
            }
        }

        data class Data(
            val beeSum: Int,
            val groupSum: Int,
            val userSum: Int
        )
    }
}