package com.toker.sys.view.home.fragment.sheet

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.dialog.ProjectListDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.view.home.activity.my.projtdeta.viewattendan.ViewAttendanActivity
import com.toker.sys.view.home.activity.sheet.project.ProjectActivity
import com.toker.sys.view.home.activity.sheet.stextenrank.STExtenRankActivity
import com.toker.sys.view.home.activity.sheet.stprojectran.STProjectRanActivity
import com.toker.sys.view.home.activity.sheet.stteamrank.STTeamRankActivity
import com.toker.sys.view.home.activity.sheet.waitcust.WaitCustActivity
import com.toker.sys.view.home.activity.sheet.waitforcust.WaitForCustActivity
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.fragment.my.item.bean.GroupListBean
import com.toker.sys.view.home.fragment.sheet.event.SheetEvent
import com.toker.sys.view.home.fragment.sheet.item.stsheet.STSheetFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 首页
 * @author yyx
 */

class SheetFragment : BaseFragment<SheetContract.View, SheetPresenter>(), SheetContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): SheetFragment {
            val args = Bundle()
            val fragment = SheetFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var mGroupList: ArrayList<GroupListBean.Data> = arrayListOf()
    private var projectData: MutableList<Data>? = null
    override var mPresenter: SheetPresenter = SheetPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        log("createView", "SheetFragment")
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_sheet, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        mPresenter.loadRepositories()
        EventBus.getDefault().register(this)
        setIntentFragment(STSheetFragment.newInstance(), Bundle())
    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN_ORDERED)
    fun sheetEvent(event: SheetEvent) {
        EventBus.getDefault().removeStickyEvent(event)
        when (event.type) {
            // 首页 主页面
            1 -> setIntentFragment(STSheetFragment.newInstance(), Bundle())
            // 项目业绩排名
            2 -> activity?.startActivity(Intent(activity, STProjectRanActivity::class.java))
            //团队业绩排名
            3 -> activity?.startActivity(Intent(activity, STTeamRankActivity::class.java))
            //拓客员业绩排名
            4 -> activity?.startActivity(Intent(activity, STExtenRankActivity::class.java))
            //我的项目 Project
            6 -> activity?.startActivity(Intent(activity, ProjectActivity::class.java))
            //待跟进客户
            7 -> {
                when (AppApplication.TYPE) {
                    Constants.RESCUE1,
                    Constants.RESCUE2-> {
                        activity?.startActivity(Intent(activity, WaitForCustActivity::class.java))
                    }
                    else -> {
                        activity?.startActivity(Intent(activity, WaitCustActivity::class.java))
                    }
                }

            }
            //待跟进客户详情
//            8 -> setIntentFragment(CustDetailFollFragment.newInstance(), Bundle())
            //选择项目
            9-> ProjectListDialog(context!!, projectData!!,event.stuta)
            //团队考勤
            10->{
                val intent = Intent(activity, ViewAttendanActivity::class.java)
                intent.putParcelableArrayListExtra("mGroupList",mGroupList)
                intent.putExtra("isType",true)
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    override fun initData() {

    }
    //我的项目
    override fun showProjectDate(data: MutableList<Data>) {
        this.projectData = data
        EventBus.getDefault().post(data)
    }
    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
    override fun showGroupList(data: MutableList<GroupListBean.Data>) {
        mGroupList.addAll(data)

    }
    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> {
                SystemSettImp.API_SYSTEM_PROJECT_LIST
            }
            else -> {
                PerformStateImp.API_PER_CUR_GROUP_LIST1
            }
        }

    }

    private fun setIntentFragment(fragment: Fragment, bundle: Bundle) {
        val beginTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        beginTransaction.replace(R.id.rl_f_sheet, fragment)
        beginTransaction.commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}