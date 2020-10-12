package com.toker.sys.view.home.activity.my.teammem

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.dialog.ProjectListDialog
import com.toker.sys.dialog.my.GroupPjtDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.my.bean.GroupProjectBean
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.fragment.event.TaskEvent
import com.toker.sys.view.home.fragment.my.item.adapter.ExtensionAdapter
import com.toker.sys.view.home.fragment.my.item.adapter.LittleBeeAdapter
import com.toker.sys.view.home.fragment.my.item.bean.ExtensionBean
import com.toker.sys.view.home.fragment.my.item.bean.LittleBeBean
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean
import com.toker.sys.view.home.fragment.task.adapter.TaskAdapter
import kotlinx.android.synthetic.main.activity_team_mem.*
import kotlinx.android.synthetic.main.activity_team_mem.sp_t_approval
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 团队成员
 *  团队业绩查询，拓客员业绩查询
 * @author yyx
 */

class TeamMemActivity : BaseActivity<TeamMemContract.View, TeamMemPresenter>(),
    TeamMemContract.View {
    private var EmBeans: MutableList<ExtensionBean.PageData> = mutableListOf()
    private var LmBeans: MutableList<LittleBeBean.PageData> = mutableListOf()
    private var groupPjtBeans: MutableList<GroupProjectBean.Data> = mutableListOf()
    private var eAdapter: ExtensionAdapter? = null
    private var lbAdapter: LittleBeeAdapter? = null
    override var mPresenter: TeamMemPresenter = TeamMemPresenter()
    var btnTM: TextView? = null
    var btnRece: TextView? = null
    var tvPjt: TextView? = null
    var edRece: EditText? = null
    var ll01: LinearLayout? = null
    var ll02: LinearLayout? = null
    var condition = ""
    var page = 1
    var pjtData: MutableList<Data>? = null
    private var type = 0//0拓客员 1 小蜜蜂
    private var data: ProjectListBean.Data? = null
    override fun layoutResID(): Int = R.layout.activity_team_mem
    private var groupId = ""
    private var projectId = ""
    override fun initView() {
        type = intent.getIntExtra("type", 0)
        LogUtils.d(TAG, "type:$type ");
        data = intent.getSerializableExtra("data") as ProjectListBean.Data
        EventBus.getDefault().register(this)
        tv_title.text = "团队成员查询"
        projectId =  data?.projectId!!
    }

    override fun initData() {
        val view = LayoutInflater.from(this).inflate(R.layout.layout_team_member_01, null)
        val rvTitle = view.findViewById<RecyclerView>(R.id.rv_team_mber)
        edRece = view.findViewById<EditText>(R.id.tv_receivde1_01)
        btnRece = view.findViewById<TextView>(R.id.tv_receivde1_02)
        btnTM = view.findViewById<TextView>(R.id.btn_team_mber)
        ll01 = view.findViewById<LinearLayout>(R.id.ll_team_mem_01)
        ll02 = view.findViewById<LinearLayout>(R.id.ll_team_mem_02)
        tvPjt = view.findViewById<TextView>(R.id.tv_team_pjt)
        val mBeans = resources.getStringArray(R.array.team_m_ber)
        rvTitle.layoutManager = GridLayoutManager(this, 2)
        val taskAdapter = TaskAdapter(this, mBeans)
        rvTitle.adapter = taskAdapter

        eAdapter = ExtensionAdapter(this, EmBeans)
        lbAdapter = LittleBeeAdapter(this, LmBeans)
        taskAdapter.upDataView(type)
        rv_team_mem.layoutManager = GridLayoutManager(this, 1)


        refreshData()

        eAdapter?.setHeaderView(view)
        lbAdapter?.setHeaderView(view)
        setOnClickListener(img_back)
        setOnClickListener(btnRece!!)
        setOnClickListener(btnTM!!)
        setOnClickListener(tvPjt!!)
        initSpringView(sp_t_approval)
        mPresenter.loadRepositories(type)
        mPresenter.loadRepositories()

        tvPjt?.text = data?.projectName
    }


    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun TaskEvent(event: TaskEvent) {
        when (event.name) {
            //拓客员
            resources.getString(R.string.tip_extension) -> {
                type = 0
                LmBeans.clear()
            }
            //小蜜蜂
            else -> {
                type = 1
                EmBeans.clear()

            }

        }
        groupId = ""
        edRece!!.text.clear()
        condition = ""
        page = 1
        refreshData()
        mPresenter.loadRepositories(type)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            //查询
            btnRece -> {
                condition = edRece!!.text.toString()
                EmBeans.clear()
                LmBeans.clear()
                mPresenter.loadRepositories(type)
            }
            //获取小组信息
            btnTM->{
                GroupPjtDialog(this,groupPjtBeans)
            }

            tvPjt -> ProjectListDialog(this, pjtData!!)

            else -> {
            }
        }
    }

    /**
     * 获取小组
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun groupProjectBean(event :GroupProjectBean.Data){
        btnTM!!.text = event.groupName
        groupId = event.groupId
        EmBeans.clear()
//        mPresenter.loadRepositories(type)
        onFRefresh()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun dataProject(event: com.toker.sys.view.home.bean.Data){
        btnTM!!.text ="全部"
        tvPjt!!.text = event.projectName
        groupId = ""
        projectId = event.projectId
        EmBeans.clear()
        groupPjtBeans.clear()
        mPresenter.getGroupByProjectId()
        Log.e(TAG, "Type: $type" );
        onFRefresh()
    }


    private fun refreshData() {
        edRece?.hint = if (type == 0) "按姓名和手机号查询" else "输入拓客员姓名查询"
        rv_team_mem.adapter = if (type == 0) eAdapter else lbAdapter
        ll01?.visibility = if (type == 0) VISIBLE else GONE
        btnTM?.visibility = if (type == 0) VISIBLE else GONE
        ll02?.visibility = if (type == 0) GONE else VISIBLE
    }

    /**
     * 拓客员
     */
    override fun showEDataList(data: ExtensionBean.Data) {
        EmBeans.addAll(data.pageData)
        eAdapter?.refreshData(EmBeans)
    }

    /**
     * 小蜜蜂
     */
    override fun showLDataList(data: LittleBeBean.Data) {
        LmBeans.addAll(data.pageData)
        lbAdapter?.refreshData(LmBeans)

    }

    override fun showGroupProject(data: MutableList<GroupProjectBean.Data>) {
        groupPjtBeans.addAll(data)
    }

    /**
     * 刷新
     */
    override fun onFRefresh() {
        super.onFRefresh()
        EmBeans.clear()
        LmBeans.clear()
        page = 1
        mPresenter.loadRepositories(type)
    }

    /**
     * 加载更多
     */
    override fun onFLoadmore() {
        super.onFLoadmore()
        page++
        mPresenter.loadRepositories(type)
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1, 2 -> {
                if (!TextUtils.isEmpty(projectId)){
                    map["projectId"] = projectId
                }
                if (condition.isNotEmpty()) {
                    map["condition"] = condition
                }
                if (groupId.isNotEmpty()) {
                    map["groupId"] = groupId
                }
                map["pageSize"] = "10"
                map["page"] = "$page"
            }
            3->{
                if (!TextUtils.isEmpty(projectId)){
                    map["projectId"] = projectId
                }

            }
            else -> {
            }
        }


        return map
    }

    //我的项目
    override fun showProjectDate(data: MutableList<Data>) {
        this.pjtData = data
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> PerformStateImp.API_PER_USER_LIST
            2 -> PerformStateImp.API_PER_BEE_LIST
            3 -> PerformStateImp.API_PER_GROUP_BY_PROJECTID
            else -> SystemSettImp.API_SYSTEM_PROJECT_LIST
        }

    }


    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
