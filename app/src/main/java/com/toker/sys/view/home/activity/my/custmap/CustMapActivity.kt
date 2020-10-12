package com.toker.sys.view.home.activity.my.custmap

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ZoomControls
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.toker.sys.R
import com.toker.sys.dialog.my.CustMapDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.view.home.fragment.my.event.MyEvent
import kotlinx.android.synthetic.main.activity_cust_map.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import com.baidu.mapapi.map.Overlay
import com.baidu.mapapi.map.CircleOptions
import com.baidu.mapapi.map.TextOptions
import com.baidu.mapapi.map.OverlayOptions
import com.toker.sys.utils.tools.BitmapUtil


/**
 * 客户地图
 * @author yyx
 */

class CustMapActivity : BaseActivity<CustMapContract.View, CustMapPresenter>(), CustMapContract.View{

    override var mPresenter: CustMapPresenter = CustMapPresenter()
    var baiduMap: BaiduMap? = null

    override fun layoutResID(): Int  = R.layout.activity_cust_map

    override fun initView() {
        baiduMap = mv_map_cust.map
        tv_title.text = "客户地图"
        ll_account.visibility = VISIBLE
    }

    override fun initData() {
        setOnClickListener(img_back)
        setOnClickListener(ll_account)
        setOnClickListener(btn_cust_map_04)

        val child = mv_map_cust.getChildAt(1)
        if (child != null && (child is ImageView || child is ZoomControls)) {
            child.visibility = View.INVISIBLE
        }

        val mapOptions = BaiduMapOptions()
        baiduMap!!.uiSettings
        mv_map_cust.showZoomControls(false)


        val map = mv_map_cust.map

        //定义Maker坐标点
        val point = LatLng(23.20,113.30)
        //构建Marker图标
//        val bitmap = BitmapDescriptorFactory
//            .fromResource(R.mipmap.icon_locat)
        //构建MarkerOption，用于在地图上添加Marker


        val view =  LayoutInflater.from(this).inflate(R.layout.item_yuan_layout,null)
        val bitmap =  BitmapDescriptorFactory.fromView(view)
        val option = MarkerOptions()
            .position(point)
            .icon(bitmap)
        map.addOverlay(option)

        val mMapStatus = MapStatus.Builder()
            .target(point)
            .zoom(15.toFloat())
            .build()
        val newMapStatus = MapStatusUpdateFactory.newMapStatus(mMapStatus)

        map.animateMapStatus(newMapStatus)


    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back ->finish()
            ll_account -> EventBus.getDefault().post(MyEvent(9))
            //查询
            btn_cust_map_04->{
                CustMapDialog(this,"天河区")
            }
            else -> {
            }
        }
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
    override fun onPause() {
        super.onPause()
        mv_map_cust.onPause()
    }

    override fun onResume() {
        super.onResume()
        mv_map_cust.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mv_map_cust.onDestroy()

    }

}
