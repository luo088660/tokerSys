package com.toker.sys.view.home.fragment.my.item.myatten.meatten

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.utils.DistanceUtil
import com.lzy.okgo.utils.HttpUtils.runOnUiThread
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.dialog.my.CallPhoneDialog
import com.toker.sys.dialog.my.CallPreDialog
import com.toker.sys.dialog.my.MeAttenRemarksDialog
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.service.LoaService
import com.toker.sys.utils.network.params.AttendSchedulImp
import com.toker.sys.utils.tools.DataUtil
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.tools.Tool
import com.toker.sys.view.home.activity.custom.event.CustomerInvalid
import com.toker.sys.view.home.fragment.my.event.MyEvent
import com.toker.sys.view.home.fragment.my.item.myatten.bean.MeAttenBean
import kotlinx.android.synthetic.main.fragment_me_atten.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log2
import kotlin.math.roundToInt


/**
 * 我的考勤
 * @author yyx
 */

class MeAttenFragment : BaseFragment<MeAttenContract.View, MeAttenPresenter>(),
    MeAttenContract.View {
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val sdf1 = SimpleDateFormat("HH:mm:ss")
    private val sdf2 = SimpleDateFormat("HH:mm")
    val formatType = "yyyy-MM-dd HH:mm:ss"

    companion object {
        @JvmStatic
        fun newInstance(): MeAttenFragment {
            val args = Bundle()
            val fragment = MeAttenFragment()
            fragment.arguments = args
            return fragment
        }
    }

    //1. 打卡范围 efficientRange
    //2. 定位频率 locationNum（分钟）
    //
    //何家俊 11-28 21:37:50
    //allowDeviateDistance，locusLocationNum这两个字段删掉

    var overDistance = 0
    override var mPresenter: MeAttenPresenter = MeAttenPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_me_atten, container!!)
        }
        return main_layout!!
    }


    override fun initView() {
        EventBus.getDefault().register(this)
        mPresenter.loadRepositories()
        Log.e(TAG, "LoaService.address:${LoaService.address} ");
    }


    override fun initData() {
        timeStar()
        setOnClickListener(btn_me_atten_01)
        setOnClickListener(btn_me_atten_02)
        setOnClickListener(ll_me_atten_01_)
        setOnClickListener(ll_me_atten_02)

        tv_me_atten_02.text = DataUtil.StringData()
//        tv_me_atten_03.text = LoaService.address
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            //签到
            btn_me_atten_01 -> {

                if (TextUtils.isEmpty(AppApplication.ADDRESS)||
                    TextUtils.isEmpty(AppApplication.LATITUDE)||
                    TextUtils.isEmpty(AppApplication.LONGITUDE)){
                    toast("未定位到位置，请稍后重试")
                    return
                }
                //更新位置距离
                getOverDistance()
                //选择地址
                val b = lacksPermissions(activity!!)
                val bb = isLocServiceEnable(activity!!)
                LogUtils.d(TAG, "b:$b ");
                LogUtils.d(TAG, "bb:$bb ");
                if (b || !bb) {

                    CallPreDialog(context!!, 1, "")
                    if (b) {
                        toast("请打开定位权限")
                        requestPermission()
                        return
                    }
                    return
                }

                val toLong = DataUtil.stringToLong(standardCheckOutTime, formatType)
                val timeMillis = System.currentTimeMillis()

                if (toLong < timeMillis) {
                    //迟到
                    mPresenter.checkIn()
                } else {
                    if (overDistance > 0) {
                        CallPreDialog(context!!, 3, getDis(overDistance))
                    } else {
                        mPresenter.checkIn()
                    }
                }
            }
            //签退
            btn_me_atten_02 -> {

                if (TextUtils.isEmpty(AppApplication.ADDRESS)||
                    TextUtils.isEmpty(AppApplication.LATITUDE)||
                    TextUtils.isEmpty(AppApplication.LONGITUDE)){
                    toast("未定位到位置，请稍后重试")
                    return
                }
                //更新位置距离
                getOverDistance()
                val toLong = DataUtil.stringToLong(standardCheckOutTime, formatType)
                val timeMillis = System.currentTimeMillis()
                LogUtils.d(TAG, "tol: $toLong");
                LogUtils.d(TAG, "timeMillis: $timeMillis");
                val b = lacksPermissions(activity!!)
                LogUtils.d(TAG, "b:$b ");
                //Gps
                val bb = isLocServiceEnable(activity!!)
                LogUtils.d(TAG, "b:$b ");
                LogUtils.d(TAG, "bb:$bb ");
                if (b || !bb) {
                    CallPreDialog(context!!, 2, "")
                    if (b) {
                        toast("请打开定位权限")
                        requestPermission()
                        return
                    }
                    return
                }
                if (toLong > timeMillis) {
                    //早退
                    CallPhoneDialog(context!!, 1, "")
                } else if (overDistance > 0) {
                    CallPhoneDialog(context!!, 2, getDis(overDistance))
                } else {
                    mPresenter.checkOut()
                }


            }
            //备注 签到
            ll_me_atten_01_ -> {
                MeAttenRemarksDialog(
                    context!!,
                    5,
                    "${tv_me_atten_05.text}",
                    "${tv_me_atten_04.text}",
                    "${btn_me_atten_01.text}",
                    "${tv_me_atten_06.text}"
                )
            }
            //备注 签退
            ll_me_atten_02 -> {
                MeAttenRemarksDialog(
                    context!!,
                    6,
                    "${tv_me_atten_09.text}",
                    "${tv_me_atten_08.text}",
                    "${btn_me_atten_02.text}",
                    "${tv_me_atten_10.text}"
                )
            }
            else -> {
            }
        }
    }

    fun getDis(dis: Int): String {

        if (dis > 1000) {
            val bb4 = dis.toFloat()//长度单位是1051米。。
            val cc = bb4 / 100 //得到10.51==
            val d = cc.roundToInt()//四舍五入是11
            val e = d / 10.toFloat()//把10 也强转为float型的，再让10除以它==
            return "${e}公里"
        } else return "${dis}米"


    }

    //TODO 考勤异常
    // 错误提示
    override fun onShowError(desc: String) {
//        toast(desc)
        ll_me_atten_03.visibility = GONE
        ll_me_atten_04.visibility = GONE
        tv_me_atten_wxkq.visibility = VISIBLE
    }

    //其余接口调用异常
    override fun onErrorData(desc: String) {
        toast(desc)
    }

    var latitude = ""
    var longitude = ""
    var id: String = ""
    var tableTag: String = ""
    var standardCheckInTime: String = ""
    //    var allowDeviateDistance: String = ""
    var efficientRange = 0
    var standardCheckOutTime: String = ""
    var rule: String = ""
    //获取今日考勤
    override fun showData(data: MeAttenBean.Data) {
        LoaService.address
        tv_me_atten_wxkq.visibility = GONE
        latitude = data.latitude
        longitude = data.longitude
        AppApplication.LOCATIONNUM = data.locationNum
//        latitude ="22.524639"
//        longitude ="113.94945"
        id = data.id
        tableTag = data.tableTag
//        allowDeviateDistance = data.allowDeviateDistance
        efficientRange = data.efficientRange
        AppApplication.USERID = data.userId
        standardCheckInTime = data.standardCheckInTime
        standardCheckOutTime = data.standardCheckOutTime
        rule = data.rule
        tv_me_atten_04.text =
            "签到\n${sdf2.format(Date(DataUtil.stringToLong(standardCheckInTime, formatType)))}"
        tv_me_atten_08.text =
            "签退\n${sdf2.format(Date(DataUtil.stringToLong(standardCheckOutTime, formatType)))}"
        ll_me_atten_01_.visibility = if (data.checkInStatus == "0") {
            tv_me_atten_05.setTextColor(activity!!.resources.getColor(R.color.btn_red))
            VISIBLE
        } else GONE
        ll_me_atten_02.visibility = if (data.checkOutStatus == "0") {
            tv_me_atten_05.setTextColor(activity!!.resources.getColor(R.color.btn_red))
            VISIBLE
        } else GONE


        btn_me_atten_01.setTextColor(activity!!.resources.getColor(if (data.checkInStatus == "0") R.color.btn_red else R.color.c_black_6))
        btn_me_atten_02.setTextColor(activity!!.resources.getColor(if (data.checkOutStatus == "0") R.color.btn_red else R.color.c_black_6))

        btn_me_atten_01.isClickable = data.checkInStatus.isNullOrEmpty()
        btn_me_atten_02.isClickable = data.checkOutStatus.isNullOrEmpty()
        tv_me_atten_05.text = data.checkInAddress
        tv_me_atten_05.visibility = if (data.checkInAddress != null) VISIBLE else GONE
        tv_me_atten_09.text = data.checkOutAddress
        tv_me_atten_09.visibility = if (data.checkOutAddress != null) VISIBLE else GONE
        tv_me_atten_06.text = data.checkInRemark
        tv_me_atten_10.text = data.checkOutRemark

        tv_me_atten_03.text = data.address
        if (data.checkInDeclare != null) {
            tv_me_atten_07.text = data.checkInDeclare
            tv_me_atten_07.setTextColor(activity!!.resources.getColor(R.color.c_txt_yiguoqi))
            ll_me_atten_01_.isClickable = false
        }
        if (data.checkOutDeclare != null) {
            tv_me_atten_11.text = data.checkOutDeclare
            tv_me_atten_11.setTextColor(activity!!.resources.getColor(R.color.c_txt_yiguoqi))
            ll_me_atten_02.isClickable = false
        }
        if (data.checkInTime != null) {
            btn_me_atten_01.text =
                "${sdf2.format(Date(DataUtil.stringToLong(data.checkInTime, formatType)))}"
        }
        if (data.checkOutTime != null) {
            btn_me_atten_02.text =
                "${sdf2.format(Date(DataUtil.stringToLong(data.checkOutTime, formatType)))}"
        }
        if (data.checkInTime != null) {
            if (data.checkOutTime == null) {
                LoaService.timeStar(data.id, false)
            } else {
                LoaService.timeStop()
            }
        }
        tv_me_atten_011.text =
            "考勤说明：偏离考勤地址${if (data.efficientRange == 0) "0" else data.efficientRange}米外，视为考勤偏离位置异常；" +
                    "异常考勤通过管理员审核后，异常考勤变更为正常考勤。"
        getOverDistance()
    }

    //TODO 更新当前定位数据
    private fun getOverDistance() {
        try {
            val lat1 = LatLng(latitude.toDouble(), longitude.toDouble())
            //关闭定位权限 经纬度为空值
            val lat2 = LatLng(LoaService.latitude.toDouble(), LoaService.longitude.toDouble())
            //计算当前位置与考勤位置的距离
            val distance = DistanceUtil.getDistance(lat1, lat2).toInt()
            //当前位置间的距离与 指定距离差
            overDistance = (distance - efficientRange)
            LogUtils.d(TAG, "overDistance:$overDistance ");
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
        when (url_type) {
            2 -> {
//                val thisTime = System.currentTimeMillis() + 10 * 60 * 1000

                val thisTime =
                    System.currentTimeMillis() + (AppApplication.LOCATIONNUM.toInt()) * 60 * 1000 - 1000
                Log.e(TAG, "thisTime:$thisTime ");
//                CacheUtil.getInstance().put(Constants.THATTIME,"$thisTime")
                Tool.commit(context!!, Constants.THATTIME, "$thisTime")
            }
            else -> {
            }
        }
        mPresenter.loadRepositories()
    }

    var content = ""

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun CustomerInvalid(event: CustomerInvalid) {

        when (event.type) {
            100 -> {
//                if (efficientRange != 0 &&event.isType == 1) {
//                    CallPhoneDialog(context!!, 2, efficientRange.toString())
//                } else {
//                    mPresenter.checkOut()
//                }
                mPresenter.checkOut()
            }
            99 -> {
                val toLong = DataUtil.stringToLong(standardCheckOutTime, formatType)
                val timeMillis = System.currentTimeMillis()
                LogUtils.d(TAG, "tol: $toLong");
                LogUtils.d(TAG, "timeMillis: $timeMillis");
                if (event.isType == 1 || event.isType == 3) {
                    mPresenter.checkIn()
                } else {
                    if (toLong > timeMillis) {
                        //早退
                        CallPhoneDialog(context!!, 1, "")
                    } else if (overDistance > 0) {
                        CallPhoneDialog(context!!, 2, "$overDistance")
                    } else {
                        mPresenter.checkOut()
                    }
                }

            }
            else -> {
                this.content = event.context
                mPresenter.updateCheck(event.type)
            }
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun MyEvent(eventBus: MyEvent) {
        mPresenter.uploadLocation()
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {

        val map = mutableMapOf<String, String>()


        when (url_type) {
            1 -> {
                map["tableTag"] =
                    SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))
            }
            2, 3 -> {
                val b = lacksPermissions(activity!!)
                val bb = isLocServiceEnable(activity!!)
                map[if (url_type == 2) "checkInTime" else "checkOutTime"] =
                    sdf.format(Date(System.currentTimeMillis()))//签到时间	是	String

                LogUtils.d(TAG, "b:$b ");
                if (!b && bb) {
                    //位置权限未开启 不上传地址信息
                    map[if (url_type == 2) "checkInAddress" else "checkOutAddress"] =AppApplication.ADDRESS
//                        LoaService.address//签到地址	是	String
                    map[if (url_type == 2) "checkInLongitude" else "checkOutLongitude"] =AppApplication.LONGITUDE
//                        LoaService.longitude
                    // "113.352779"//LoaService.longitude//签到地址经度	是	String
                    map[if (url_type == 2) "checkInLatitude" else "checkOutLatitude"] =AppApplication.LATITUDE
//                        LoaService.latitude
                }
                //"23.122559" // LoaService.latitude//签到地址纬度	是	String
                if (overDistance > 0) {
                    map["overDistance"] = overDistance.toString()//超出距离	是	String
                }

                if (url_type == 2) {
                    map["standardCheckInTime"] = standardCheckInTime
                } else {
                    map["standardCheckOutTime"] = standardCheckOutTime
                }
                map["id"] = id
                map["rule"] = rule
                map["tableTag"] = tableTag
            }
            4 -> {
                map["attendanceId"] = id
                map["locateAddress"] = LoaService.address  //当前地址
                map["locateLongitude"] = LoaService.longitude// 	当前地址经度
                map["locateLatitude"] = LoaService.latitude //当前地址纬度
            }
            5, 6 -> {
                map[if (url_type == 5) "checkInDeclare" else "checkOutDeclare"] = content
                map["id"] = id
                map["tableTag"] =
                    tableTag//SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))

            }
            else -> {

            }
        }
        return map
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> AttendSchedulImp.API_CUST_ATTEND_TO_DAY
            2 -> AttendSchedulImp.API_CUST_ATTEND_CHECKIN
            3 -> AttendSchedulImp.API_CUST_ATTEND_CHECKOUT
            4 -> AttendSchedulImp.API_CUST_ATTEND_UPLOADLOCATION
            5 -> AttendSchedulImp.API_UPDATE_CHECK_IN_DECLARE
            6 -> AttendSchedulImp.API_UPDATE_CHECK_OUT_DECLARE
            else -> {
                ""
            }
        }
    }

    var mTask2: TimerTask? = null
    var mTimer2: Timer? = null
    //定时器
    fun timeStar() {
        if (mTimer2 == null && mTask2 == null) {
            mTimer2 = Timer()
            mTask2 = object : TimerTask() {
                override fun run() {
                    runOnUiThread(Runnable {
                        if (tv_me_atten_01 != null) {
                            tv_me_atten_01.text = sdf1.format(Date(System.currentTimeMillis()))
                        }
                    })
                }
            }
            mTimer2!!.schedule(mTask2, 0, 1000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mTask2?.cancel()
        mTimer2?.cancel()
        mTask2 = null
        mTimer2 = null
        EventBus.getDefault().unregister(this)
    }

    /**
     * 判断是否缺少权限
     */
    private fun lacksPermission(mContexts: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            mContexts,
            permission
        ) === PackageManager.PERMISSION_DENIED
    }

    fun isLocServiceEnable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;

    }

    /**
     * 判断权限集合
     * permissions 权限数组
     * return true-表示没有改权限 false-表示权限已开启
     */
    fun lacksPermissions(mContexts: Context): Boolean {
        for (permission in permissionsREAD()) {
            if (lacksPermission(mContexts, permission)) {
                return true;
            }
        }
        return false;
    }

    fun permissionsREAD() = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    /**
     * 添加权限
     */
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            activity!!, arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 0
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {
            for (i in permissions.indices) {
                if (grantResults[i] != -1) {
                    //T.showShort(mContext,"权限设置成功");
                    initDatS()
                }
            }

        }
    }


    fun initDatS() {
        val intent = Intent(activity, LoaService::class.java)
        activity?.bindService(intent, connection, AppCompatActivity.BIND_AUTO_CREATE)
        LoaService.handler = handler

    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            //获取服务的代理对象
//            binder = service as LoaService.MyBinder
        }

        override fun onServiceDisconnected(name: ComponentName) {

        }
    }
    /**
     * 接受到消息后将城市名显示出来
     */
    private val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
//            if (binder != null) {
//                log("----数据${binder!!.city}", "MainActivity")
//            }
        }
    }
}