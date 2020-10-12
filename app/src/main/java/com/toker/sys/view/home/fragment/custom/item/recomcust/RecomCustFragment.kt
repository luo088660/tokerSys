package com.toker.sys.view.home.fragment.custom.item.recomcust

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.dialog.custom.FUAllRecUserDialog
import com.toker.sys.dialog.my.CallPhoneDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.view.home.activity.custom.bean.RecomCustBean
import com.toker.sys.view.home.activity.custom.event.FUAllRecUserEvent
import com.toker.sys.view.home.activity.custom.fcustomdetail.FCustomDetailActivity
import com.toker.sys.view.home.activity.custom.mcfcustomrecom.McfCustomRecomActivity
import com.toker.sys.view.home.activity.custom.newcustom.NewCustomActivity
import com.toker.sys.view.home.fragment.custom.adapter.RecomCustAdapter
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import com.toker.sys.view.home.fragment.custom.event.MyCusEvent
import com.toker.sys.view.home.fragment.event.CustomProjectEvent
import kotlinx.android.synthetic.main.fragment_recom_cust.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

/**
 * 推荐客户
 * @author yyx
 */

class RecomCustFragment : BaseFragment<RecomCustContract.View, RecomCustPresenter>(),
    RecomCustContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): RecomCustFragment {
            val args = Bundle()
            val fragment = RecomCustFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val sdf = SimpleDateFormat("yyyy/MM/dd")
    private val sdf1 = SimpleDateFormat("yyyy-MM-dd")
    private var startTime: String = ""
    private var condition: String = ""
    private var status: String = ""
    private var projectId: String = ""
    private var endTime: String = ""
    private var phone: String? = ""
    private var mDatePicker1: CustomDatePicker? = null
    private var tv04: TextView? = null
    private var btn01: TextView? = null
    private var tv02: TextView? = null
    private var tv03: TextView? = null
    private var tv07: TextView? = null
    private var ll04: LinearLayout? = null
    private var et01: EditText? = null
    var page = "1"
    override var mPresenter: RecomCustPresenter = RecomCustPresenter()
    private var mBeans = mutableListOf<RecomCustBean.PageData>()
    private var adapter: RecomCustAdapter? = null
    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        log("createView", "RecomCustFragment")
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_recom_cust, container!!)
        }
        return main_layout!!
    }

    override fun initView() {


        rv_t_recom_cust.layoutManager = GridLayoutManager(context, 1)
        phone = arguments?.getString("phone")

    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        if (!TextUtils.isEmpty(phone)){
            condition = phone!!
        }
        onFRefresh()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_recom_sust_01, null)
        tv02 = view.findViewById<TextView>(R.id.tv_receivde1_02)
        tv03 = view.findViewById<TextView>(R.id.tv_receivde1_03)
        tv07 = view.findViewById<TextView>(R.id.tv_receivde1_07)
        ll04 = view.findViewById<LinearLayout>(R.id.ll_receivde1_04)
        et01 = view.findViewById<EditText>(R.id.tv_receivde1_01)
        tv04 = view.findViewById<TextView>(R.id.tv_receivde1_04)
        btn01 = view.findViewById<TextView>(R.id.btn_receivde1_01)
        setOnClickListener(tv02!!)
        setOnClickListener(tv03!!)
        setOnClickListener(btn01!!)
        setOnClickListener(ll04!!)
        setOnClickListener(tv04!!)
        tv07?.text = "共 0 个客户"
        adapter = RecomCustAdapter(context!!, mBeans)
        rv_t_recom_cust.adapter = adapter
        rv_t_recom_cust.addItemDecoration(getItemDecoration())
        adapter!!.setHeaderView(view)
        initSpringView(sp_t_recom_cust)
        //初始化时间
        initDatePicker()
        if (!TextUtils.isEmpty(phone)){
            et01?.setText(phone)
        }

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

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            tv02->EventBus.getDefault().post(CustomProjectEvent(1))
            tv04 -> {
                mDatePicker1!!.show(System.currentTimeMillis())
            }
            btn01->{
                condition = "${et01!!.text}"
                onFRefresh()
            }
            tv03->{
                FUAllRecUserDialog(context!!)
            }
            else -> {
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: com.toker.sys.view.home.bean.Data) {
        tv02!!.text = event.projectName
        projectId = event.projectId
        onFRefresh()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun MyCusEvent(event: MyCusEvent) {
        when (event.type) {
            //拨打电话
            2 -> {
                CallPhoneDialog(activity!!, event.mBeans!!.phone)
            }
            //推荐客户
            3 -> {
                val intent = Intent(activity, NewCustomActivity::class.java)
                intent.putExtra("type", true)
                intent.putExtra("mBean", event.mDBean)
                activity!!.startActivity(intent)
            }
            else -> {
            }
        }
    }
    override fun showData(data: RecomCustBean.Data) {
        mBeans.addAll(data.pageData)
        tv07!!.text = Html.fromHtml(String.format(getString(R.string.tip_custom), data.rowTotal))
        adapter!!.refreshData(mBeans)

    }

    //查看客户详情
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun recomCustBean(event:RecomCustBean.PageData){
        val intent = Intent(context,  McfCustomRecomActivity::class.java)
        intent.putExtra("isType",true)
        val bean = FollowCTBean.PageData(event.id,event.tableTag,event.outRecommend,event.outVisit)
        intent.putExtra("bean",bean)
        startActivity(intent)


    }
    //状态

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun FUAllRecUserEvent(event: FUAllRecUserEvent) {
        tv03?.text = event.name
       status =  when (event.name) {
            "推荐成功"->"1"
            "到访"->"2"
            "认筹"->"3"
            "认购"->"4"
            "签约"->"5"
            "过期" -> "6"
            else -> ""
        }
        onFRefresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun MyCusEvent(event: FollowCTBean.PageData) {
        val intent = Intent(activity, FCustomDetailActivity::class.java)
        intent.putExtra("type", 4)
        intent.putExtra("bean", event)
        activity!!.startActivity(intent)
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
                tv04?.text = "${sdf.format(Date(startTime))}\n${sdf.format(Date(endTime))}"
                this@RecomCustFragment.startTime = sdf.format(Date(startTime))
                this@RecomCustFragment.endTime = sdf.format(Date(endTime))
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

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
//                map["validFlag"] = "0"
                if (status.isNotEmpty()) {
                    map["status"] = status
                }
                if (projectId.isNotEmpty()) {
                    map["projectId"] = projectId
                }
                if (condition.isNotEmpty()) {
                    map["condition"] = condition
                }
                if (startTime.isNotEmpty()) {
                    map["startTime"] = startTime.replace("/","-")
                }
                if (startTime.isNotEmpty()) {
                    map["endTime"] = endTime.replace("/","-")
                }
                map["pageSize"] = "10"
                map["page"] = page
            }
            else -> {
            }
        }
        return map
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
    override fun getUrl(url_type: Int): String {

        return CustomerManageImp.API_CUST_CUSTOMER_LIST
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}// Required empty public constructor1