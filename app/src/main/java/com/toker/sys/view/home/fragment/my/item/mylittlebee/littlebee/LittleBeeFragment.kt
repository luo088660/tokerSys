package com.toker.sys.view.home.fragment.my.item.mylittlebee.littlebee

import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.view.home.activity.task.adapter.LittleBeEAdapter
import com.toker.sys.view.home.fragment.my.item.bean.LittleBeeBean
import kotlinx.android.synthetic.main.fragment_little_bee.*
import kotlinx.android.synthetic.main.layout_little_bee_01.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 小蜜蜂
 * @author yyx
 */

class LittleBeeFragment : BaseFragment<LittleBeeContract.View, LittleBeePresenter>(),
    LittleBeeContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): LittleBeeFragment {
            val args = Bundle()
            val fragment = LittleBeeFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private val sdf = SimpleDateFormat("yyyy/MM/dd")
    private val sdf1 = SimpleDateFormat("yyyy/MM")
    var startTime = ""
    var endTime = ""
    var condition = ""
    var page = 1
    private var eAdapter: LittleBeEAdapter? = null
    override var mPresenter: LittleBeePresenter = LittleBeePresenter()
    var mBeans: MutableList<LittleBeeBean.PageData> = mutableListOf()

    private var mDatePicker1: CustomDatePicker? = null
    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_little_bee, container!!)
        }
        return main_layout!!
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun initView() {
        rv_little_bee.layoutManager = GridLayoutManager(context, 1)
        initDatePicker()
        startTime = "${sdf1.format(System.currentTimeMillis())}/01"
        endTime = "${sdf.format(System.currentTimeMillis())}"
        tv_entire_04.text = startTime
        tv_entire_05.text = endTime
    }

    override fun initData() {
        eAdapter = LittleBeEAdapter(context!!, mBeans)
        rv_little_bee.adapter = eAdapter
        initSpringView(sv_little_bee)
        setOnClickListener(tv_my_cus_06)
        setOnClickListener(tv_entire_04)
        setOnClickListener(tv_entire_05)
        onFRefresh()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            //查询
            tv_my_cus_06 -> {
                condition = "${tv_my_cus_05.text}"
                onFRefresh()
            }
            tv_entire_04,
            tv_entire_05->{
                mDatePicker1!!.show(System.currentTimeMillis())
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

    /**
     * 数据展示
     *
     */
    override fun showData(data: LittleBeeBean.Data) {
        mBeans.addAll(data.pageData)
        eAdapter?.refreshData(mBeans)
        tv_my_cus_07.text =
            Html.fromHtml(String.format(resources.getString(R.string.tip_bee_01), data.rowTotal))
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
                tv_entire_04?.text = "${sdf.format(Date(startTime))}"
                tv_entire_05.text = "${sdf.format(Date(endTime))}"
                this@LittleBeeFragment.startTime = sdf.format(Date(startTime))
                this@LittleBeeFragment.endTime = sdf.format(Date(endTime))
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
        val map = mutableMapOf<String, String>()
        if (startTime.isNotEmpty()) {
            map["startTime"] = startTime//开始时间
        }
        if (endTime.isNotEmpty()) {
            map["endTime"] = endTime//结束时间
        }
        if (condition.isNotEmpty()) {
            map["condition"] = condition//查询条件
        }
        map["pageSize"] = "10"//页面大小
        map["page"] = "$page"//当前页码


        return map
    }

    override fun getUrl(url_type: Int): String {
        return PerformStateImp.API_PER_GBEE_LIST
    }

}// Required empty public constructor