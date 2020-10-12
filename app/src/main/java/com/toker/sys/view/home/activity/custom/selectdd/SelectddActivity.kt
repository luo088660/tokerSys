package com.toker.sys.view.home.activity.custom.selectdd

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.service.LoaService
import kotlinx.android.synthetic.main.activity_selectdd.*
import kotlinx.android.synthetic.main.layout_content_title.*
import android.widget.ZoomControls
import android.widget.ImageView
import com.baidu.mapapi.map.InfoWindow
import android.widget.Button
import android.widget.TextView
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.geocode.*
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.custom.bean.RegisterBean
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception


/**
 * @author yyx
 */

class SelectddActivity : BaseActivity<SelectddContract.View, SelectddPresenter>(),
    SelectddContract.View, BaiduMap.OnMapClickListener, BaiduMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?): Boolean {
        showMarker(p0?.position, p0?.title)
        return false
    }

    private fun showMarker(position: LatLng?, title: String?) {
        baiduMap?.clear()
        val option = MarkerOptions()
            .icon(bitmap)
            .position(position)
        baiduMap?.addOverlay(option)
        EventBus.getDefault().post(position!!)
    }

    val bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.icon_signa)

    override fun onMapPoiClick(p0: MapPoi?): Boolean {
        showMarker(p0?.position, p0?.name)
        return false
    }

    var button: TextView? = null
    override fun onMapClick(p0: LatLng?) {
        showMarker(p0, "titli")
    }

    override var mPresenter: SelectddPresenter = SelectddPresenter()

    var baiduMap: BaiduMap? = null
    var bean: RegisterBean? = null
    override fun layoutResID(): Int = R.layout.activity_selectdd
    override fun initView() {
        EventBus.getDefault().register(this)
        tv_title.text = "选择地址"
        baiduMap = mv_map_selectadd.map

    }

    override fun onResume() {
        super.onResume()
        mv_map_selectadd.onResume()
    }

    override fun initData() {

        button = TextView(this)
        button?.setPadding(20, 5, 20, 5)
        button?.background = resources.getDrawable(R.drawable.pb_pd_sp_download)
        tv_myaccount.visibility = VISIBLE
        tv_myaccount.text = "确定"
        setOnClickListener(img_back)
        setOnClickListener(tv_myaccount)
        val uiSettings = baiduMap?.uiSettings!!
        uiSettings.isCompassEnabled = false
        uiSettings.isRotateGesturesEnabled = false
        mv_map_selectadd.showScaleControl(false)
        mv_map_selectadd.showZoomControls(false)
        uiSettings.isOverlookingGesturesEnabled = false


        try {
//            val point = LatLng(22.526521, 113.952310)
//            val point = LatLng(23.128051, 113.359250)
            val point = LatLng(LoaService.latitude.toDouble(), LoaService.longitude.toDouble())

            // 隐藏logo
            val child = mv_map_selectadd.getChildAt(1)
            if (child != null && (child is ImageView || child is ZoomControls)) {
                child!!.visibility = View.INVISIBLE
            }

            val mMapStatus = MapStatus.Builder()
                .target(point)
                .zoom(18.toFloat())
                .build()
            val option = MarkerOptions()
                .position(point)
                .icon(bitmap)

            baiduMap!!.addOverlay(option)
            latLug(point)
            val newMapStatus = MapStatusUpdateFactory.newMapStatus(mMapStatus)
            baiduMap?.setOnMapClickListener(this)
            baiduMap?.setOnMarkerClickListener(this)
            baiduMap?.animateMapStatus(newMapStatus)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            //确定选择
            tv_myaccount -> {
                if (bean == null) {
                    return
                }
                val intent = Intent()
                intent.putExtra("bean", bean)
                setResult(4, intent)
                finish()
            }
            else -> {
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mv_map_selectadd.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mv_map_selectadd.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun registerBean(event: RegisterBean) {
        bean = event
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun latLug(latLng: LatLng) {
        getAddress(latLng)
    }

    // 根据坐标获取地址
    fun getAddress(latLng: LatLng) {
        LogUtils.d(TAG, "latLng:$latLng ");
        val mGeoCoder = GeoCoder.newInstance();
        // 发起反地理编码（经纬度 -> 地址信息）

        mGeoCoder.setOnGetGeoCodeResultListener(object : OnGetGeoCoderResultListener {
            override fun onGetGeoCodeResult(geoCodeResult: GeoCodeResult) {
            }

            override fun onGetReverseGeoCodeResult(reverseGeoCodeResult: ReverseGeoCodeResult) {
                if (reverseGeoCodeResult != null && reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {
                    Log.d(TAG, "reverseGeoCodeResult:$reverseGeoCodeResult ");
                    // 获取反向地理编码结果
                    val poiInfo = reverseGeoCodeResult.poiList[0]
                    val poiRegionsInfoList = reverseGeoCodeResult.poiRegionsInfoList
                   /* var name = ""
                    if (poiRegionsInfoList != null){
                        name = "${poiRegionsInfoList[0].regionName}${poiRegionsInfoList[0].directionDesc},"
                    }
                    var address = "${reverseGeoCodeResult.address}${reverseGeoCodeResult.sematicDescription}".replace(name,"")

                    val indexOf = if (address.contains("附近")){
                        address.indexOf("附近")
                    }else address.indexOf("内")
                    if (indexOf>-1){
                        address = address.substring(0,indexOf)
                    }*/
//                    Log.d(TAG, "address:$address ");
                    var address = if (poiInfo!= null) {
                        "${reverseGeoCodeResult.address}${poiInfo.name}"
                    }else{
                        "${reverseGeoCodeResult.address}"
                    }

                    val bean = RegisterBean(
                       address,
//                        reverseGeoCodeResult.address,
                        reverseGeoCodeResult.addressDetail.district,
                        reverseGeoCodeResult.addressDetail.city,
                        reverseGeoCodeResult.addressDetail.province,
                        reverseGeoCodeResult.addressDetail.street,
                        latLng.latitude.toString(),
                        latLng.longitude.toString()

                    )
                    EventBus.getDefault().post(bean)
                    button?.text = if(address.length>20){
                        "${address.substring(0,20)}\n${address.substring(20,address.length)}"
                    } else address
//                    button?.text = poiInfo.name
                    //构造InfoWindow
                    //point 描述的位置点
                    //-100 InfoWindow相对于point在y轴的偏移量
                    val mInfoWindow = InfoWindow(button, latLng, -100)
                    //使InfoWindow生效
                    baiduMap?.showInfoWindow(mInfoWindow)

                }
            }
        })
        mGeoCoder.reverseGeoCode(ReverseGeoCodeOption().location(latLng))
    }
}
