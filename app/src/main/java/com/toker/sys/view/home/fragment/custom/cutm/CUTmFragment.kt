package com.toker.sys.view.home.fragment.custom.cutm

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.view.home.fragment.custom.CustomFragment
import com.toker.sys.view.home.fragment.event.CustomEvent
import com.toker.sys.view.home.fragment.event.CustomEventT
import com.toker.sys.view.home.fragment.event.TaskEvent
import com.toker.sys.view.home.fragment.event.TaskEventT
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author yyx
 */

class CUTmFragment : BaseFragment<CUTmContract.View, CUTmPresenter>(), CUTmContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): CUTmFragment {
            val args = Bundle()
            val fragment = CUTmFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var mPresenter: CUTmPresenter = CUTmPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null){
            main_layout = inflateView(R.layout.fragment_c_u_tm, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        setIntentFragment(CustomFragment.newInstance(), Bundle())
    }

    override fun initData() {

    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {

    }

    private fun setIntentFragment(fragment: Fragment, bundle: Bundle) {
        val beginTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        beginTransaction.replace(R.id.fl_custom_hh, fragment)
        beginTransaction.commitAllowingStateLoss()
    }
}// Required empty public constructor