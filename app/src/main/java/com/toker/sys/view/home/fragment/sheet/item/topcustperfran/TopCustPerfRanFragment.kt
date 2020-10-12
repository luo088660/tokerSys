package com.toker.sys.view.home.fragment.sheet.item.topcustperfran

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.R
import com.toker.sys.dialog.ProjectListDialog
import com.toker.sys.dialog.sheet.MemberDialog
import com.toker.sys.dialog.sheet.TurnoverDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.utils.network.params.PerformStateImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.view.datepicker.CustomDatePicker
import com.toker.sys.utils.view.datepicker.DateFormatUtils
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.home.activity.sheet.event.MemberRanEvent
import com.toker.sys.view.home.activity.sheet.event.STProjectRanEvent
import com.toker.sys.view.home.activity.sheet.event.TimeEvent
import com.toker.sys.view.home.activity.sheet.event.VISIEvent
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.fragment.my.item.bean.GroupListBean
import com.toker.sys.view.home.fragment.sheet.event.SheetEvent
import com.toker.sys.view.home.fragment.sheet.item.topcustperfran.stextenrank.STExtenRanKFragment
import com.toker.sys.view.home.fragment.sheet.item.topcustperfran.stprojectran.STProjectRaNFragment
import com.toker.sys.view.home.fragment.sheet.item.topcustperfran.stteamrank.STTeaMRankFragment
import kotlinx.android.synthetic.main.fragment_s_t_sheet.*
import kotlinx.android.synthetic.main.fragment_top_cust_perf_ran.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_my_performance.*
import kotlinx.android.synthetic.main.layout_pion_perfor_rank.*
import kotlinx.android.synthetic.main.layout_pion_perfor_rank.tv_ver_01
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat

/**
 * 拓客中层
 * 拓客管理员
 * [拓客业绩排名]
 * @author yyx
 */

class TopCustPerfRanFragment : BaseFragment<TopCustPerfRanContract.View, TopCustPerfRanPresenter>(),
    TopCustPerfRanContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): TopCustPerfRanFragment {
            val args = Bundle()
            val fragment = TopCustPerfRanFragment()
            fragment.arguments = args
            return fragment
        }
    }
    private var type = "2"
    var mGroupList = mutableListOf<Data>()
    private val sdf = SimpleDateFormat("yyyy-MM")
    var time = sdf.format(System.currentTimeMillis())
    var isOrder1 = false
    var isOrder2 = false
    var isOrder3 = false
    var drawableS: Drawable? = null
    var drawableX: Drawable? = null
    var datePicker: CustomDatePicker? = null
    override var mPresenter: TopCustPerfRanPresenter = TopCustPerfRanPresenter()

    override fun onNetworkLazyLoad() {
    }

    private var timestamp = System.currentTimeMillis()
    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_top_cust_perf_ran, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)
        mPresenter.loadRepositories()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        tv_poin_date.text = time.replace("-", "/")
        // 使用代码设置drawableleft
        drawableS = resources.getDrawable(R.mipmap.icon_xspx)
        drawableX = resources.getDrawable(R.mipmap.icon_xxpx)
        // / 这一步必须要做,否则不会显示.
        drawableS!!.setBounds(0, 0, drawableS!!.minimumWidth, drawableS!!.minimumHeight)
        drawableX!!.setBounds(0, 0, drawableX!!.minimumWidth, drawableX!!.minimumHeight)

        setOnClickListener(ll_poin_date)
        setOnClickListener(tv_ver_01)
        setOnClickListener(tv_ver_02)
        setOnClickListener(tv_ver_03)
        setOnClickListener(tv_ver_04)
        setOnClickListener(tv_ver_05)
        setOnClickListener(tv_ver_06)
        setOnClickListener(tv_ver_07)
        setOnClickListener(tv_perfor_rank_viewall)
        initDatePicker()
//        setIntentFragment(R.id.fl_pion_perfor_rank, STProjectRaNFragment.newInstance(), Bundle())
        memberRanEvent(MemberRanEvent(1, "项目"))
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            //返回顶部
            btn_sheet -> sl_sheent.fullScroll(View.FOCUS_UP)
            //我的消息
            ll_msg -> EventBus.getDefault().post(MainEvent(16))
            //时间选择
            ll_poin_date -> datePicker?.show(timestamp)

            // 成员 项目 团队 拓客员删选
            tv_ver_01 -> MemberDialog(context!!)
            //全部项目
            tv_ver_02 -> EventBus.getDefault().post(SheetEvent(9, 2))
            //全部团队
            tv_ver_03 -> ProjectListDialog(context!!, this.mGroupList!!, true)
            //成交额
            tv_ver_04 -> TurnoverDialog(context!!)
            //完成
            tv_ver_05 -> {
                tv_ver_05.setCompoundDrawables(
                    null,
                    null,
                    if (isOrder1) drawableS else drawableX,
                    null
                )
                isOrder1 = !isOrder1
            }
            //目标
            tv_ver_06 -> {
                tv_ver_06.setCompoundDrawables(
                    null,
                    null,
                    if (isOrder2) drawableS else drawableX,
                    null
                )
                isOrder2 = !isOrder2
            }
            //完成率
            tv_ver_07 -> {
                tv_ver_07.setCompoundDrawables(
                    null,
                    null,
                    if (isOrder3) drawableS else drawableX,
                    null
                )
                isOrder3 = !isOrder3
            }
            //查看全部
            tv_perfor_rank_viewall -> {
                when ("${tv_ver_01.text}") {
                    "项目" -> {
                        EventBus.getDefault().post(SheetEvent(2))
                    }
                    "团队" -> {
                        EventBus.getDefault().post(SheetEvent(3))
                    }
                    //拓客员
                    else -> {
                        EventBus.getDefault().post(SheetEvent(4))
                    }
                }

            }
            else -> {
            }
        }
    }


    //我的团队
    override fun showGroupList(data: MutableList<GroupListBean.Data>) {
        mGroupList.clear()
        data.forEach {
            mGroupList.add(Data(it.id, it.projectId, it.groupName, ""))
        }

    }


    /**
     * 时间选择
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initDatePicker() {
       /* datePicker = CustomDatePicker(context, object : CustomDatePicker.CallbackListen {
            override fun onTimeSelected(timestamp: Long, timeType: Int) {
                val time = DateFormatUtils.long2Str(timestamp, false, timeType)
                tv_poin_date.text = time.replace("-", "/")
                EventBus.getDefault().post(TimeEvent(time, "$timeType"))
            }
        }, 4, isVisiTab = true, isSEndTime = false)*/
        datePicker = CustomDatePicker(context, object : CustomDatePicker.CallbackSEListen {
            override fun onTimeSelected(
                startTime: Long, endTime: Long, timeType: Int
            ) {
                Log.e(TAG, "timeType: $timeType");
                Log.e(TAG, "startTime: $startTime");
                Log.e(TAG, "endTime: $endTime");
                when (timeType) {
                    //按照 年月日 进行筛选
                    1, 2, 3 -> {
                        if (startTime == 0L) {
                            return
                        }
                        this@TopCustPerfRanFragment.timestamp = startTime
                        type = "$timeType"

                        val time = DateFormatUtils.long2Str(timestamp, false, timeType)
                        this@TopCustPerfRanFragment.time = time
                        tv_poin_date?.text = time.replace("-", "/")

                    }
                    else -> {

                        if (startTime == 0L || endTime == 0L) {
                            return
                        }
                        this@TopCustPerfRanFragment.timestamp = startTime
                        type = "4"
                        beginDate = DateFormatUtils.long2Str(startTime, false, timeType)
                        endDate = DateFormatUtils.long2Str(endTime, false, timeType)

                        tv_poin_date?.text =
                            "${beginDate.replace("-", "/")}\n${endDate.replace("-", "/")}"
                    }
                }

                EventBus.getDefault().post(TimeEvent(time, "$type",beginDate,endDate))

            }

        }, 4, true, true)
    }

    var beginDate = ""
    var endDate = ""

    //排名类型
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun memberRanEvent(event: MemberRanEvent) {
        tv_poin_date.text = time.replace("-", "/")
        tv_ver_01.text = event.name
        tv_ver_02.text = "全部项目"
        tv_ver_03.text = "全部团队"
        tv_ver_04.text = "成交额(万元)"
        setIntentFragment(
            R.id.fl_pion_perfor_rank, when (event.name) {
                "项目" -> {
                    rl_ver_02.visibility = GONE
                    rl_ver_03.visibility = GONE
                    STProjectRaNFragment.newInstance()
                }
                "团队" -> {
                    rl_ver_02.visibility = VISIBLE
                    rl_ver_03.visibility = GONE
                    STTeaMRankFragment.newInstance()
                }
                //拓客员
                else -> {
                    rl_ver_02.visibility = VISIBLE
                    rl_ver_03.visibility = VISIBLE
                    STExtenRanKFragment.newInstance()
                }
            }, Bundle()
        )

    }

    //成交额
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun VISIEvent(event: VISIEvent) {
        LogUtils.d(TAG, "evnt:${event.type} ");
        tv_perfor_rank_viewall.visibility = if (event.VISI) VISIBLE else GONE
    }

    //成交额
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun stProjectRanEvent(event: STProjectRanEvent) {
        tv_ver_04.text = event.name
    }


    private var projectId = ""
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: Data) {
        when (event.stuta) {
            //全部团队
            0 -> {
                tv_ver_03.text = event.projectName
            }
            //我的项目
            2 -> {
                projectId = event.projectId
                tv_ver_02!!.text = event.projectName
                tv_ver_03.text = "全部团队"
                mPresenter.loadRepositories()
            }
            else -> {
            }
        }

    }

    override fun getUrl(url_type: Int): String {
        return PerformStateImp.API_PER_CUR_GROUP_LIST
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        if (!TextUtils.isEmpty(projectId)) {
            map["projectId"] = projectId
        }
        return map
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    private fun setIntentFragment(srcId: Int, fragment: Fragment, bundle: Bundle) {
        val beginTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        beginTransaction.replace(srcId, fragment)
        beginTransaction.commitAllowingStateLoss()
    }


}// Required empty public constructor