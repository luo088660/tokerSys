package com.toker.sys.view.home.fragment.my.item.mylittlebee.punchrecord

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
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.task.bean.PunchRecordBean
import com.toker.sys.view.home.activity.task.photoview.PhotoViewActivity
import com.toker.sys.view.home.fragment.event.IamgeBitmap
import com.toker.sys.view.home.fragment.my.item.adapter.PunchRecordAdapter
import kotlinx.android.synthetic.main.fragment_punch_record.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 打卡记录
 * @author yyx
 */

class PunchRecordFragment : BaseFragment<PunchRecordContract.View, PunchRecordPresenter>(),
    PunchRecordContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): PunchRecordFragment {
            val args = Bundle()
            val fragment = PunchRecordFragment()
            fragment.arguments = args
            return fragment
        }
    }
    var timestamp: Long = 0L
    var page = 1
    var time = ""
    private var mDatePicker1: CustomDatePicker? = null
    var mBeans: MutableList<PunchRecordBean.PageData> = mutableListOf()
    override var mPresenter: PunchRecordPresenter = PunchRecordPresenter()
    var adapter : PunchRecordAdapter? = null
    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_punch_record, container!!)
        }
        return main_layout!!
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initView() {
        EventBus.getDefault().register(this)
        onFRefresh()
        rv_punch_record.layoutManager = GridLayoutManager(context!!,1)
        rv_punch_record.addItemDecoration(getItemDecoration())
        initSpringView(sv_punch_record)
        setOnClickListener(tv_entir_05)
        initDatePicker()

    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            tv_entir_05 -> {
                mDatePicker1?.show(if(timestamp != 0L)timestamp else System.currentTimeMillis())
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

    override fun onFLoadmore() {
        super.onFLoadmore()
        page++
        mPresenter.loadRepositories()
    }
    override fun initData() {
        adapter = PunchRecordAdapter(context!!, mBeans)
        rv_punch_record.adapter = adapter
    }

    override fun showSuccesData(data: PunchRecordBean.Data) {
        mBeans.addAll(data.pageData)
        adapter?.reFreshData(mBeans)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun bitmapDrawable(event: IamgeBitmap){
        val intent = Intent(context, PhotoViewActivity::class.java)
        intent.putExtra("bmpPath",event.bmpPath)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {
        // 通过时间戳初始化日期，毫秒级别
        mDatePicker1 = CustomDatePicker(context, object : CustomDatePicker.CallbackListen {
            override fun onTimeSelected(timestamp: Long, timeType: Int) {
                log("onTimeSelected---$timeType", "STProjectRanActivity")
                this@PunchRecordFragment.timestamp = timestamp
                this@PunchRecordFragment.time = DateFormatUtils.long2Str(timestamp, false, timeType)
                tv_entir_05?.text = time.replace("-", "/")
                onFRefresh()

            }
        }, 5, false, false)


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
        map["page"] = "$page"//当前页码
        if (!TextUtils.isEmpty(time)){
            map["attendanceTime"] = time
        }
        return map
    }
    override fun getUrl(url_type: Int): String {
        return PerformStateImp.API_PER_RECORD_LIST
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}// Required empty public constructor