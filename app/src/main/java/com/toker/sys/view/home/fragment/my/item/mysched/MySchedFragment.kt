package com.toker.sys.view.home.fragment.my.item.mysched

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.view.home.fragment.my.event.MyEvent
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus

/**
 * 我的排班
 * @author yyx
 */

class MySchedFragment : BaseFragment<MySchedContract.View, MySchedPresenter>(), MySchedContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): MySchedFragment {
            val args = Bundle()
            val fragment = MySchedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var mPresenter: MySchedPresenter = MySchedPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null){
            main_layout = inflateView(R.layout.fragment_my_sched, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        tv_title.text = "我的排班"
    }

    override fun initData() {
        setOnClickListener(img_back)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            img_back -> EventBus.getDefault().post(MyEvent(1))
            else -> {
            }
        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }


}// Required empty public constructor