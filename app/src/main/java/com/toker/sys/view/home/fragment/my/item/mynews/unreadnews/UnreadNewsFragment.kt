package com.toker.sys.view.home.fragment.my.item.mynews.unreadnews

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.AppApplication
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.view.home.fragment.event.ReadCountEvent
import com.toker.sys.view.home.fragment.my.item.adapter.UnreadNewsAdapter
import com.toker.sys.view.home.fragment.my.item.bean.UnreadNewsBean
import kotlinx.android.synthetic.main.fragment_unread_news.*
import kotlinx.android.synthetic.main.fragment_unread_news.sp_t_pend
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat

/**
 * 未读
 * @author yyx
 */

class UnreadNewsFragment : BaseFragment<UnreadNewsContract.View, UnreadNewsPresenter>(),
    UnreadNewsContract.View {
    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val sdf1 = SimpleDateFormat("yyyy/MM/dd")

    companion object {
        @JvmStatic
        fun newInstance(): UnreadNewsFragment {
            val args = Bundle()
            val fragment = UnreadNewsFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private var page = "1"
    val mBeans: MutableList<UnreadNewsBean.Records> = mutableListOf()
    override var mPresenter: UnreadNewsPresenter = UnreadNewsPresenter()
    private var newsAdapter: UnreadNewsAdapter? = null
    private var mBean: UnreadNewsBean.Records? = null
    var traceId = ""
    var url = ""
    var tableTag = ""
    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_unread_news, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        mPresenter.loadRepositories()
        rv_read_news.layoutManager = GridLayoutManager(context, 1)
        newsAdapter = UnreadNewsAdapter(context!!, mBeans, 1)
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
            EventBus.getDefault().post(ReadCountEvent(data.total))
        }else{
            if(page == "1"){
                mBeans.clear()
                newsAdapter!!.refreshData(mBeans)
                EventBus.getDefault().post(ReadCountEvent(0))
            }
        }
    }

    //标记已读
    override fun noReadCount(i: Int) {
        when (i) {
            1 -> {
//                toast("标记成功")
                onFRefresh()
            }
            else -> {
                toast("标记失败")
            }
        }

    }

    override fun showDataSuccess(desc: String) {
        toast(desc)
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun unreadNewsBean(event: UnreadNewsBean.Records) {
        when (event.type) {
            0,1 -> {
                mBean = event
                mPresenter.readMsg()
            }
            2 -> {
                val executeUrl = event.executeUrl
                val indexOf = executeUrl.indexOf("#")
                val substring = executeUrl.substring(0,indexOf)

                val urlreplace = executeUrl.replace(substring, "")
                url = substring.replace("url=", "")

                val indexOf1 = urlreplace.indexOf(";")
                 traceId = urlreplace.substring(0,indexOf1).replace("#traceId=","")
                 tableTag = urlreplace.substring(indexOf1).replace(";tableTag=","")
                mPresenter.noSendTraceMsg()

            }
            else -> {
            }
        }

    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                map["status"] = "0"
                map["pageSize"] = "10"
                map["page"] = page
            }
            2 -> {
                map["msgId"] = mBean?.id!!
                map["tableTag"] = mBean?.tableTag!!//"${mBean?.tableTag!!}"
            }
            3 -> {
                map["traceId"] = traceId
                map["phone"] = AppApplication.PHONE
                map["tableTag"] = tableTag
            }
        }

        return map
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> TaskManageImp.API_TASK_MSG_LIST
            2 -> TaskManageImp.API_TASK_READ_MSG
            3 -> url
            else->""
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }


}// Required empty public constructor