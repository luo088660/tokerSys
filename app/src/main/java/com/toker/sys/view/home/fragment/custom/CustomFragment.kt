package com.toker.sys.view.home.fragment.custom

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.dialog.ProjectListDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.custom.newcustom.NewCustomActivity
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.fragment.custom.adapter.CustomAdapter
import com.toker.sys.view.home.fragment.custom.item.custfollup.CustFollUpFragment
import com.toker.sys.view.home.fragment.custom.item.followcust.FollowCustFragment
import com.toker.sys.view.home.fragment.custom.item.followup.FollowUpFragment
import com.toker.sys.view.home.fragment.custom.item.invacust.InvaCustFragment
import com.toker.sys.view.home.fragment.custom.item.mycus.MyCusFragment
import com.toker.sys.view.home.fragment.custom.item.publicpocust.PublicPoCustFragment
import com.toker.sys.view.home.fragment.custom.item.publicpool.PublicPoolFragment
import com.toker.sys.view.home.fragment.custom.item.recomcust.RecomCustFragment
import com.toker.sys.view.home.fragment.event.CustomEvent
import com.toker.sys.view.home.fragment.event.CustomProjectEvent
import com.toker.sys.view.home.fragment.event.MyOCusEvent
import kotlinx.android.synthetic.main.fragment_custom.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 客户
 * @author yyx
 */

class CustomFragment : BaseFragment<CustomContract.View, CustomPresenter>(), CustomContract.View {


    companion object {
        @JvmStatic
        fun newInstance(): CustomFragment {
            val args = Bundle()
            val fragment = CustomFragment()
            fragment.arguments = args
            return fragment!!
        }
    }

    private var data: MutableList<Data>? = null
    private var name: String? = "跟进中的客户"
    private var mBeans1 = arrayOf("")
    private var mBeans2 = arrayOf("")
    var mBeans = arrayOf("")
    private var rootView: View? = null//缓存Fragment view
    private var customAdapter: CustomAdapter? = null
    override var mPresenter: CustomPresenter = CustomPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        LogUtils.d(TAG, "createView: CustomFragment");
        if (rootView == null) {
            rootView = inflateView(R.layout.fragment_custom, container!!)
        }
        return rootView!!
    }

    override fun initView() {
        LogUtils.d(TAG, "initView--: ");
        tv_title.text = "客户"
        img_back.visibility = GONE
        if (AppApplication.TYPE == Constants.RESCUE1) {
            ll_custom.visibility = GONE
        }
        tv_yx_custom.isChecked = true
        initFragmentTabHost()
        EventBus.getDefault().register(this)

    }


    override fun initData() {
        mPresenter.loadRepositories()
        setOnClickListener(btn_custom)
        setOnClickListener(img_back)
        setOnClickListener(tv_yx_custom)
        setOnClickListener(tv_tj_custom)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            img_back -> {
                if (tv_title.text == resources.getString(R.string.custom_foll_up)) {
                    tv_title.text = "客户"
                    EventBus.getDefault().post(CustomEvent(mBeans1[0]))
                } else {
                    EventBus.getDefault().post(MainEvent(1))
                }
            }
            //新增客户 AddNewCutFragment
            btn_custom -> {
                val intent = Intent(context, NewCustomActivity::class.java)
                context?.startActivity(intent)

            }
            //推荐客户
            tv_tj_custom -> {
                rv_custom.visibility =  GONE
                tv_tj_custom.isChecked = true
                setIntentFragment(RecomCustFragment.newInstance(), Bundle())

            }
            //意向客户
            tv_yx_custom -> {
               /* rv_custom.visibility = GONE
                tv_yx_custom.isChecked = true
                setIntentFragment(MyCusFragment.newInstance(), Bundle())*/
                customEvent(CustomEvent(name!!))
            }
            else -> {
            }
        }
    }

    //客户明细
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun MyOCusEvent(x: MyOCusEvent) {
        rv_custom.visibility =  GONE
        tv_tj_custom.isChecked = true
        val bundle = Bundle()
        bundle.putString("phone",x.phone)
        setIntentFragment(RecomCustFragment.newInstance(), bundle)
    }

    override fun showProjectDate(data: MutableList<Data>) {
        this.data = data
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN_ORDERED)
    fun customEvent(event: CustomEvent) {
        this.name = event.name
        LogUtils.e("CustomFragment","event:${event.name}")
        EventBus.getDefault().removeStickyEvent(event)
        rv_custom.visibility = VISIBLE
        if (AppApplication.TYPE == Constants.RESCUE2) {
            ll_custom.visibility = VISIBLE
        }
        val bundle = Bundle()
        setIntentFragment(
            when (event.name) {
                //跟进中的客户
                mBeans1[0] -> {
                    //跟进中的客户 FollowCustFragment
                    if (AppApplication.TYPE == Constants.RESCUE1) FollowUpFragment.newInstance() else FollowCustFragment.newInstance()
                }
                //公客池
                mBeans1[1] -> {
                    customAdapter?.upDataView(1)
                    bundle.putString("type", "1")
                    if (AppApplication.TYPE == Constants.RESCUE1) PublicPoolFragment.newInstance() else PublicPoCustFragment.newInstance()
                }
                //无效客户  InvalidFragment.newInstance()
                mBeans1[2] -> {
                    bundle.putString("type", "2")
                    if (AppApplication.TYPE == Constants.RESCUE1) PublicPoolFragment.newInstance() else InvaCustFragment.newInstance()

                }
                //待跟进客户
                activity!!.resources.getString(R.string.custom_foll_up)
                -> {
                    rv_custom.visibility = GONE
                    tv_title.text = resources.getString(R.string.custom_foll_up)
                    CustFollUpFragment.newInstance()
                }
                else -> {
                    //意向客户
                    tv_yx_custom.isChecked = true
                    rv_custom.visibility = GONE
                    MyCusFragment.newInstance()
                }
            }, bundle
        )
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun CustomProjectEvent(event: CustomProjectEvent) {
        when (event.type) {
            //选择项目
            1 -> ProjectListDialog(context!!, data!!)
            else -> {
            }
        }

    }

    /**
     * fragment 展示
     */
    private fun initFragmentTabHost() {
        mBeans1 = resources.getStringArray(R.array.custom_rescue1)
        mBeans2 = resources.getStringArray(R.array.custom_rescue2)
        rv_custom.layoutManager = when (AppApplication.TYPE) {

            Constants.RESCUE1,
            Constants.RESCUE2 -> {
                mBeans = mBeans1
                setIntentFragment(
                    if (AppApplication.TYPE == Constants.RESCUE1) FollowUpFragment.newInstance() else FollowCustFragment.newInstance(),
                    Bundle()
                )
                GridLayoutManager(activity, 3)
            }
            else -> {
                mBeans = mBeans2
                rv_custom.visibility = GONE
                btn_custom.visibility = VISIBLE
                name = "意向客户"
                //意向客户
                tv_yx_custom.isChecked = true
                setIntentFragment(MyCusFragment.newInstance(), Bundle())
                GridLayoutManager(activity, 3)
            }
        }
        name = mBeans[0]
        customAdapter = CustomAdapter(activity!!, mBeans)
        rv_custom.adapter = customAdapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        LogUtils.d(TAG, "onDestroyView----: onDestroyView");
        EventBus.getDefault().unregister(this)
    }

    private fun setIntentFragment(fragment: Fragment, bundle: Bundle) {
        val beginTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        beginTransaction.replace(R.id.fl_custom, fragment)
        beginTransaction.commitAllowingStateLoss()
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> SystemSettImp.API_SYSTEM_PROJECT_LIST
            else -> {
                ""
            }
        }

    }

}