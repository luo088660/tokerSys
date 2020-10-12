package com.toker.sys.view.home.fragment.my.item.teamattend

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
 * 团队考勤
 * @author yyx
 */

class TeamAttendFragment : BaseFragment<TeamAttendContract.View, TeamAttendPresenter>(), TeamAttendContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): TeamAttendFragment {
            val args = Bundle()
            val fragment = TeamAttendFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var mPresenter: TeamAttendPresenter = TeamAttendPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null){
            main_layout = inflateView(R.layout.fragment_team_attend, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        tv_title.text = "团队考勤"
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