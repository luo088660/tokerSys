package com.toker.sys.view.home.fragment.my.item.mynews.haveread

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.view.home.fragment.my.item.adapter.UnreadNewsAdapter
import com.toker.sys.view.home.fragment.my.item.bean.UnreadNewsBean
import kotlinx.android.synthetic.main.fragment_unread_news.*
import java.text.SimpleDateFormat

/**
 * @author yyx
 */

class HaveReadFragment : BaseFragment<HaveReadContract.View, HaveReadPresenter>(), HaveReadContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): HaveReadFragment {
            val args = Bundle()
            val fragment = HaveReadFragment()
            fragment.arguments = args
            return fragment
        }
    }

    val sdf1 = SimpleDateFormat("yyyy/MM/dd")
    var newsAdapter: UnreadNewsAdapter? = null
    val mBeans: MutableList<UnreadNewsBean.Records> = mutableListOf()
    override var mPresenter: HaveReadPresenter = HaveReadPresenter()
    private var page = "1"
    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null){
            main_layout = inflateView(R.layout.fragment_have_read, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        mPresenter.loadRepositories()
        rv_read_news.layoutManager = GridLayoutManager(context, 1)
        newsAdapter = UnreadNewsAdapter(context!!, mBeans, 0)
        rv_read_news.adapter = newsAdapter
        initSpringView(sp_t_pend)
    }

    override fun initData() {
    }
    override fun onFRefresh() {
        super.onFRefresh()
        page="1"
        mBeans.clear()
        mPresenter.loadRepositories()
    }

    override fun onFLoadmore() {
        super.onFLoadmore()
        var toInt = page.toInt()
        toInt++
        page = toInt.toString()
        mPresenter.loadRepositories()

    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
    override fun showDataList(data: UnreadNewsBean.Data) {
        if (data.records.isNotEmpty()) {
            for (datum in data.records) {
                val sendTime = sdf1.format(datum.sendTime)
                val bean = UnreadNewsBean.Records(
                    "",
                    "",
                    0,
                    sendTime,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    "",
                    "",
                    "",
                    "",0
                )

                val contains = mBeans.contains(bean)
                if (!contains){
                    mBeans.add(bean)
                }
                mBeans.add(datum)
            }
            newsAdapter!!.refreshData(mBeans)
        }
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String,String>()
        map["status"]="1"
        map["pageSize"]="10"
        map["page"]=page
        return map
    }

    override fun getUrl(url_type: Int): String {
        return TaskManageImp.API_TASK_MSG_LIST
    }

}// Required empty public constructor