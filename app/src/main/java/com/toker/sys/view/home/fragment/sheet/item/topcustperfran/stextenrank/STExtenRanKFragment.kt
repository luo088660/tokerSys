package com.toker.sys.view.home.fragment.sheet.item.topcustperfran.stextenrank

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.dialog.ProjectListDialog
import com.toker.sys.dialog.sheet.TurnoverDialog
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.adapter.STExtenRankAdapter
import com.toker.sys.view.home.activity.sheet.event.STProjectRanEvent
import com.toker.sys.view.home.activity.sheet.event.TimeEvent
import com.toker.sys.view.home.activity.sheet.event.VISIEvent
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.bean.STExtenRankBean
import kotlinx.android.synthetic.main.activity_s_t_exten_rank.*
import kotlinx.android.synthetic.main.fragment_s_t_exten_ran_k.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_t_received_01.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat

/**
 * 主页
 * 拓客员排名
 * @author yyx
 */

class STExtenRanKFragment : BaseFragment<STExtenRanKContract.View, STExtenRanKPresenter>(), STExtenRanKContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): STExtenRanKFragment {
            val args = Bundle()
            val fragment = STExtenRanKFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val sdfPb = SimpleDateFormat("yyyy-MM")
    private var time: String = sdfPb.format(System.currentTimeMillis())
    var adapter: STExtenRankAdapter? = null
    override var mPresenter: STExtenRanKPresenter = STExtenRanKPresenter()
    var beginDate = ""
    var endDate = ""

    var drawableS: Drawable? = null
    var drawableX: Drawable? = null
    var isOrder = false
    private var type: String = "2"
    private var item: String = "4"
    private var sortWay: String = "asc"
    private var sortBy: String = "1"
    private var page = 1
    private var projectId = ""
    private var groupId = ""
    private var mBeans: MutableList<STExtenRankBean.Record> = mutableListOf()
    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null){
            main_layout = inflateView(R.layout.fragment_s_t_exten_ran_k, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        rv_t_exten_rank_.layoutManager = GridLayoutManager(context, 1)
        adapter = STExtenRankAdapter(context!!, mBeans)
        rv_t_exten_rank_.adapter = adapter
    }

    override fun initData() {
        drawableS = resources.getDrawable(R.mipmap.icon_xspx)
        drawableX = resources.getDrawable(R.mipmap.icon_xxpx)
        // / 这一步必须要做,否则不会显示.
        drawableS!!.setBounds(0, 0, drawableS!!.minimumWidth, drawableS!!.minimumHeight)
        drawableX!!.setBounds(0, 0, drawableX!!.minimumWidth, drawableX!!.minimumHeight)
        mPresenter.loadRepositories()
        setOnClickListener(tv_st_exten_rank_02_)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            //完成
            tv_st_exten_rank_02_ -> {
                tv_st_exten_rank_02_?.setCompoundDrawables(
                    null,
                    null,
                    if (isOrder) drawableS else drawableX,
                    null
                )
                sortWay = if (isOrder) "asc" else "desc"
                isOrder = !isOrder
                mPresenter.loadRepositories()
            }
            else -> {
            }
        }
    }
    //数据展示
    override fun showListData(data: STExtenRankBean.Data) {
        EventBus.getDefault().post(VISIEvent(data.records.size==10,"3"))
        adapter?.refreshData(data.records)
    }

    /**
     * 时间
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun TimeEvent(event: TimeEvent) {
        time = event.time
        type = event.type

        beginDate = event.beginDate
        endDate = event.endDate
        mPresenter.loadRepositories()
    }
    /**
     * 业绩项
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun STProjectRanEvent(event: STProjectRanEvent) {
        log("${event.name}-----", "STProjectRanActivity")
        item = "${event.type}"
        mPresenter.loadRepositories()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: Data) {
        LogUtils.d(TAG, "event.stuta:${event.stuta} ");
        when (event.stuta) {
            //全部团队
            0 -> {
                groupId = event.id
            }
            //我的项目
            2 -> {
                projectId = event.projectId
                groupId = ""
            }
            else -> {
            }
        }
        mPresenter.loadRepositories()

    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                if (!TextUtils.isEmpty(projectId)) {
                    map["projectId"] = projectId
                }
                if (!TextUtils.isEmpty(groupId)) {
                    map["groupId"] = groupId
                }
//                map["time"] = time
                map["type"] = type
                map["item"] = item //留电数:1,到访数:2,成交量:3,成交额:4"
                when (type) {
                    "4" -> {
                        map["beginDate"] = beginDate
                        map["endDate"] = endDate
                    }
                    else -> {
                        map["time"] = time
                    }
                }
                map["sortWay"] = sortWay//"sortBy不能为空,asc:正序;desc:倒序"
                map["sortBy"] = sortBy //完成：1，目标：2，完成率：3"
                map["page"] = "$page"      //	当前页	否	int	默认1
                map["pageSize"] = "10"       //	分页大小	否	int	默认10
            }
            else -> {
            }
        }
        return map
    }

    override fun getUrl(url_type: Int): String {
        return  PerformStateImp.API_PER_STATE_BY_GROUP
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }
}