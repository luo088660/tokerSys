package com.toker.sys.view.home.fragment.sheet.item.comingintpool

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.view.home.bean.OverdFollowBean
import com.toker.sys.view.home.fragment.sheet.adapter.OverdFollowUpAdapter
import kotlinx.android.synthetic.main.fragment_coming_int_pool.*
import kotlinx.android.synthetic.main.fragment_overd_follow_up.rv_overd_follow
import java.lang.Exception

/**
 * 即将进入公客池
 * @author yyx
 */

class ComingIntPoolFragment : BaseFragment<ComingIntPoolContract.View, ComingIntPoolPresenter>(),
    ComingIntPoolContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): ComingIntPoolFragment {
            val args = Bundle()
            val fragment = ComingIntPoolFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var page = 1

    override var mPresenter: ComingIntPoolPresenter = ComingIntPoolPresenter()
    var mBeans = mutableListOf<OverdFollowBean.Record>()
    var tv01: TextView? = null
    var upAdapter: OverdFollowUpAdapter? = null
    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_coming_int_pool, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_total, null)
        tv01 = view.findViewById<TextView>(R.id.tv_custo_total)

        rv_overd_follow.layoutManager = GridLayoutManager(context!!, 1)
        upAdapter = OverdFollowUpAdapter(context!!, mBeans,true);
        upAdapter?.setHeaderView(view)
        rv_overd_follow.adapter = upAdapter
        rv_overd_follow.addItemDecoration(getItemDecoration())
        try {
            projectId = arguments!!.getString("id")
            Log.d(TAG, "projectId:$projectId ");
        }catch (e: Exception){
            e.printStackTrace()
        }
        initSpringView(sv_int_pool)
    }

    override fun initData() {

        onFRefresh()
    }

    override fun onFRefresh() {
        super.onFRefresh()
        mBeans.clear()
        page = 1
        mPresenter.loadRepositories()

    }

    override fun onFLoadmore() {
        super.onFLoadmore()
        page++
        mPresenter.loadRepositories()
    }

    override fun showListData(data: OverdFollowBean.Data) {
        mBeans.addAll(data.records)
        upAdapter?.refreshData(mBeans)

        tv01?.text =
            Html.fromHtml(String.format(resources.getString(R.string.tip_custom), data.total))
    }

    var projectId:String = ""
    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        if (!TextUtils.isEmpty(projectId)) {
            map["projectId"] = projectId!!
        }
        map["searchType"] = "toPoolNum"
        map["page"] = "$page"
        return map
    }

    override fun getUrl(url_type: Int): String {
        return  if (AppApplication.TYPE==Constants.RESCUE2) {
            CustomerManageImp.API_CUST_PROJECT_PAGE_PROJECT
        }else{
            CustomerManageImp.API_CUST_PROJECT_FOLLOW_USERPAGE
        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }


}// Required empty public constructor