package com.toker.sys.view.home.fragment.custom.item.custfollup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseFragment

/**
 * 待跟进客户
 * @author yyx
 */

class CustFollUpFragment : BaseFragment<CustFollUpContract.View, CustFollUpPresenter>(), CustFollUpContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): CustFollUpFragment {
            val args = Bundle()
            val fragment = CustFollUpFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var mPresenter: CustFollUpPresenter = CustFollUpPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null){
            main_layout = inflateView(R.layout.fragment_cust_foll_up, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }


}