package com.toker.sys.view.home.fragment.custom.item.followcust

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.dialog.custom.FUAllStatesDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.view.home.activity.custom.event.FUAllStatesEvent
import com.toker.sys.view.home.activity.custom.fcustomdetail.FCustomDetailActivity
import com.toker.sys.view.home.fragment.custom.adapter.FollowCustAdapter
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import com.toker.sys.view.home.fragment.custom.bean.FollowCustBean
import com.toker.sys.view.home.fragment.custom.event.MyCusEvent
import com.toker.sys.view.home.fragment.event.CustomProjectEvent
import kotlinx.android.synthetic.main.fragment_follow_cust.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

/**
 * 拓客管理员
 * 跟进中的客户
 * @author yyx
 */

class FollowCustFragment : BaseFragment<FollowCustContract.View, FollowCustPresenter>(),
    FollowCustContract.View {

    private var mDatePicker1: CustomDatePicker? = null
    private var timeType = 3
    private val thisTimestamp = System.currentTimeMillis()
    var startTime = ""
    var condition = ""
    var endTime = ""
    var isBeeFlag = false
    private val sdf = SimpleDateFormat("yyyy/MM/dd")
    private val sdf1 = SimpleDateFormat("yyyy-MM-dd")

    companion object {
        @JvmStatic
        fun newInstance(): FollowCustFragment {
            val args = Bundle()
            val fragment = FollowCustFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var page = "1"
    private var tv01: TextView? = null
    private var tv02: TextView? = null
    private var tv03: TextView? = null
    private var tv04: TextView? = null
    private var img04: ImageView? = null
    private var tv05: EditText? = null
    private var tv06: TextView? = null
    private var tv07: TextView? = null
    private var tv08: TextView? = null

    val beeBean = mutableListOf<FollowCustBean.PageData>()
    private var mBeans = mutableListOf<FollowCustBean.PageData>()
    private var adapter: FollowCustAdapter? = null
    override var mPresenter: FollowCustPresenter = FollowCustPresenter()

    var rowTotal = ""
    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_follow_cust, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)
//        mPresenter.loadRepositories()

        rv_t_follow_cust.layoutManager = GridLayoutManager(context, 1)
    }

    override fun onStart() {
        super.onStart()
        onFRefresh()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_follow_cust_01, null)
        tv03?.hint = "开始时间\n结束时间"
        tv01 = view.findViewById<TextView>(R.id.tv_follow_cus_01)
        tv02 = view.findViewById<TextView>(R.id.tv_follow_cus_02)
        tv03 = view.findViewById<TextView>(R.id.tv_follow_cus_03)
        tv04 = view.findViewById<TextView>(R.id.tv_follow_cus_04)
        img04 = view.findViewById<ImageView>(R.id.img_follow_cus_04)
        tv05 = view.findViewById<EditText>(R.id.tv_follow_cus_05)
        tv06 = view.findViewById<TextView>(R.id.tv_follow_cus_06)
        tv07 = view.findViewById<TextView>(R.id.tv_follow_cus_07)
        tv08 = view.findViewById<TextView>(R.id.tv_follow_cus_08)
        tv01?.visibility=GONE
        tv02?.visibility=GONE
        setOnClickListener(tv01!!)
        setOnClickListener(tv02!!)
        setOnClickListener(tv03!!)
        setOnClickListener(tv04!!)
        setOnClickListener(img04!!)
        setOnClickListener(tv06!!)
        setOnClickListener(tv08!!)

        adapter = FollowCustAdapter(context!!, mBeans)
        rv_t_follow_cust.adapter = adapter
        rv_t_follow_cust.addItemDecoration(getItemDecoration())
        adapter!!.setHeaderView(view)


        //初始化时间
        initDatePicker()
        initSpringView(sp_t_follow_cust)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            //全部项目
            tv01 -> EventBus.getDefault().post(CustomProjectEvent(1))
            //全部状态
            tv02 -> {
                FUAllStatesDialog(context!!)
            }
            //开始日期
            tv03,
            img04

            -> {
                mDatePicker1!!.show(System.currentTimeMillis())
            }
            //查询
            tv06 -> {
                condition = "${tv05!!.text}"
                onFRefresh()
            }
            //展示小蜜蜂拓客  新增客户--推荐客户
            tv08 -> {
                /*val intent = Intent(activity, NewCustomActivity::class.java)
                intent.putExtra("type", true)
                activity!!.startActivity(intent)*/
                isBeeFlag = !isBeeFlag
                adapter?.refreshData(if (isBeeFlag) beeBean else mBeans)
                tv07!!.text = Html.fromHtml(String.format(getString(R.string.tip_custom),if (isBeeFlag) beeBean.size else  rowTotal))
            }
            else -> {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: com.toker.sys.view.home.bean.Data) {
        tv01!!.text = event.projectName
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun MyCusEvent(event: MyCusEvent) {
        val intent = Intent(activity, FCustomDetailActivity::class.java)
        intent.putExtra("type", 1)
        intent.putExtra(
            "bean",
            FollowCTBean.PageData(
                event.date!!.id,
                event.date!!.tableTag,
                event.date!!.notFollowDays.toInt()
            )
        )
        activity!!.startActivity(intent)
    }

    override fun onFRefresh() {
        super.onFRefresh()
        mBeans.clear()
        page = "1"
        mPresenter.loadRepositories()
    }

    override fun onFLoadmore() {
        super.onFLoadmore()

        var toInt = page.toInt()
        toInt++
        page = toInt.toString()
        mPresenter.loadRepositories()
    }

    override fun showData(data: FollowCustBean.Data) {
        mBeans.addAll(data.pageData)
        rowTotal = data.rowTotal
        tv07!!.text = Html.fromHtml(String.format(getString(R.string.tip_custom), rowTotal))
        for (bean in mBeans) {
            if (bean.beeFlag == "0") {
                beeBean.add(bean)
            }
        }

        adapter?.refreshData(if (isBeeFlag) beeBean else mBeans)


    }

    /**
     * 时间选择
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker1 = CustomDatePicker(context, object : CustomDatePicker.CallbackSEListen {
            override fun onTimeSelected(
                startTime: Long, endTime: Long, timeType: Int
            ) {
                if( startTime == 0L||endTime==0L){
                    return
                }
                LogUtils.d(TAG, "startTime:$startTime ");
                LogUtils.d(TAG, "endTime: $endTime");
                tv03?.text = "${sdf.format(Date(startTime))}\n${sdf.format(Date(endTime))}"
                this@FollowCustFragment.startTime = sdf1.format(Date(startTime))
                this@FollowCustFragment.endTime = sdf1.format(Date(endTime))
                onFRefresh()
            }

        }, 0, false, true)


        // 不允许点击屏幕或物理返回键关闭
//        mDatePicker1?.setCancelable(false)
        // 不显示时和分
        mDatePicker1?.setCanShowPreciseTime(false)
        // 不允许循环滚动
        mDatePicker1?.setScrollLoop(true)
        // 不允许滚动动画
        mDatePicker1?.setCanShowAnim(false)

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun FUAllStatesEvent(event: FUAllStatesEvent) {
        tv02?.text = event.name
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                if (condition.isNotEmpty()) {
                    map["condition"] = condition
                }
                map["validFlag"] = "0"
                map["status"] = "1"
                if (!startTime.isNullOrEmpty()) {
                    map["startTime"] = startTime
                }
                if (!endTime.isNullOrEmpty()) {
                    map["endTime"] = endTime
                }
                map["pageSize"] = "10"
                map["page"] = page
            }
            else -> {
            }
        }
        return map
    }

    override fun getUrl(url_type: Int): String {

        return CustomerManageImp.API_CUST_MANAGE_LIST
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}