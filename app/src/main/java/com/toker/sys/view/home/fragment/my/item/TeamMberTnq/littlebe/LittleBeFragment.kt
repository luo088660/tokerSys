package com.toker.sys.view.home.fragment.my.item.TeamMberTnq.littlebe

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.view.home.fragment.my.item.adapter.LittleBeeAdapter
import com.toker.sys.view.home.fragment.my.item.bean.LittleBeBean
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean
import kotlinx.android.synthetic.main.fragment_little_be.*

/**
 * 小蜜蜂
 * @author yyx
 */

class LittleBeFragment : BaseFragment<LittleBeContract.View, LittleBePresenter>(), LittleBeContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): LittleBeFragment {
            val args = Bundle()
            val fragment = LittleBeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var mBeans: MutableList<LittleBeBean.PageData> = mutableListOf()
    var data: ProjectListBean.Data? = null
    var littleBeeAdapter: LittleBeeAdapter? = null
    override var mPresenter: LittleBePresenter = LittleBePresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_little_be, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        data = arguments?.getSerializable("data") as ProjectListBean.Data
        rv_ll_bee.layoutManager = GridLayoutManager(context, 1)
        littleBeeAdapter = LittleBeeAdapter(activity!!, mBeans)
        rv_ll_bee.adapter = littleBeeAdapter
    }

    override fun initData() {
        mPresenter.loadRepositories()
    }

    override fun showDataList(data: LittleBeBean.Data) {
        mBeans.addAll(data.pageData)
        littleBeeAdapter?.refreshData(mBeans)

    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {

    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["projectId"] = data?.projectId!!
//        map["condition"] = ""
        map["pageSize"] = "10"
        map["page"] = "1"

        return map
    }

    override fun getUrl(url_type: Int): String {
        return PerformStateImp.API_PER_BEE_LIST
    }


}// Required empty public constructor