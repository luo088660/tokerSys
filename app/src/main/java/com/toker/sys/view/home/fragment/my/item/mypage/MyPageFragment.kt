package com.toker.sys.view.home.fragment.my.item.mypage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.utils.tools.PhoneUtils
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.home.activity.my.projtdeta.ProjtDetaActivity
import com.toker.sys.view.home.activity.my.qrcode.QRCodeActivity
import com.toker.sys.view.home.fragment.my.event.MyEvent
import com.toker.sys.view.home.fragment.my.item.adapter.MyPageAdapter
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean
import com.toker.sys.view.home.fragment.my.item.bean.ReadCountBean
import kotlinx.android.synthetic.main.fragment_my_page.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的  主页
 * @author yyx
 */

class MyPageFragment : BaseFragment<MyPageContract.View, MyPagePresenter>(), MyPageContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): MyPageFragment {
            val args = Bundle()
            val fragment = MyPageFragment()
            fragment.arguments = args
            return fragment
        }
    }

    val mBeans: MutableList<ProjectListBean.Data> = mutableListOf()
    override var mPresenter: MyPagePresenter = MyPagePresenter()
    var adapter: MyPageAdapter? = null
    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_my_page, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        tv_title.text = "我的"
        mPresenter.ProjectList()
        tv_my_page_01.text = when (AppApplication.TYPE) {
            Constants.RESCUE1 -> "${AppApplication.USERNAME}\t拓客中层"
            Constants.RESCUE2 -> "${AppApplication.USERNAME}\t拓客管理员"
            Constants.RESCUE3 -> "${AppApplication.USERNAME}\t拓客员"
            else -> {
                "${AppApplication.USERNAME}\t拓客组长"
            }
        }

        rv_my_page.layoutManager = GridLayoutManager(context, 1)
        adapter = MyPageAdapter(context!!, mBeans, false)
        rv_my_page.adapter = adapter
        rv_my_page.visibility = GONE
    }

    override fun onStart() {
        super.onStart()
        img_back.visibility = GONE
        mPresenter.noReadCount()
        if (!TextUtils.isEmpty(AppApplication.ICON)) {

            Glide.with(context!!).load(AppApplication.ICON)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(25)))//圆角半径
                .into(img_title)
        }
    }

    var projectName = ""
    override fun showListData(data: MutableList<ProjectListBean.Data>) {
        adapter?.refreshView(data)
        if (data.isNotEmpty()) {
            projectName = data.joinToString {
                it.projectName
            }.replace(",", "/")
            tv_my_page_03.text = projectName
        }
        Log.e(TAG, "AppApplication.TYPE:${AppApplication.TYPE} ");
        if (AppApplication.TYPE == Constants.RESCUE1) {
            tv_my_page_03.text = AppApplication.COMPANY
        }
    }


    override fun initData() {
        AppApplication.PHONE
        tv_my_page_02.text = PhoneUtils(context!!).phoneRepla(AppApplication.PHONE)
        //我的排班
        ll_my_03.visibility = GONE
        when (AppApplication.TYPE) {
            //中层,管理员
            Constants.RESCUE1,
            Constants.RESCUE2 -> {
                ll_my_02.visibility = GONE
                ll_my_04.visibility = GONE
                ll_my_07.visibility = GONE
                img_my_pager.visibility = GONE
            }
            //拓客员
            Constants.RESCUE3 -> {
                ll_my_01.visibility = GONE
                ll_my_07.visibility = GONE
            }
            else -> {
                ll_my_01.visibility = GONE
            }
        }

        setOnClickListener(img_back)
        setOnClickListener(ll_my_info)
        setOnClickListener(tv_my_01)
        setOnClickListener(ll_my_02)
        setOnClickListener(ll_my_03)
        setOnClickListener(ll_my_04)
        setOnClickListener(ll_my_05)
        setOnClickListener(ll_my_06)
        setOnClickListener(ll_my_07)
//        setOnClickListener(img_my_pager)

    }


    var isfoVisiBtn = false
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            img_back -> EventBus.getDefault().post(MainEvent(1))
            //个人信息
            ll_my_info -> {

                EventBus.getDefault().post(MainEvent(3, projectName))
            }
            //我的项目
            tv_my_01 -> {

                val drawableLeft = resources.getDrawable(R.mipmap.icon_xiangmu, null)
                val drawable = resources.getDrawable(
                    if (!isfoVisiBtn) R.mipmap.icon_xsjt else R.mipmap.icon_right,
                    null
                )
                rv_my_page.visibility = if (!isfoVisiBtn) VISIBLE else GONE
                isfoVisiBtn = !isfoVisiBtn
                tv_my_01.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    drawableLeft,
                    null,
                    drawable,
                    null
                )
            }
            //我的考勤 MyAttenFragment
            ll_my_02 -> EventBus.getDefault().post(MyEvent(3))
            //我的排班 MySchedFragment
            ll_my_03 -> EventBus.getDefault().post(MyEvent(4))
            //我的小蜜蜂 MyLittleBeeFragment
            ll_my_04 -> EventBus.getDefault().post(MyEvent(5))
            //客户地图
            ll_my_05 -> EventBus.getDefault().post(MyEvent(2))
            //我的消息 MyNewsFragment
            ll_my_06 -> EventBus.getDefault().post(MyEvent(6))
            //我的二维码
            img_my_pager -> {
                val intent = Intent(context, QRCodeActivity::class.java)
                intent.putExtra("projectName", projectName)
                startActivity(intent)
            }
            //团队考勤TeamAttendFragment
            else -> EventBus.getDefault().post(MyEvent(7))
        }
    }

    //未读消息统计
    override fun showDataNumBer(data: ReadCountBean.Data) {
        tv_my_ms_01.text = if (data.countNum > 99) "99+" else "${data.countNum}"
        tv_my_ms_01.visibility = if (data.countNum != 0) VISIBLE else GONE
    }

    //项目详情
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun projectListBean(event: ProjectListBean.Data) {
        val intent = Intent(context, ProjtDetaActivity::class.java)
        intent.putExtra("event", event)
        startActivity(intent)
    }

    override fun getUrl(url_type: Int): String {

        return when (url_type) {
            1 -> SystemSettImp.API_SYSTEM_PROJECT_LIST
            3 -> TaskManageImp.API_TASK_NOREAD_COUNT
            else -> {
                ""
            }
        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}