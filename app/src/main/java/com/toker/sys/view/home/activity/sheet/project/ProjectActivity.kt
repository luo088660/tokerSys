package com.toker.sys.view.home.activity.sheet.project

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.my.projtdeta.ProjtDetaActivity
import com.toker.sys.view.home.fragment.my.item.adapter.MyPageAdapter
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean
import kotlinx.android.synthetic.main.activity_project.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的项目
 * @author yyx
 */

class ProjectActivity : BaseActivity<ProjectContract.View, ProjectPresenter>(), ProjectContract.View {

    override var mPresenter: ProjectPresenter = ProjectPresenter()


    override fun layoutResID(): Int = R.layout.activity_project
    val mBeans: MutableList<ProjectListBean.Data> = mutableListOf()
    var adapter: MyPageAdapter? = null
    override fun initView() {
        EventBus.getDefault().register(this)
        tv_title.text = "我的项目"
        mPresenter.loadRepositories()
        rv_project.layoutManager = GridLayoutManager(this, 1)
        adapter = MyPageAdapter(this, mBeans, true)
        rv_project.adapter = adapter
    }

    override fun initData() {
        setOnClickListener(img_back)


    }

    override fun showListData(data: MutableList<ProjectListBean.Data>) {
        LogUtils.d(TAG, "data:$data ");
        adapter?.refreshView(data)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()

            else -> {

            }
        }
    }

    //项目详情
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun projectListBean(event: ProjectListBean.Data) {
        val intent = Intent(this, ProjtDetaActivity::class.java)
        intent.putExtra("event",event)
        startActivity(intent)
    }

    override fun getUrl(url_type: Int): String {
        return SystemSettImp.API_SYSTEM_PROJECT_LIST
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
