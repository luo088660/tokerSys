package com.toker.sys.view.home.fragment.my

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.home.activity.my.custmap.CustMapActivity
import com.toker.sys.view.home.activity.my.myatten.MyAttenActivity
import com.toker.sys.view.home.activity.my.mylittlebee.MyLittleBeeActivity
import com.toker.sys.view.home.activity.my.mynews.MyNewsActivity
import com.toker.sys.view.home.activity.my.projtdeta.viewattendan.ViewAttendanActivity
import com.toker.sys.view.home.activity.my.recomextpoint.RecomExtPointActivity
import com.toker.sys.view.home.activity.sheet.project.ProjectActivity
import com.toker.sys.view.home.fragment.my.event.MyEvent
import com.toker.sys.view.home.fragment.my.item.bean.GroupListBean
import com.toker.sys.view.home.fragment.my.item.mypage.MyPageFragment
import com.toker.sys.view.home.fragment.my.item.mysched.MySchedFragment
import com.toker.sys.view.home.fragment.my.item.projtdeta.ProjtDetaFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的
 * @author yyx
 */

class MyFragment : BaseFragment<MyContract.View, MyPresenter>(), MyContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): MyFragment {
            val args = Bundle()
            val fragment = MyFragment()
            fragment.arguments = args
            return fragment
        }
    }
    var mGroupList: ArrayList<GroupListBean.Data> = arrayListOf()
    override var mPresenter: MyPresenter = MyPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        log("createView", "MyFragment")
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_my, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        setIntentFragment(MyPageFragment.newInstance(), Bundle())
        EventBus.getDefault().register(this)

    }


    override fun initData() {
        if (AppApplication.TYPE == Constants.RESCUE4) {
            mPresenter.loadRepositories()
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun myEvent(event: MyEvent) {
        log("event.type:${event.type}", "MyFragment")
        when (event.type) {
            //我的主页
            1 -> setIntentFragment(MyPageFragment.newInstance(), Bundle())
            //客户地图
            2 -> activity?.startActivity(Intent(activity, CustMapActivity::class.java))
            //我的考勤
            3 -> activity?.startActivity(Intent(activity, MyAttenActivity::class.java))
            //我的排班
            4 -> setIntentFragment(MySchedFragment.newInstance(), Bundle())
            //我的小蜜蜂
            5 -> activity?.startActivity(Intent(activity, MyLittleBeeActivity::class.java))
            //我的消息
            6 -> activity?.startActivity(Intent(activity, MyNewsActivity::class.java))
            //团队考勤
            7 ->  {
                val intent = Intent(activity, ViewAttendanActivity::class.java)
                intent.putParcelableArrayListExtra("mGroupList",mGroupList)
                intent.putExtra("isType",true)
                startActivity(intent)

            }//setIntentFragment(TeamAttendFragment.newInstance(), Bundle())
            //项目详情 ProjtDetaFragment
            8 -> setIntentFragment(ProjtDetaFragment.newInstance(), Bundle())
            //推荐拓客点
            9-> activity?.startActivity(Intent(activity, RecomExtPointActivity::class.java))
            //我的项目
            10-> startActivity(Intent(activity, ProjectActivity::class.java))
            else -> {
            }
        }
        EventBus.getDefault().removeStickyEvent(event)

    }


    override fun getUrl(url_type: Int): String {
        return  PerformStateImp.API_PER_CUR_GROUP_LIST1
    }

    override fun showGroupList(data: MutableList<GroupListBean.Data>) {
        mGroupList.addAll(data)

    }
    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {

    }


    private fun setIntentFragment(fragment: Fragment, bundle: Bundle) {
        val beginTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        beginTransaction.replace(R.id.rl_f_my, fragment)
        beginTransaction.commitAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        log("onDestroyView", "MyFragment")
        EventBus.getDefault().unregister(this)
    }
}