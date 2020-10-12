package com.toker.sys.view.home.fragment.custom.item.followup

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.dialog.custom.FUAllRecUserDialog
import com.toker.sys.dialog.custom.FUAllStatesDialog
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.custom.event.FUAllRecUserEvent
import com.toker.sys.view.home.activity.custom.event.FUAllStatesEvent
import com.toker.sys.view.home.bean.FollowUpBean
import com.toker.sys.view.home.bean.ProjectNoBean
import com.toker.sys.view.home.fragment.custom.adapter.FollowUpAdapter
import com.toker.sys.view.home.fragment.event.CustomProjectEvent
import kotlinx.android.synthetic.main.fragment_follow_up.*
import kotlinx.android.synthetic.main.layout_t_received_01.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat

/**
 * 拓客中层
 * 跟进中的客户
 * @author yyx
 */

class FollowUpFragment : BaseFragment<FollowUpContract.View, FollowUpPresenter>(),
    FollowUpContract.View {

    private var mDatePicker2: CustomDatePicker? = null
    private var timeType = 0
    private var orderBy = "totalNum"
    private var page = 1
    private var orderWay = "ASC"
    private var firstNum = ""
    private var secondNum = ""
    val sdfY = SimpleDateFormat("yyyy")
    val sdfM = SimpleDateFormat("MM")
    val sdfD = SimpleDateFormat("dd")
    private var timestamp = System.currentTimeMillis()

    private var mBeans = mutableListOf<FollowUpBean.Record>()
    private var upAdapter: FollowUpAdapter? = null

    companion object {
        @JvmStatic
        fun newInstance(): FollowUpFragment {
            val args = Bundle()
            val fragment = FollowUpFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var tv01: TextView? = null
    private var tv02: TextView? = null
    private var tv03: TextView? = null
    private var tv04: TextView? = null
    private var tv05: TextView? = null
    private var img06: ImageView? = null
    private var tv07: TextView? = null
    private var btn02: TextView? = null
    private var btn01: TextView? = null
    private var tv11: TextView? = null
    private var tv12: TextView? = null
    var isOrder1 = false
    var isOrder2 = false
    var drawableS: Drawable? = null
    var drawableX: Drawable? = null
    var drawable: Drawable? = null
    override var mPresenter: FollowUpPresenter = FollowUpPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_follow_up, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        rv_t_follow.layoutManager = GridLayoutManager(context, 1)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {

        drawableX = resources.getDrawable(R.mipmap.icon_xxpx)
        drawable = resources.getDrawable(R.mipmap.icon_mrpx)
        drawableS = resources.getDrawable(R.mipmap.icon_xspx)
        // / 这一步必须要做,否则不会显示.
        drawable!!.setBounds(0, 0, drawable!!.minimumWidth, drawable!!.minimumHeight)
        drawableS!!.setBounds(0, 0, drawableS!!.minimumWidth, drawableS!!.minimumHeight)
        drawableX!!.setBounds(0, 0, drawableX!!.minimumWidth, drawableX!!.minimumHeight)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_follow_up_00, null)

        tv11 = view.findViewById<TextView>(R.id.tv_follow_up_011)
        tv12 = view.findViewById<TextView>(R.id.tv_follow_up_012)
        btn01 = view.findViewById<TextView>(R.id.tv_follow_up2_01)
        btn02 = view.findViewById<TextView>(R.id.tv_follow_up2_02)
        tv01 = view.findViewById<TextView>(R.id.tv_follow_up_01)
        tv02 = view.findViewById<TextView>(R.id.tv_follow_up_02)
        tv03 = view.findViewById<TextView>(R.id.tv_follow_up_03)
        tv04 = view.findViewById<TextView>(R.id.tv_follow_up_04)
        val tv044 = view.findViewById<TextView>(R.id.tv_follow_up_044)
        tv05 = view.findViewById<TextView>(R.id.tv_follow_up_05)
        img06 = view.findViewById<ImageView>(R.id.tv_follow_up_06)
        tv07 = view.findViewById<TextView>(R.id.tv_follow_up_07)



        tv04?.visibility = GONE
        tv044.visibility = GONE
        upAdapter = FollowUpAdapter(context!!, mBeans)
        rv_t_follow.adapter = upAdapter
        upAdapter!!.setHeaderView(view)
        tv05?.hint = "选择日期"

        setOnClickListener(tv01!!)
        setOnClickListener(tv02!!)
        setOnClickListener(tv03!!)
        setOnClickListener(tv04!!)
        setOnClickListener(tv05!!)
        setOnClickListener(img06!!)
        setOnClickListener(btn02!!)
        setOnClickListener(btn01!!)


        setOnClickListener(btn_follow_up)
        //初始化时间
        initDatePicker()
        initSpringView(sp_t_follow)

        mPresenter.loadRepositories()
    }

    override fun onClick(v: View) {
        super.onClick(v)

        when (v) {
//            btn_follow_up -> sl_follow_up.fullScroll(View.FOCUS_UP)
            //全部项目
            tv01 -> {
                EventBus.getDefault().post(CustomProjectEvent(1))
            }
            //全部状态
            tv02 -> {
                FUAllStatesDialog(context!!)
            }
            //全部推荐用户
            tv03 -> {
                FUAllRecUserDialog(context!!)
            }
            //结束日期
            tv05 -> {
                mDatePicker2!!.show(timestamp)
            }
            //确定
            img06 -> {

            }
            btn01 -> {
                orderWay = if (!isOrder1) "ASC" else "DESC"
                orderBy = "totalNum"
                btn01?.setCompoundDrawables(
                    null,
                    null,
                    if (isOrder1) drawableS else drawableX,
                    null
                )
                isOrder1 = !isOrder1
                isOrder2 = false
                btn02?.setCompoundDrawables(null, null, drawable, null)
                onFRefresh()
            }
            btn02 -> {
                orderWay = if (!isOrder2) "ASC" else "DESC"
                orderBy = "recommendNum "
                btn02?.setCompoundDrawables(
                    null,
                    null,
                    if (isOrder2) drawableS else drawableX,
                    null
                )
                isOrder2 = !isOrder2
                isOrder1 = false
                btn01?.setCompoundDrawables(null, null, drawable, null)
                onFRefresh()
            }
            else -> {
            }
        }
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

    override fun showData(data: FollowUpBean.Data) {

        tv07!!.text = Html.fromHtml(String.format(getString(R.string.tip_custom), data.page.total))
        mBeans.addAll(data.page.records)
        upAdapter?.refreshData(mBeans, firstNum, secondNum)
        val summary = data.summary

        try {
            tv11?.text = summary.totalNum.toString()
            tv12?.text = summary.recommendNum.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            tv11?.text = "0"
            tv12?.text = "0"
        }

    }

    var projectId = ""
    //全部项目
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: com.toker.sys.view.home.bean.Data) {
        tv01!!.text = event.projectName
        projectId = event.projectId
        onFRefresh()
    }

    /**
     * 全部状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun FUAllStatesEvent(event: FUAllStatesEvent) {
        firstNum = when (event.type) {
            0 -> ""
            1 -> "noRecommendNum"
            else -> "recommendNum"
        }
        tv02?.text = event.name
        btn01?.text = if (event.name.contains("全部")) "客户总数" else event.name
        onFRefresh()
    }

    /**
     * 全部推荐用户
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun FUAllRecUserEvent(event: FUAllRecUserEvent) {
//        展示字段	是	String	reportNum(报备数)、visitNum(到访数)、recruitNum(认筹数)、subscriptionNum(认购数)、signingNum(签约数)、expiredNum(过期)数，只填写英文的字段
        secondNum = when (event.name) {
            "全部" -> {
                ""
            }
            "推荐成功" -> {
                "reportNum"
            }
            "到访" -> {
                "visitNum"
            }
            "认筹" -> {
                "recruitNum"
            }
            "认购" -> {
                "subscriptionNum"
            }
            "签约" -> {
                "signingNum"
            }
            //过期
            else -> {
                "expiredNum"
            }
        }
        tv03?.text = event.name
        btn02?.text = if (event.name.contains("全部")) "已推荐客户" else event.name
        onFRefresh()
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    /**
     * 时间选择
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {


        // 通过时间戳初始化日期，毫秒级别
        mDatePicker2 = CustomDatePicker(context, object : CustomDatePicker.CallbackListen {
            override fun onTimeSelected(timestamp: Long, timeType: Int) {
                this@FollowUpFragment.timestamp = timestamp
                this@FollowUpFragment.timeType = timeType
                tv05?.text =
                    DateFormatUtils.long2Str(timestamp, false, this@FollowUpFragment.timeType)
                onFRefresh()
            }
        }, 4, isVisiTab = true, isSEndTime = false)

        // 不允许点击屏幕或物理返回键关闭
//        mDatePicker2?.setCancelable(false)
        // 不显示时和分
        mDatePicker2?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mDatePicker2?.setScrollLoop(true)
        // 不允许滚动动画
        mDatePicker2?.setCanShowAnim(false)

    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        if (!TextUtils.isEmpty(firstNum)) {
            map["firstNum"] = firstNum//展示字段	是	String	 recommendNum (已推荐客户数)、noRecommendNum（未推荐客户）
        }
        if (!TextUtils.isEmpty(secondNum)) {
            map["secondNum"] =
                secondNum//	展示字段	是	String	reportNum(报备数)、visitNum(到访数)、recruitNum(认筹数)、subscriptionNum(认购数)、signingNum(签约数)、expiredNum(过期)数，只填写英文的字段\
        }
        if (!TextUtils.isEmpty(projectId)) {
            map["projectId"] = projectId
        }
        map["sortBy"] = if (orderBy == "totalNum") {
            firstNum
            //recommendNum (已推荐客户数)、noRecommendNum（未推荐客户）  reportNum(报备数)、visitNum(到访数)、recruitNum(认筹数)、subscriptionNum(认购数)、signingNum(签约数)、expiredNum(过期)数，只填写英文的字段
        } else {
            secondNum
        }
        map["dateType"] = when (timeType) {
            1 -> "year"
            2 -> "month"
            3 -> "day"
            else -> ""
        }//	日期类型	是	String	 year、 month、 day 表示查询的日期类型
        when (timeType) {
            1 -> {
                map["year"] = sdfY.format(timestamp)//具体年份	是	String	具体年份，如2019
            }
            2 -> {
                map["year"] = sdfY.format(timestamp)//具体年份	是	String	具体年份，如2019
                map["month"] =
                    sdfM.format(timestamp)//具体月份	否	String	如dateType为月或日，则必填。填写内容为具体月份，如 10
            }
            3 -> {
                map["year"] = sdfY.format(timestamp)//具体年份	是	String	具体年份，如2019
                map["month"] =
                    sdfM.format(timestamp)//具体月份	否	String	如dateType为月或日，则必填。填写内容为具体月份，如 10
                map["day"] = sdfD.format(timestamp)//具体某天	否	String	如dateType为日，则必填。填写内容为具体某天，如 10
            }
        }

        map["pageSize"] = "10"
        map["page"] = page.toString()
        map["sortWay"] = orderWay//ASC(顺序)、DESC（逆序），只填写英文的字段
        return map
    }

    override fun getUrl(url_type: Int): String {

        return CustomerManageImp.API_CUSTOMER_MGRUSER_PROJECT_ONFOLLOW
    }


    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
        mDatePicker2?.onDestroy()
    }
}