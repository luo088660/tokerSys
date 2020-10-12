package com.toker.sys.view.home.fragment.task.item.middleLayer.tpend

//import kotlinx.android.synthetic.main.layout_t_entire_02.*
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
import com.toker.sys.dialog.task.ApprTaskResultDialog
import com.toker.sys.dialog.task.ApprovalTaskDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.task.approvallist.ApprovalListActivity
import com.toker.sys.view.home.activity.task.event.AllTaskTypeEvent
import com.toker.sys.view.home.activity.task.event.ApprovalTaskEvent
import com.toker.sys.view.home.activity.task.event.ScrollTopEvent
import com.toker.sys.view.home.fragment.event.TaskHomeEvent
import com.toker.sys.view.home.fragment.task.adapter.TPendAdapter
import com.toker.sys.view.home.fragment.task.bean.Data
import com.toker.sys.view.home.fragment.task.bean.PageData
import kotlinx.android.synthetic.main.fragment_t_pend.*
import kotlinx.android.synthetic.main.fragment_t_received.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 拓客中层
 * 待审批
 * @author yyx
 */

class TPendFragment : BaseFragment<TPendContract.View, TPendPresenter>(), TPendContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): TPendFragment {
            val args = Bundle()
            val fragment = TPendFragment()
            fragment.arguments = args
            return fragment
        }
    }
    var projectId =""
    var taskType =""
    private var pageSize = "1"
    private var tv01: TextView? = null
    private var tv02: TextView? = null
    private var tv03: TextView? = null
    private var tv04: TextView? = null
    private var mBeans = mutableListOf<PageData>()
    override var mPresenter: TPendPresenter = TPendPresenter()

    var adapter: TPendAdapter? = null
    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        LogUtils.d(TAG, "createView:---createView ");
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_t_pend, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        LogUtils.d(TAG, "initView:---initView ");
        EventBus.getDefault().register(this)
//        mPresenter.loadRepositories()
        rv_t_pend.layoutManager = GridLayoutManager(context, 1)
    }

    override fun onStart() {
        super.onStart()
        onFRefresh()
    }

    override fun initData() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_t_pend_011, null)
        tv01 = view.findViewById<TextView>(R.id.tv_pend_01)
        tv02 = view.findViewById<TextView>(R.id.tv_pend_02)
        tv04 = view.findViewById<TextView>(R.id.tv_pend_04)
        tv03 = view.findViewById<TextView>(R.id.tv_pend_03)
        setOnClickListener(tv01!!)
        setOnClickListener(tv02!!)
        setOnClickListener(tv03!!)

        adapter = TPendAdapter(context!!, mBeans)
        rv_t_pend.adapter = adapter
        adapter!!.setHeaderView(view)
//        rv_t_pend.addItemDecoration(getItemDecoration())
        initSpringView(sp_t_pend)
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


    var remark: String = ""
    var result: String = ""
    var tableTag: String = ""
    var taskId: String = ""
    /**
     * 审批任务
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun approvalTaskEvent(event: ApprovalTaskEvent) {
        when (event.type) {
            0 -> {
                //审批结果
//
                this.remark = event.remark
                this.result = event.result
                this.taskId = event.bean!!.id
                this.tableTag = event.bean!!.tableTag
                mPresenter.approveResult()
            }
            1-> ApprovalTaskDialog(context!!, event.bean)
            2->{
                //刷新列表
                onFRefresh()
            }
            else -> {

            }
        }


    }
    //审批成功
    override fun onSuccess() {
        ApprTaskResultDialog(context!!, "审批操作成功")
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            //全部项目
            tv01-> EventBus.getDefault().post(TaskHomeEvent(3))
            //全部任务类型
            tv02 -> AllTaskTypeDialog(context!!)
            //返回审批列表
            tv03 -> {
                val intent = Intent(context, ApprovalListActivity::class.java)
                intent.putExtra("type", 0)
                context?.startActivity(intent)
            }
            else -> {
            }
        }
    }
    //平滑到顶部
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun scrollToPosition(x: ScrollTopEvent) {
        rv_t_pend.scrollToPosition(0)
    }
    //全部项目
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: com.toker.sys.view.home.bean.Data) {
        tv01!!.text = event.projectName
        projectId = event.projectId
        onFRefresh()
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {

        val map = mutableMapOf<String, String>()
        when (url_type) {
            2 -> {
                if (taskType.isNotEmpty()) {
                    map["taskType"] = taskType
                }
                map["tag"] = "3"
                map["status"] = ""
                map["pageSize"] = "10"
                map["page"] = pageSize
                if (projectId.isNotEmpty()) {
                    map["projectId"] = projectId
                }
            }
            3 -> {
                map["taskId"] = taskId
                map["result"] = result
                map["remark"] = remark
                map["tableTag"] = tableTag
            }
            else -> {
            }
        }
        return map
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> {
                TaskManageImp.API_TASK_APPROVE_COUNT
            }
            2 -> TaskManageImp.API_TASK_MIDDLE_TARGET_TASK_PAGE
            3 -> TaskManageImp.API_TASK_APPROVE
            else -> {
                ""
            }
        }


    }

    override fun onFRefresh() {
        //刷新
        pageSize = "1"
        mBeans.clear()
        mPresenter.loadRepositories()

    }

    override fun onFLoadmore() {
        //加载更多
        var toInt = pageSize.toInt()
        toInt++
        pageSize = toInt.toString()
        mPresenter.loadRepositories()

    }


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    override fun showApproveCount(data: Any) {
        tv04!!.text = Html.fromHtml(String.format(getString(R.string.tip_task), data.toString()))

    }

}