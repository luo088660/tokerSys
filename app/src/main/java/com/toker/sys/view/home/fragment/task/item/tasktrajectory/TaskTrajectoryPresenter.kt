package com.toker.sys.view.home.fragment.task.item.tasktrajectory

import android.os.Bundle
import com.toker.sys.mvp.base.BasePresenter
import com.toker.sys.utils.tools.GsonUtil
import com.toker.sys.view.home.fragment.task.bean.TaskTrajectoryBean
import org.json.JSONObject

/**
 * @author yyx
 */

class TaskTrajectoryPresenter : BasePresenter<TaskTrajectoryContract.View>(), TaskTrajectoryContract.Presenter {
    override fun onSuccessData(jsonObject: JSONObject, url_type: Int, load_type: Int, bundle: Bundle?) {


        val toJson = jsonObject.toString()
        val toBean = GsonUtil.GsonToBean(toJson, TaskTrajectoryBean.TrajectoryBean::class.java)
        if (toBean?.isSuccess()!!) {
            mView?.showData(toBean.data)
        }
    }

    override fun loadRepositories() {
        reqData(1)
    }
}