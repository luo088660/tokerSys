package com.toker.sys.view.home.fragment.sheet.item.topcustperfran.stprojectran

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.view.home.activity.adapter.STProjectRanAdapter
import com.toker.sys.view.home.activity.sheet.event.STProjectRanEvent
import com.toker.sys.view.home.activity.sheet.event.TimeEvent
import com.toker.sys.view.home.activity.sheet.event.VISIEvent
import com.toker.sys.view.home.bean.STExtenRankBean
import kotlinx.android.synthetic.main.activity_s_t_project_ran.*
import kotlinx.android.synthetic.main.activity_s_t_project_ran.rv_s_t_project
import kotlinx.android.synthetic.main.fragment_s_t_project_ra_n.*
import kotlinx.android.synthetic.main.layout_t_received_01.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat

/**
 * 主页
 * 项目排名
 * @author yyx
 */

class STProjectRaNFragment : BaseFragment<STProjectRaNContract.View, STProjectRaNPresenter>(), STProjectRaNContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): STProjectRaNFragment {
            val args = Bundle()
            val fragment = STProjectRaNFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var drawableS: Drawable? = null
    private var drawableX: Drawable? = null
    private var drawableM: Drawable? = null
    private val sdfPb = SimpleDateFormat("yyyy-MM")
    private var time: String = sdfPb.format(System.currentTimeMillis())
    private var type: String = "2"
    private var item: String = "4"
    private var sortWay: String = "desc"
    private var sortBy: String = "1"
    private var page = 1
    private var mBeans: MutableList<STExtenRankBean.Record> = mutableListOf()

    var isOrder1 = false
    var isOrder2 = false
    var isOrder3 = false
    override var mPresenter: STProjectRaNPresenter = STProjectRaNPresenter()
    var adapter: STProjectRanAdapter? = null
    var beginDate = ""
    var endDate = ""
    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null){
            main_layout = inflateView(R.layout.fragment_s_t_project_ra_n, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        rv_s_t_project.layoutManager = GridLayoutManager(context, 1)
        adapter = STProjectRanAdapter(context!!, mBeans)
        rv_s_t_project.adapter = adapter
    }

    override fun initData() {
        mPresenter.loadRepositories()

        // 使用代码设置drawableleft
        drawableS = resources.getDrawable(R.mipmap.icon_xspx)
        drawableX = resources.getDrawable(R.mipmap.icon_xxpx)
        drawableM = resources.getDrawable(R.mipmap.icon_mrpx)
        // / 这一步必须要做,否则不会显示.
        drawableS!!.setBounds(0, 0, drawableS!!.minimumWidth, drawableS!!.minimumHeight)
        drawableX!!.setBounds(0, 0, drawableX!!.minimumWidth, drawableX!!.minimumHeight)
        drawableM!!.setBounds(0, 0, drawableM!!.minimumWidth, drawableM!!.minimumHeight)

        setOnClickListener(tv_stp_03_)
        setOnClickListener(tv_stp_04_)
        setOnClickListener(tv_stp_05_)

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            //完成
            tv_stp_03_ -> {
                tv_stp_03_?.setCompoundDrawables(null, null, if (isOrder1) drawableS else drawableX, null  )
                tv_stp_04_?.setCompoundDrawables(null, null, drawableM, null  )
                tv_stp_05_?.setCompoundDrawables(null, null, drawableM, null  )
                sortWay = if (isOrder1) "desc" else "asc"
                isOrder1 = !isOrder1
                isOrder2 = false
                isOrder3 = false
                sortBy = "1"
                initData()
            }
            //目标
            tv_stp_04_ -> {
                tv_stp_04_?.setCompoundDrawables(null, null, if (isOrder2) drawableS else drawableX, null  )
                tv_stp_03_?.setCompoundDrawables(null, null, drawableM, null  )
                tv_stp_05_?.setCompoundDrawables(null, null, drawableM, null  )
                sortWay = if (isOrder2) "desc" else "asc"
                isOrder2 = !isOrder2
                isOrder1 = false
                isOrder3 = false
                sortBy = "2"
                initData()
            }
            //完成率
            tv_stp_05_ -> {
                tv_stp_05_?.setCompoundDrawables(null, null, if (isOrder3) drawableS else drawableX, null  )
                tv_stp_04_?.setCompoundDrawables(null, null, drawableM, null  )
                tv_stp_03_?.setCompoundDrawables(null, null, drawableM, null  )
                sortWay = if (isOrder3) "desc" else "asc"
                isOrder3 = !isOrder3
                isOrder1 = false
                isOrder2 = false
                sortBy = "3"
                initData()
            }
            else -> {
            }
        }
    }


    override fun showListData(data: STExtenRankBean.Data) {
        EventBus.getDefault().post(VISIEvent(data.records.size==10,"1"))
        adapter?.refreshData(data.records)
    }
    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
    /**
     * 时间
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun TimeEvent(event: TimeEvent) {
        time = event.time
        beginDate = event.beginDate
        endDate = event.endDate
        type = event.type
        initData()
    } /**
     * 业绩项
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun STProjectRanEvent(event: STProjectRanEvent) {
        log("${event.name}-----", "STProjectRanActivity")
        item = "${event.type}"
        initData()
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
//        map["time"] = time
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
        map["sortWay"] = sortWay//",asc:正序;desc:倒序"
        map["sortBy"] = sortBy //完成：1，目标：2，完成率：3"
        map["page"] = "$page"      //	当前页	否	int	默认1
        map["pageSize"] = "10"       //	分页大小	否	int	默认10
        return map
    }

    override fun getUrl(url_type: Int): String {
        return PerformStateImp.API_PER_PROJECT_PERFORMANCE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}