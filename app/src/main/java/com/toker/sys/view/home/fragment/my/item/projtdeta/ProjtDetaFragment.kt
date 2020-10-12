package com.toker.sys.view.home.fragment.my.item.projtdeta

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.home.fragment.my.event.MyEvent
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_overall_perfor.*
import org.greenrobot.eventbus.EventBus

/**
 * 项目详情
 * @author yyx
 */

class ProjtDetaFragment : BaseFragment<ProjtDetaContract.View, ProjtDetaPresenter>(), ProjtDetaContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): ProjtDetaFragment {
            val args = Bundle()
            val fragment = ProjtDetaFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var mPresenter: ProjtDetaPresenter = ProjtDetaPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null){
            main_layout = inflateView(R.layout.fragment_projt_deta, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        tv_title.text = "清远银湖城"
    }

    override fun initData() {

        setOnClickListener(img_back)
        setOnClickListener(tv_perfor_01)
        setOnClickListener(tv_perfor_02)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            img_back -> {
                EventBus.getDefault().post(MyEvent(1))
            }
            //团队排名
            tv_perfor_01->{
                EventBus.getDefault().post(MainEvent(14))
            }
            //拓客员排名
            tv_perfor_02->{
                EventBus.getDefault().post(MainEvent(15))
            }
            else -> {
            }
        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }


}// Required empty public constructor