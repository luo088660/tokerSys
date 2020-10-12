package com.toker.sys.view.home.fragment.custom.item.mycus

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.toker.sys.R
import com.toker.sys.dialog.custom.FUAllStatesDialog
import com.toker.sys.dialog.my.CallPhoneDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.custom.event.FUAllStatesEvent
import com.toker.sys.view.home.activity.custom.mcfcustomdetail.McfCustomDetailActivity
import com.toker.sys.view.home.activity.custom.newcustom.NewCustomActivity
import com.toker.sys.view.home.fragment.custom.adapter.MyCusAdapter
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import com.toker.sys.view.home.fragment.custom.event.MyCusEvent
import com.toker.sys.view.home.fragment.event.CustomProjectEvent
import com.toker.sys.view.home.fragment.event.MyOCusEvent
import kotlinx.android.synthetic.main.fragment_my_cus.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

/**
 * 意向客户
 * 我的客户
 * @author yyx
 */

class MyCusFragment : BaseFragment<MyCusContract.View, MyCusPresenter>(), MyCusContract.View {


    private var mDatePicker1: CustomDatePicker? = null
    private var timeType = 3
    private val thisTimestamp = System.currentTimeMillis()

    companion object {
        @JvmStatic
        fun newInstance(): MyCusFragment {
            val args = Bundle()
            val fragment = MyCusFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val sdfD = SimpleDateFormat("yyyy/MM/dd")
    var recordDelete: FollowCTBean.PageData? = null
    private var status: String = ""
    private var startTime: String = ""
    private var endTime: String = ""
    private var projectId: String = ""
    private var adapter: MyCusAdapter? = null
    private var mBeans = mutableListOf<FollowCTBean.PageData>()
    val beeBean = mutableListOf<FollowCTBean.PageData>()
    private var page = 1
    override var mPresenter: MyCusPresenter = MyCusPresenter()
    private var tv01: TextView? = null
    private var tv02: TextView? = null
    private var tv03: TextView? = null
    private var img04: ImageView? = null
    private var tv08: TextView? = null
    private var tv07: TextView? = null
    private var tv06: TextView? = null
    private var et05: EditText? = null
    var condition = ""
    var rowTotal = ""
    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        log("createView", "MyCusFragment")
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_my_cus, container!!)
        }
        return main_layout!!
    }


    override fun initView() {
        mPresenter.getContext(context)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        onFRefresh()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_follow_cust_01, null)
        tv01 = view.findViewById<TextView>(R.id.tv_follow_cus_01)
        tv02 = view.findViewById<TextView>(R.id.tv_follow_cus_02)
        tv03 = view.findViewById<TextView>(R.id.tv_follow_cus_03)
        img04 = view.findViewById<ImageView>(R.id.img_follow_cus_04)
        tv08 = view.findViewById<TextView>(R.id.tv_follow_cus_08)
        tv07 = view.findViewById<TextView>(R.id.tv_follow_cus_07)
        tv06 = view.findViewById<TextView>(R.id.tv_follow_cus_06)
        et05 = view.findViewById<EditText>(R.id.tv_follow_cus_05)
        tv01!!.visibility = GONE
        tv07?.text = "共 0 个客户"
        setOnClickListener(tv01!!)
        setOnClickListener(tv02!!)
        setOnClickListener(tv03!!)
        setOnClickListener(img04!!)
        setOnClickListener(tv06!!)
        setOnClickListener(tv08!!)
        //初始化时间
        initDatePicker()

        srv_my_sus.layoutManager = mPresenter.createLayoutManager()
        srv_my_sus.addItemDecoration(mPresenter.createItemDecoration())

        srv_my_sus.setSwipeMenuCreator(mPresenter.swipeMenuCreator)
        srv_my_sus.setOnItemMenuClickListener(mPresenter.mMenuItemClickListener)

        adapter = MyCusAdapter(context!!, mBeans)
        srv_my_sus.adapter = adapter
        adapter?.setHeaderView(view)
        initSpringView(sp_t_my_cus)


    }

    override fun onFRefresh() {
        super.onFRefresh()
        mBeans.clear()
        page = 1
        mPresenter.loadRepositories()
    }

    override fun onFLoadmore() {
        super.onFLoadmore()
        page ++
        mPresenter.loadRepositories()
    }


    override fun showData(data: FollowCTBean.Data) {
        beeBean.clear()
        mBeans.addAll(data.pageData)


        for (bean in mBeans) {
            if (bean.beeFlag == "0") {
                beeBean.add(bean)
            }
        }
        adapter?.refreshData(if (isBeeFlag) beeBean else mBeans)

        rowTotal = data.rowTotal
        tv07!!.text = Html.fromHtml(String.format(getString(R.string.tip_custom), rowTotal))
    }

    var isBeeFlag = false
    override fun onClick(v: View) {
        super.onClick(v)
        //当前时间
        val long2Str = DateFormatUtils.long2Str(thisTimestamp, false, this@MyCusFragment.timeType)
        when (v) {
            //全部项目
            tv01 -> {
                EventBus.getDefault().post(CustomProjectEvent(1))
            }
            //全部状态
            tv02 -> {
                FUAllStatesDialog(context!!)
            }
            //开始日期
            tv03 -> {
                mDatePicker1!!.show(System.currentTimeMillis())
            }
            //结束日期
            //查询
            img04 -> {
            }
            //展示小蜜蜂拓客  新增客户--推荐客户
            tv08 -> {
                isBeeFlag = !isBeeFlag
                adapter?.refreshData(if (isBeeFlag) beeBean else mBeans)
                tv07!!.text = Html.fromHtml(String.format(getString(R.string.tip_custom),if (isBeeFlag) beeBean.size else  rowTotal))
            }
            //查询
            tv06 -> {
                condition = "${et05?.text}"
                onFRefresh()
            }
            else -> {
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: com.toker.sys.view.home.bean.Data) {
        tv01!!.text = event.projectName
        projectId = event.projectId
        onFRefresh()

    }

    override fun deleteView(position: Int) {
        recordDelete = mBeans[position - 1]
        mPresenter.deleteView()

    }

    /**
     * 时间选择
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker1 = CustomDatePicker(context, object : CustomDatePicker.CallbackSEListen {
            override fun onTimeSelected(startTime: Long, endTime: Long, timeType: Int) {
                if( startTime == 0L||endTime==0L){
                    return
                }
                tv03?.text = "${sdfD.format(Date(startTime))}\n${sdfD.format(Date(endTime))}"
                this@MyCusFragment.startTime = sdfD.format(Date(startTime))
                this@MyCusFragment.endTime = sdfD.format(Date(endTime))
                onFRefresh()
            }
        }, 0, isVisiTab = false, isSEndTime = true)


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
        mBeans.clear()
        status = if (event.type == 0) "" else event.type.toString()
        mPresenter.loadRepositories()
    }

    override fun deleteSuccess() {
        toast("删除成功")
        //刷新列表
        onFRefresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun MyCusEvent(event: MyCusEvent) {
        when (event.type) {
            //跟进记录
            1 -> {
                val bean = event.mBeans!!
                val intent = Intent()
                intent.putExtra("isType", false)
                intent.putExtra("bean", bean)


                intent.setClass(
                    context,
                    McfCustomDetailActivity::class.java
                )

                context?.startActivity(intent)
            }
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

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
//                map["validFlag"] = ""
                if (status.isNotEmpty()) {
                    map["status"] = status
                }
                if (startTime.isNotEmpty()) {
                    map["startTime"] = startTime.replace("/","-")
                }
                if (endTime.isNotEmpty()) {
                    map["endTime"] = endTime.replace("/","-")
                }
                if (projectId.isNotEmpty()) {
                    map["projectId"] = projectId
                }
                if (condition.isNotEmpty()) {
                    map["condition"] = condition
                }
                map["pageSize"] = "10"
                map["page"] = "$page"
            }
            2 -> {
                map["id"] = recordDelete!!.id
                map["tableTag"] = recordDelete!!.tableTag
            }
            else -> {
            }
        }
        return map
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> CustomerManageImp.API_CUST_MANAGE_LIST
            2 -> CustomerManageImp.API_CUST_MANAGE_DEL
            else -> ""
        }
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }
    override fun onStop() {
        super.onStop()

    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
        mDatePicker1?.onDestroy()
    }
}