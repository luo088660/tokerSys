package com.toker.sys.view.home.fragment.my.item.TeamMberTnq.extension

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.view.home.fragment.my.item.adapter.ExtensionAdapter
import com.toker.sys.view.home.fragment.my.item.bean.ExtensionBean
import com.toker.sys.view.home.fragment.my.item.bean.ProjectListBean
import kotlinx.android.synthetic.main.fragment_extension.*

/**
 * 拓客员
 * @author yyx
 */

class ExtensionFragment : BaseFragment<ExtensionContract.View, ExtensionPresenter>(), ExtensionContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): ExtensionFragment {
            val args = Bundle()
            val fragment = ExtensionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var data: ProjectListBean.Data? = null
    var mBeans: MutableList<ExtensionBean.PageData> = mutableListOf()
    var adapter: ExtensionAdapter? = null
    override var mPresenter: ExtensionPresenter = ExtensionPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_extension, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        data = arguments?.getSerializable("data") as ProjectListBean.Data
        mPresenter.loadRepositories()

        rv_extension.layoutManager = GridLayoutManager(context, 1)
        adapter = ExtensionAdapter(context!!, mBeans)
        rv_extension.adapter = adapter
    }

    override fun initData() {
    }

    override fun showDataList(data: ExtensionBean.Data) {
        mBeans.addAll(data.pageData)
        adapter?.refreshData(mBeans)
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
        return PerformStateImp.API_PER_USER_LIST
    }


}// Required empty public constructor