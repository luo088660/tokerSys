package com.toker.sys.view.home.fragment.task.item.teamLeader.tstart

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.dialog.task.TaskCategorieDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.task.event.ScrollTopEvent
import com.toker.sys.view.home.activity.task.event.TaskCategorieEvent
import com.toker.sys.view.home.fragment.event.TaskCustEvent
import com.toker.sys.view.home.fragment.task.adapter.TRTaskPageAdapter
import com.toker.sys.view.home.fragment.task.adapter.TStartAdapter
import com.toker.sys.view.home.fragment.task.bean.Data
import com.toker.sys.view.home.fragment.task.bean.PageData
import kotlinx.android.synthetic.main.fragment_t_received.*
import kotlinx.android.synthetic.main.fragment_t_start.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 拓客员
 * 拓客组长
 *
 * 已完成
 * 未开始
 *
 * @author yyx
 */

class TStartFragment : BaseFragment<TStartContract.View, TStartPresenter>(), TStartContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): TStartFragment {
            val args = Bundle()
            val fragment = TStartFragment()
            fragment.arguments = args
            return fragment
        }
    }

    //TODO  2 未开始 or  3 已完成
    private var type = "2"
    //TODO type 1 指标型 or 2 事务型
    var typeStuta = 1
    private var tv01: TextView? = null
    private var tv011: TextView? = null
    private var tv02: TextView? = null
    private var tv03: TextView? = null
    var adapter: TStartAdapter? = null
    var tadapter: TRTaskPageAdapter? = null
    var taskName = ""
    private var mBeans = mutableListOf<PageData>()

    var pageData1 =
        mutableListOf<com.toker.sys.view.home.fragment.task.item.controller.treceived.PageData>()
    private var pageSize = "1"
    override var mPresenter: TStartPresenter = TStartPresenter()
    //TODO taskType false 我的任务 true 团队任务
    private var taskType = false

    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_t_start, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        taskType = arguments!!.getBoolean(Constants.TASKTYPE)
        type = arguments!!.getString("type")


    }

    override fun onStart() {
        super.onStart()
        mPresenter.loadRepositories(typeStuta)
    }

    override fun initData() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_t_start01, null)
        tv011 = view.findViewById<TextView>(R.id.tv_current_01)
        tv01 = view.findViewById<TextView>(R.id.tv_t_start_01)
        tv02 = view.findViewById<TextView>(R.id.tv_t_start_02)
        tv03 = view.findViewById<TextView>(R.id.tv_t_start_03)

        adapter = TStartAdapter(context!!, mBeans)
        tadapter = TRTaskPageAdapter(context!!, pageData1, taskType, true)
        rv_t_start.layoutManager = GridLayoutManager(context, 1)
        rv_t_start.adapter = adapter
        adapter!!.setHeaderView(view)
        tadapter!!.setHeaderView(view)
        rv_t_start.addItemDecoration(getItemDecoration())
        initSpringView(sp_t_start)
        setOnClickListener(tv011!!)
        setOnClickListener(tv02!!)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            //选择任务类型
            tv011 -> {
                TaskCategorieDialog(context!!)
            }
            //查询
            tv02 -> {
                taskName = "${tv01!!.text}"
                onFRefresh()
            }
            else -> {
            }
        }
    }

    //选择任务类型
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun TaskCategorieEvent(event: TaskCategorieEvent) {
        tv011!!.text = event.name
        this.typeStuta = event.type
        tv03!!.text = if (type.toInt() == 2) Html.fromHtml(
            String.format(
                getString(R.string.tip_start_02),
                "0"
            )
        ) else
            Html.fromHtml(String.format(getString(R.string.tip_start_03), "0"))
        onFRefresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun TaskCustEvent(event: TaskCustEvent) {
        //TODO taskType false 我的任务 true 团队任务
        taskType = event.isType
        onFRefresh()

    }

    override fun onFLoadmore() {
        super.onFLoadmore()
        //加载更多
        var toInt = pageSize.toInt()
        toInt++
        pageSize = toInt.toString()
        mPresenter.loadRepositories(typeStuta)

    }

    override fun onFRefresh() {
        super.onFRefresh()
        if (mBeans.isNotEmpty()) {
            mBeans.clear()
        }
        if (pageData1.isNotEmpty()) {
            pageData1.clear()
        }
        //刷新
        pageSize = "1"
        mPresenter.loadRepositories(typeStuta)

    }

    override fun targetTaskPage(data: Data) {
        mBeans.addAll(data.pageData)
        if (pageSize == "1") {
            rv_t_start.adapter = adapter
        }

        adapter!!.refreshData(mBeans)
        tv03!!.text = if (type.toInt() == 2) Html.fromHtml(
            String.format(
                getString(R.string.tip_start_02),
                data.rowTotal
            )
        ) else
            Html.fromHtml(String.format(getString(R.string.tip_start_03), data.rowTotal))


    }
    //平滑到顶部
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun scrollToPosition(x: ScrollTopEvent) {
        rv_t_start.scrollToPosition(0)
    }
    /**
     * 事务型指标
     */
    override fun eventTaskPage(pageData: com.toker.sys.view.home.fragment.task.item.controller.treceived.Data) {

//        if (pageData.pageData.isNotEmpty()) {
            pageData1.addAll(pageData.pageData)
            if (pageSize == "1") {
                rv_t_start.adapter = tadapter
            }
//        }
        tv03!!.text = Html.fromHtml(
            String.format(
                getString(if (type.toInt() == 2) R.string.tip_start_02 else R.string.tip_start_03),
                pageData.rowTotal
            )
        )
        LogUtils.d(TAG, "pageData1:$pageData1 ");
        tadapter!!.refreshData(pageData1, taskType)
    }


    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            //团队 个人 指标型任务列表
            1 -> if (taskType) TaskManageImp.API_TASK_GROUP_TARGET_TASK_PAGE else TaskManageImp.API_TASK_PERSON_TARGET_TASKPAGE
            2 -> if (taskType) TaskManageImp.API_TASK_GROUP_EVENT_TASK_PAGE else TaskManageImp.API_TASK_PERSON_EVENT_TASK_PAGE
            else -> {
                ""
            }
        }
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {

        val map = mutableMapOf<String, String>()
        if (taskName.isNotEmpty()) {
            map["taskName"] = taskName
        }
        when (url_type) {
            1 -> {

                map["tag"] = type
                map["status"] = ""
                map["pageSize"] = "10"
                map["page"] = pageSize
            }
            else -> {
                map["status"] = if (type.toInt() == 2) "1" else "3"
                map["pageSize"] = "10"
                map["page"] = pageSize
            }
        }
        return map
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }


    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}// Required empty public constructor