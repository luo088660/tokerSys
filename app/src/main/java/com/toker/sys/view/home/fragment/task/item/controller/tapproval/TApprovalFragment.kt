package com.toker.sys.view.home.fragment.task.item.controller.tapproval

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.dialog.task.AllTaskTypeDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.view.home.activity.task.approvallist.ApprovalListActivity
import com.toker.sys.view.home.activity.task.event.AllTaskTypeEvent
import com.toker.sys.view.home.activity.task.event.ScrollTopEvent
import com.toker.sys.view.home.fragment.event.TaskHomeEvent
import com.toker.sys.view.home.fragment.task.adapter.TApprovalAdapter
import com.toker.sys.view.home.fragment.task.bean.Data
import com.toker.sys.view.home.fragment.task.bean.PageData
import kotlinx.android.synthetic.main.fragment_t_approval.*
import kotlinx.android.synthetic.main.fragment_t_current.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 拓客管理员
 * 审批中
 * @author yyx
 */

class TApprovalFragment : BaseFragment<TApprovalContract.View, TApprovalPresenter>(), TApprovalContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): TApprovalFragment {
            val args = Bundle()
            val fragment = TApprovalFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var pageSize = "1"

    private var tv02: TextView? = null
    private var tv01: TextView? = null
    private var tv03: TextView? = null
    private var tv04: TextView? = null
    var adapter: TApprovalAdapter? = null
    var taskType = ""
    var projectId = ""
    private var mBeans = mutableListOf<PageData>()

    override var mPresenter: TApprovalPresenter = TApprovalPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_t_approval, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        mPresenter.loadRepositories()
    }

    override fun initData() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_t_pend_011, null)
        tv01 = view.findViewById<TextView>(R.id.tv_pend_01)
        tv02 = view.findViewById<TextView>(R.id.tv_pend_02)
        tv03 = view.findViewById<TextView>(R.id.tv_pend_03)
        tv04 = view.findViewById<TextView>(R.id.tv_pend_04)


        rv_t_approval.layoutManager = GridLayoutManager(context, 1)

        adapter = TApprovalAdapter(context!!, mBeans)
        rv_t_approval.adapter = adapter
        rv_t_approval.addItemDecoration(getItemDecoration())
        adapter!!.setHeaderView(view)
        initSpringView(sp_t_approval)


        setOnClickListener(tv01!!)
        setOnClickListener(tv02!!)
        setOnClickListener(tv03!!)
    }
    override fun onFLoadmore() {
        super.onFLoadmore()
        //加载更多
        var toInt = pageSize.toInt()
        toInt++
        pageSize = toInt.toString()
        mPresenter.loadRepositories()

    }

    override fun onFRefresh() {
        super.onFRefresh()
        //刷新
        pageSize = "1"
        mPresenter.loadRepositories()
        mBeans.clear()
    }

    //平滑到顶部
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun scrollToPosition(x: ScrollTopEvent) {
        rv_t_approval.scrollToPosition(0)
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            //全部项目
            R.id.tv_pend_01 -> {
                EventBus.getDefault().post(TaskHomeEvent(3))
            }
            //全部任务类型
            R.id.tv_pend_02 -> {
                AllTaskTypeDialog(context!!)
            }
            //审批不通过的任务
            R.id.tv_pend_03 -> {
                val intent = Intent(context, ApprovalListActivity::class.java)
                intent.putExtra("type", 1)
                context?.startActivity(intent)
            }
            else -> {
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: com.toker.sys.view.home.bean.Data) {
        tv01!!.text = event.projectName
        projectId = event.projectId
        onFRefresh()
    }
    override fun targetTaskPage(data: Data) {
        mBeans.addAll(data.pageData)
        adapter!!.refreshData(mBeans)

        tv04!!.text = Html.fromHtml(String.format(getString(R.string.tip_task), data.rowTotal))
    }

    /**
     * 全部任务类型
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun AllTaskTypeEvent(event: AllTaskTypeEvent) {
        when (event.type) {
            3 -> {
                tv02!!.text = event.name
                taskType = ""
            }
            else -> {
                tv02!!.text = "${event.name}任务"
                taskType = "${event.type}"
            }
        }
        onFRefresh()
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> {
                TaskManageImp.API_TASK_MANAGER_TARGET_TASKPAGE
            }
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
                map["tag"] = "3"
                map["pageSize"] ="10"
                map["page"] = pageSize
            }
            else -> {
            }
        }
        return map
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}// Required empty public constructor