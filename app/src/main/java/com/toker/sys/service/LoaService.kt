package com.toker.sys.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.baidu.location.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*
import com.baidu.mapapi.utils.CoordinateConverter
import com.lzy.okgo.utils.HttpUtils.runOnUiThread
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.tools.Tool
import com.toker.sys.view.home.activity.main.MainActivity
import com.toker.sys.view.home.activity.main.event.MainEvent
import org.greenrobot.eventbus.EventBus
import java.util.*


/**
 * 后台定位
 */
@Suppress("DEPRECATION")
class LoaService : Service() {
    private val TAG = "LoaService"
    //查询到的城市信息

    //设置为静态成员方便在 Activity 中引用

    companion object {
        var handler: Handler? = null
        //省份
        var province: String = ""
        //城市
        var city: String = ""
        //区
        var district: String = ""
        //街道
        var street: String = ""
        //详细地址
        var address: String = ""
        //经度
        var latitude: String = ""
        //纬度
        var longitude: String = ""
        var mTask2: TimerTask? = null
        var mTimer2: Timer? = null
        //定时器
        fun timeStar(id: String, isfo: Boolean) {
            Log.e("LoaService", "timeStar: $isfo");
            if (mTimer2 == null && mTask2 == null) {
                mTimer2 = Timer()
                mTask2 = object : TimerTask() {
                    override fun run() {
                        runOnUiThread(Runnable {
                            Log.e("LoaService", "timeStop: ");
                            val value =
                                Tool.getValue(AppApplication.getInstance(), Constants.THATTIME)
//                            val value = CacheUtil.getInstance().get(Constants.THATTIME)
                            Log.e("LoaService", "value:$value ");
                            if (!TextUtils.isEmpty(value)) {
                                //时间存在
                                val time = value!!.toLong()
                                val millis = System.currentTimeMillis()
                                Log.e("LoaService", "time  :$time ");
                                Log.e("LoaService", "millis:$millis ");
                                if (time<=millis){
                                    Log.e("LoaService", "uploadLocation: " );
                                    EventBus.getDefault().post(MainEvent(100, id))
                                }
                            } else {
                                Log.e("LoaService", "uploadLocation: " );
                                EventBus.getDefault().post(MainEvent(100, id))
                            }
                        })
                    }
                }
                mTimer2!!.schedule(mTask2, if (isfo) 0L else 1 * 60 * 1000, (1 * 60 * 1000))
//                mTimer2!!.schedule(mTask2, if (isfo)0L else 10*60*1000, (10*60*1000))
            }
        }

        fun timeStop() {
            Log.e("LoaService", "timeStop: ");
            mTask2?.cancel()
            mTimer2?.cancel()
        }
    }

    //Activity 与 Service 交互的通道
    private val binder = MyBinder()

    //定位客户端
    private var client: LocationClient? = null

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        LogUtils.d(TAG, "onReceiveLocation: onCreate")
        getLacation()
    }

    private fun getLacation() {

        client = LocationClient(applicationContext)
        //注册监听接口


        val option = LocationClientOption()

        option.setCoorType("bd0911");
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        option.setPriority(LocationClientOption.NetWorkFirst)





        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
//        option.setCoorType("gcj02")
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的

        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true)
        //可选，设置是否需要地址描述
        option.setIsNeedLocationDescribe(true)
        //可选，设置是否需要设备方向结果
        option.setNeedDeviceDirect(false)
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.isLocationNotify = true
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(true)
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true)
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true)
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false)
        //可选，默认false，设置是否开启Gps定位
        option.isOpenGps = true
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        option.setIsNeedAltitude(false)
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        option.setOpenAutoNotifyMode()
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        option.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT)

        option.setIsNeedLocationDescribe(true);
        val locationListener = MyLocationListener()
        option.setScanSpan(1000*1)
        client!!.registerLocationListener(locationListener)
        client!!.locOption = option
        //开始定位
        client!!.start()

//        initNotification()

    }

    private fun initNotification() {

        //开启前台定位服务：

        //开启前台定位服务：
        val builder: Notification.Builder = Notification.Builder(this)
        //获取一个Notification构造器

        //获取一个Notification构造器
        val nfIntent =
            Intent(this, MainActivity::class.java)
        builder.setContentIntent(
            PendingIntent.getActivity(
                this,
                0,
                nfIntent,
                0
            )
        ) // 设置PendingIntent
            .setContentTitle("正在进行后台定位") // 设置下拉列表里的标题
            .setSmallIcon(R.mipmap.icon_retina_hd_spotlight_) // 设置状态栏内的小图标
            .setContentText("后台定位通知") // 设置上下文内容
            .setAutoCancel(true)
            .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间

        var notification: Notification? = null
        notification = builder.build()
        notification.defaults = Notification.DEFAULT_SOUND //设置为默认的声音

        client!!.enableLocInForeground(1001, notification) // 调起前台定位

    }

    /**
     * 在代理中返回真正地城市名信息
     */
    inner class MyBinder : Binder() {
        val city: String
            get() = city
    }

    inner class MyLocationListener : BDLocationListener {
        override fun onReceiveLocation(bdLocation: BDLocation?) {
            if (bdLocation == null) {
                Toast.makeText(this@LoaService, "定位失败", Toast.LENGTH_SHORT).show()
                return
            }
            if (bdLocation.locType == BDLocation.TypeNetWorkLocation || bdLocation.locType == BDLocation.TypeGpsLocation) {
                //也可以使用 bdLocation.getCity()
                if (bdLocation.district != null) {
//                    Log.d(TAG, "onReceiveLocation: " + bdLocation.district)
                    if (handler != null) {
                        //向实例化 Handler 的线程发送消息
                        province = bdLocation.province
                        city = bdLocation.city
                        district = bdLocation.district
                        street = bdLocation.street
                        //详细地址
//                        address = bdLocation.addrStr



                        val converter = CoordinateConverter();
                        converter.from(CoordinateConverter.CoordType.COMMON);
                        // LatLng待转换坐标
                        val latLng = LatLng(bdLocation.latitude, bdLocation.longitude)

                        converter.coord(latLng);
                        val desLatLng = converter.convert();
                        latitude = String.format("%.6f", desLatLng.latitude)
                        longitude = String.format("%.6f", desLatLng.longitude)
                        getAddress(LatLng(latitude.toDouble(), longitude.toDouble()))
                         Log.e(
                              TAG, "\nbdLocation.latitude:" + latitude +
                                      "\nbdLocation.longitude:" + longitude +
                                      "\nbdLocation.address:" + address +
                                      "\nbdLocation.address:" + bdLocation.addrStr +
                                      "\nbdLocation.street:" + bdLocation.radius +
                                      "\nbdLocation.addrStr:${bdLocation.addrStr}"
                          )
                        AppApplication.ADDRESS = bdLocation.addrStr
                        AppApplication.LATITUDE = latitude
                        AppApplication.LONGITUDE = longitude
                    }
                }
            }
        }

    }


    // 根据坐标获取地址
    fun getAddress(latLng: LatLng) {
//        LogUtils.d(TAG, "latLng:$latLng ");
        val mGeoCoder = GeoCoder.newInstance();
        // 发起反地理编码（经纬度 -> 地址信息）

        mGeoCoder.setOnGetGeoCodeResultListener(object : OnGetGeoCoderResultListener {
            override fun onGetGeoCodeResult(geoCodeResult: GeoCodeResult) {
            }

            override fun onGetReverseGeoCodeResult(reverseGeoCodeResult: ReverseGeoCodeResult) {
                if (reverseGeoCodeResult != null && reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    // 获取反向地理编码结果
                    val poiRegionsInfoList = reverseGeoCodeResult.poiRegionsInfoList
//                    Log.e(TAG, "poiRegionsInfoList:$reverseGeoCodeResult ");
                    /*   var name = ""
                       if (poiRegionsInfoList != null){
                           name = "${poiRegionsInfoList[0].regionName}${poiRegionsInfoList[0].directionDesc},"
                       }
                       address = reverseGeoCodeResult.address+reverseGeoCodeResult.sematicDescription.replace(name,"")

                       val indexOf = if (address.contains("附近")){
                           address.indexOf("附近")
                       }else address.indexOf("内")
                       if (indexOf>-1){
                           address = address.substring(0,indexOf)
                       }*/
                    val poiInfo = reverseGeoCodeResult.poiList[0]
                    address = if (poiInfo != null) {
                        "${reverseGeoCodeResult.address}${poiInfo.name}"
                    } else {
                        "${reverseGeoCodeResult.address}"
                    }
                }
            }
        })
        mGeoCoder.reverseGeoCode(ReverseGeoCodeOption().location(latLng))
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy: ----");
        //服务销毁停止定位
        client!!.stop()
        mTask2?.cancel()
        mTimer2?.cancel()
        mTask2 = null
        mTimer2 = null
    }

}