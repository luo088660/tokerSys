package com.toker.sys.view.home.fragment.sheet.item.attendtoday

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.AppApplication
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.service.LoaService
import com.toker.sys.utils.network.params.AttendSchedulImp
import com.toker.sys.utils.tools.DataUtil
import com.toker.sys.view.home.fragment.my.item.myatten.bean.MeAttenBean
import kotlinx.android.synthetic.main.fragment_attend_today.*
import kotlinx.android.synthetic.main.fragment_me_atten.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 拓客员
 * 拓客组长
 * [今日考勤]
 * @author yyx
 */

class AttendTodayFragment : BaseFragment<AttendTodayContract.View, AttendTodayPresenter>(),
    AttendTodayContract.View {

    private val sdf2 = SimpleDateFormat("HH:mm")
    val formatType = "yyyy-MM-dd HH:mm:ss"

    companion object {
        @JvmStatic
        fun newInstance(): AttendTodayFragment {
            val args = Bundle()
            val fragment = AttendTodayFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override var mPresenter: AttendTodayPresenter = AttendTodayPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_attend_today, container!!)
        }
        return main_layout!!
    }

    override fun initView() {


    }

    override fun onStart() {
        super.onStart()
        mPresenter.loadRepositories()
    }

    override fun initData() {
        tv_today_01.text = DataUtil.StringData()

    }

    override fun showData(data: MeAttenBean.Data) {
        ll_attend_today.visibility = VISIBLE
        tv_attend_today.visibility = GONE

        AppApplication.LOCATIONNUM = data.locationNum

        tv_today_02.text =
            "${sdf2.format(Date(DataUtil.stringToLong(data.standardCheckInTime, formatType)))}"
        tv_today_06.text =
            "${sdf2.format(Date(DataUtil.stringToLong(data.standardCheckOutTime, formatType)))}"

        tv_today_02.setTextColor(context?.resources!!.getColor(if (data.checkInStatus.isNullOrEmpty()||data.checkInStatus=="1")R.color.c_black_6 else R.color.btn_red))
        tv_today_06.setTextColor(context?.resources!!.getColor(if (data.checkOutStatus.isNullOrEmpty()||data.checkOutStatus=="1")R.color.c_black_6 else R.color.btn_red))

        tv_today_04.text = "${if (!data.checkInRemark.isNullOrEmpty()) data.checkInRemark else ""}"
        tv_today_05.text = data.checkInAddress
        tv_today_044.text ="${if (!data.checkInDeclare.isNullOrEmpty()) data.checkInDeclare else ""}"
        tv_today_08.text =  "${if (!data.checkOutRemark.isNullOrEmpty()) data.checkOutRemark else ""}"
        tv_today_088.text = "${if (!data.checkOutDeclare.isNullOrEmpty()) data.checkOutDeclare else ""}"
        tv_today_09.text = data.checkOutAddress

        if (!data.checkInTime.isNullOrEmpty()) {
            tv_today_02.text =
                "${sdf2.format(Date(DataUtil.stringToLong(data.checkInTime, formatType)))}"
        }
        if (!data.checkOutTime.isNullOrEmpty()) {
            tv_today_06.text =
                "${sdf2.format(Date(DataUtil.stringToLong(data.checkOutTime, formatType)))}"
        }
        if (data.checkInAddress.isNullOrEmpty()) {
            tv_today_05.visibility = GONE
        }
        if (data.checkOutAddress.isNullOrEmpty()) {
            tv_today_09.visibility = GONE
        }
        if (data.checkInTime != null){
            if (data.checkOutTime == null) {
                LoaService.timeStar(data.id,true)
            }else{
                LoaService.timeStop()
            }
        }
    }

    override fun onShowError(desc: String) {
        ll_attend_today.visibility = GONE
        tv_attend_today.visibility = VISIBLE
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String,String>()
        map["tableTag"] = SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))
        return map
    }
    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> AttendSchedulImp.API_CUST_ATTEND_TO_DAY
            else -> {
                ""
            }
        }
    }


}// Required empty public constructor