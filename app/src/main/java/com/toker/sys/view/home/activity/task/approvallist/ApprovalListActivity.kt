package com.toker.sys.view.home.activity.task.approvallist

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.view.View
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.dialog.ProjectListDialog
import com.toker.sys.dialog.task.AllTaskTypeDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.view.home.activity.adapter.ApprovalListAdapter
import com.toker.sys.view.home.activity.task.event.AllTaskTypeEvent
import com.toker.sys.view.home.fragment.task.bean.Data
import com.toker.sys.view.home.fragment.task.bean.PageData
import kotlinx.android.synthetic.main.activity_approval_list.*
import kotlinx.android.synthetic.main.activity_approval_list.sp_team_rank
import kotlinx.android.synthetic.main.activity_s_t_team_rank.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_t_pend_011.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 审批记录
 * 不通过的任务
 * @author yyx
 */

class ApprovalListActivity : BaseActivity<ApprovalListContract.View, ApprovalListPresenter>(),
    ApprovalListContract.View {

    override var mPresenter: ApprovalListPresenter = ApprovalListPresenter()

    private var data: MutableList<com.toker.sys.view.home.bean.Data>? = null
    override fun layoutResID(): Int = R.layout.activity_approval_list

    private var page = 1
    private var type = 0
    private var taskType = ""
    private var projectId = ""
    var adapter: ApprovalListAdapter? = null
    private var mBeans = mutableListOf<PageData>()

    override fun initView() {
        type = intent.getIntExtra("type", 0)
        EventBus.getDefault().register(this)

        tv_title.text = if (type == 0) "历史审批记录" else "任务"
        tv_pend_03.text = "返回审批中的列表"
        mPresenter.loadRepositories()
        mPresenter.getProjectList()
    }

    override fun initData() {

        rv_t_approval_list.layoutManager = GridLayoutManager(this, 1)

        adapter = ApprovalListAdapter(this, mBeans)
        rv_t_approval_list.adapter = adapter
        rv_t_approval_list.addItemDecoration(getItemDecoration())


//        ll_t_entire_01.visibility = VISIBLE
        tv_pend_03.visibility = GONE
        setOnClickListener(tv_pend_01)
        setOnClickListener(tv_pend_02)
        setOnClickListener(tv_pend_03)
        setOnClickListener(img_back)
//        setOnClickListener(tv_entire_see_deta_02)
        initSpringView(sp_team_rank)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            //全部项目
            tv_pend_01 -> {
                ProjectListDialog(this, data!!)
            }
            //全部任务类型
            tv_pend_02 -> {
                AllTaskTypeDialog(this)
            }
            img_back,
            tv_pend_03 -> {
                finish()
            }
            //指标考核任务
//            tv_entire_see_deta_02 -> startActivity(Intent(this, MonthIndicAssActivity::class.java))

            else -> {
            }
        }
    }
    //全部项目
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: com.toker.sys.view.home.bean.Data) {
        tv_pend_01!!.text = event.projectName
        projectId = event.projectId
        onFRefresh()
    }

    override fun onFRefresh() {
        super.onFRefresh()
        this.page = 1
        mBeans.clear()
        mPresenter.loadRepositories()
    }

    override fun onFLoadmore() {
        super.onFLoadmore()
        this.page++
        mPresenter.loadRepositories()
    }

    override fun targetTaskPage(data: Data) {
        mBeans.addAll(data.pageData)
        adapter!!.refreshData(mBeans)

        tv_pend_04.text = Html.fromHtml(String.format(getString(R.string.tip_task), data.rowTotal))
    }

    override fun showProjectDate(data: MutableList<com.toker.sys.view.home.bean.Data>) {
        this.data = data
    }

    /**
     * 全部任务类型
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun AllTaskTypeEvent(event: AllTaskTypeEvent) {
        when (event.type) {
            3 -> {
                tv_pend_02!!.text = event.name
                taskType = ""
            }
            else -> {
                tv_pend_02!!.text = "${event.name}任务"
                taskType = "${event.type}"
            }
        }
        onFRefresh()
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> {
                if (AppApplication.TYPE == Constants.RESCUE1) TaskManageImp.API_TASK_MIDDLE_TARGET_TASK_PAGE
                else TaskManageImp.API_TASK_MANAGER_TARGET_TASKPAGE
            }
            2 -> SystemSettImp.API_SYSTEM_PROJECT_LIST
            else -> {
                ""
            }
        }
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {

        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                if (taskType.isNotEmpty()) {
                    map["taskType"] = taskType
                }
                if (projectId.isNotEmpty()) {
                    map["projectId"] = projectId
                }
                map["tag"] = "4"
                map["pageSize"] = "10"
                map["page"] = "$page"
            }
            else -> {
            }
        }
        return map
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
