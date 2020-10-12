package com.toker.sys.view.home.activity.my.teammbertnq

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.fragment.event.TaskEvent
import com.toker.sys.view.home.fragment.my.item.TeamMberTnq.extension.ExtensionFragment
import com.toker.sys.view.home.fragment.my.item.TeamMberTnq.littlebe.LittleBeFragment
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean
import com.toker.sys.view.home.fragment.my.item.mylittlebee.littlebee.LittleBeeFragment
import com.toker.sys.view.home.fragment.task.adapter.TaskAdapter
import kotlinx.android.synthetic.main.activity_team_mber_tnq.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.security.cert.Extension

/**
 * 团队成员查询
 * @author yyx
 */

class TeamMberTnqActivity : BaseActivity<TeamMberTnqContract.View, TeamMberTnqPresenter>(), TeamMberTnqContract.View {

    override var mPresenter: TeamMberTnqPresenter = TeamMberTnqPresenter()
    private var type = 0//1 拓客员 2 小蜜蜂

    override fun layoutResID(): Int = R.layout.activity_team_mber_tnq

    var data: ProjectListBean.Data? = null
    override fun initView() {
        type = intent.getIntExtra("type", 0)
        LogUtils.d(TAG, "type:$type ");
        data = intent.getSerializableExtra("data") as ProjectListBean.Data
        EventBus.getDefault().register(this)
        tv_title.text = "团队成员查询"

    }

    override fun initData() {
        setOnClickListener(img_back)
        val bundle = Bundle()
        bundle.putSerializable("data", data)
        setIntentFragment(if (type == 0) ExtensionFragment.newInstance() else LittleBeFragment.newInstance(), bundle)
        val mBeans = resources.getStringArray(R.array.team_m_ber)
        rv_team_mber.layoutManager = GridLayoutManager(this, 2)
        val taskAdapter = TaskAdapter(this, mBeans)
        rv_team_mber.adapter = taskAdapter
        taskAdapter.upDataView(type)
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            else -> {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun TaskEvent(event: TaskEvent) {
        val bundle = Bundle()
        bundle.putSerializable("data", data)
        setIntentFragment(
            when (event.name) {
                //拓客员
                resources.getString(R.string.tip_extension) -> {
                    ExtensionFragment.newInstance()
                }
                //小蜜蜂
                else -> {
                    LittleBeFragment.newInstance()
                }
            }, bundle
        )

    }

    private fun setIntentFragment(fragment: Fragment, bundle: Bundle) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        beginTransaction.replace(R.id.fl_team_mber, fragment)
        beginTransaction.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
