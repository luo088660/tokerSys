package com.toker.sys.view.home.fragment.sheet.item.topcustperfran.stteamrank

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.view.home.activity.adapter.STTeamRankAdapter
import com.toker.sys.view.home.activity.sheet.event.STProjectRanEvent
import com.toker.sys.view.home.activity.sheet.event.TimeEvent
import com.toker.sys.view.home.activity.sheet.event.VISIEvent
import com.toker.sys.view.home.bean.STExtenRankBean
import kotlinx.android.synthetic.main.fragment_s_t_tea_m_rank.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

/**
 * 主页
 * 团队排名
 * @author yyx
 */

class STTeaMRankFragment : BaseFragment<STTeaMRankContract.View, STTeaMRankPresenter>(),
    STTeaMRankContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): STTeaMRankFragment {
            val args = Bundle()
            val fragment = STTeaMRankFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mBeans: MutableList<STExtenRankBean.Record> = mutableListOf()
    override var mPresenter: STTeaMRankPresenter = STTeaMRankPresenter()

    val sdf = SimpleDateFormat("yyyy-MM")
    private var adapter: STTeamRankAdapter? = null
    override fun onNetworkLazyLoad() {
    }

    var isOrder = false
    var drawableS: Drawable?= null
    var drawableX: Drawable?= null
    private var page = 1
    private var item = 1
    private var sortWay: String = "asc"
    var tvKhzl:TextView?= null
    private var sortBy: String = "1"
    private var time =sdf.format(Date(System.currentTimeMillis()))
    private var type = "2"
    private var projectId = ""

    var beginDate = ""
    var endDate = ""

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_s_t_tea_m_rank, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        val view = LayoutInflater.from(activity).inflate(R.layout.layout_st_team_rk,null)
        tvKhzl = view.findViewById<TextView>(R.id.tv_khzl)
        setOnClickListener(tvKhzl!!)
        rv_team_rank_.layoutManager = GridLayoutManager(context, 1)
        adapter = STTeamRankAdapter(context!!, mBeans)
        adapter!!.setHeaderView(view)
        rv_team_rank_.adapter = adapter
    }
    override fun initData() {
        mPresenter.loadRepositories()

        // 使用代码设置drawableleft
        drawableS = resources.getDrawable( R.mipmap.icon_xspx)
        drawableX = resources.getDrawable( R.mipmap.icon_xxpx)
        // / 这一步必须要做,否则不会显示.
        drawableS!!.setBounds(0, 0, drawableS!!.minimumWidth,drawableS!!.minimumHeight )
        drawableX!!.setBounds(0, 0, drawableX!!.minimumWidth,drawableX!!.minimumHeight )

    }
    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            tvKhzl->{
                tvKhzl?.setCompoundDrawables(null,null,if (isOrder)drawableS else drawableX,null)
                sortWay = if (isOrder) "desc" else "asc"
                isOrder = !isOrder
                mPresenter.loadRepositories()
            }
            else -> {
            }
        }
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
     * 选择项目
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun dataProject(event: com.toker.sys.view.home.bean.Data) {
        projectId = event.projectId
        mPresenter.loadRepositories()
    }
    /**
     * 业绩项
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun STProjectRanEvent(event: STProjectRanEvent) {
        log("${event.name}-----", "STProjectRanActivity")
        item = event.type
        mPresenter.loadRepositories()
    }
    //数据展示
    override fun showDataList(data: STExtenRankBean.Data) {

        EventBus.getDefault().post(VISIEvent(data.records.size==10,"2"))
        adapter?.refreshData(data.records)
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
                when (type) {
                    "4" -> {
                        map["beginDate"] = beginDate
                        map["endDate"] = endDate
                    }
                    else -> {
                        map["time"] = time
                    }
                }
                map["type"] = type
                map["item"] = "$item" //留电数:1,到访数:2,成交量:3,成交额:4"
                map["sortWay"] = sortWay//"sortBy不能为空,asc:正序;desc:倒序"
                map["sortBy"] = sortBy //完成：1，目标：2，完成率：3"
                map["page"] = "$page"      //	当前页	否	int	默认1
                map["pageSize"] = "10"       //	分页大小	否	int	默认10
            }
            else -> {
            }
        }
//
        return map
    }

    override fun getUrl(url_type: Int): String {
        return PerformStateImp.API_PER_GROUP_PER_FORMANCE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}// Required empty public constructor