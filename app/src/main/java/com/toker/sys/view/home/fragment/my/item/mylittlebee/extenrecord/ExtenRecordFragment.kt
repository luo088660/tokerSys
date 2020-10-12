package com.toker.sys.view.home.fragment.my.item.mylittlebee.extenrecord

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.custom.mcfcustomdetail.McfCustomDetailActivity
import com.toker.sys.view.home.activity.custom.newcustom.NewCustomActivity
import com.toker.sys.view.home.activity.task.adapter.ExtenRecordAdapter
import com.toker.sys.view.home.activity.task.bean.ExtenRecordBean
import com.toker.sys.view.home.fragment.custom.event.MyCusEvent
import kotlinx.android.synthetic.main.fragment_exten_record.*
import kotlinx.android.synthetic.main.fragment_punch_record.*
import kotlinx.android.synthetic.main.layout_custo_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

/**
 * 拓客记录
 * @author yyx
 */

class ExtenRecordFragment : BaseFragment<ExtenRecordContract.View, ExtenRecordPresenter>(), ExtenRecordContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): ExtenRecordFragment {
            val args = Bundle()
            val fragment = ExtenRecordFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private val sdf = SimpleDateFormat("yyyy-MM-dd")
    var timestamp: Long = 0L
    var startTime = ""
    var endTime = ""
    private var mDatePicker1: CustomDatePicker? = null
    var page = 1
    var mBeans:MutableList<ExtenRecordBean.PageData>  = mutableListOf()
    var adapter : ExtenRecordAdapter? = null
    override var mPresenter: ExtenRecordPresenter = ExtenRecordPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null){
            main_layout = inflateView(R.layout.fragment_exten_record, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)

        rv_exten_record.layoutManager = GridLayoutManager(context,1)
        rv_exten_record.addItemDecoration(getItemDecoration())
        initSpringView(sv_exten_record)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        onFRefresh()
        adapter = ExtenRecordAdapter(context!!, mBeans)
        rv_exten_record.adapter = adapter
        setOnClickListener(tv_entiret_05)
        initDatePicker()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            tv_entiret_05 -> {
                mDatePicker1?.show(System.currentTimeMillis())
            }
            else -> {
            }
        }
    }
    override fun onFRefresh() {
        super.onFRefresh()
        page=1
        mBeans.clear()
        mPresenter.loadRepositories()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun MyCusEvent(event: MyCusEvent) {
        val bean = event.mBeans!!
        val intent = Intent(context, McfCustomDetailActivity::class.java)
        intent.putExtra("isType", false)
        intent.putExtra("bean", bean)
        startActivity(intent)
    }
    override fun onFLoadmore() {
        super.onFLoadmore()
        page++
        mPresenter.loadRepositories()
    }

    override fun showSuccesData(data: ExtenRecordBean.Data) {
        mBeans.addAll(data.pageData)
        adapter?.refreshData(mBeans)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {
        // 通过时间戳初始化日期，毫秒级别
        mDatePicker1 = CustomDatePicker(context, object :CustomDatePicker.CallbackSEListen  {
            override fun onTimeSelected( startTime: Long, endTime: Long, timeType: Int) {
                log("onTimeSelected---$timeType", "STProjectRanActivity")
                if(startTime ==0L ||endTime==0L){
                    return
                }
                this@ExtenRecordFragment.timestamp = timestamp
                tv_entiret_05?.text = "${sdf.format(Date(startTime))}至${sdf.format(Date(endTime))}".replace("-", "/")
                this@ExtenRecordFragment.startTime = sdf.format(Date(startTime))
                this@ExtenRecordFragment.endTime = sdf.format(Date(endTime))
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
    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map  = mutableMapOf<String,String>()

        map["pageSize"] = "10"//页面大小
        if (startTime.isNotEmpty()) {
            map["startTime"] = startTime//开始时间
        }
        if (endTime.isNotEmpty()) {
            map["endTime"] = endTime//结束时间
        }
        map["page"] = "$page"//当前页码
        return map
    }
    override fun getUrl(url_type: Int): String {
        return  PerformStateImp.API_PER_CUSTOMER_LIST
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}// Required empty public constructor