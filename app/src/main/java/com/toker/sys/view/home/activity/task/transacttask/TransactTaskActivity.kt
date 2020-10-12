package com.toker.sys.view.home.activity.task.transacttask

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.toker.sys.R
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.utils.tools.DataUtil
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.tools.TimeSwitchUtils
import com.toker.sys.view.home.activity.task.adapter.TransactListAdapter
import com.toker.sys.view.home.activity.task.adapter.TransactTaskAdapter
import com.toker.sys.view.home.activity.task.bean.TransactABean
import com.toker.sys.view.home.activity.task.bean.TransactBean
import com.toker.sys.view.home.activity.task.bean.TransactList
import com.toker.sys.view.home.activity.task.bean.TransactTaskBean
import com.toker.sys.view.home.activity.task.photoview.PhotoViewActivity
import com.toker.sys.view.home.fragment.event.IamgeBitmap
import kotlinx.android.synthetic.main.activity_transact_task.*
import kotlinx.android.synthetic.main.layout_admini_tran_detail_01.*
import kotlinx.android.synthetic.main.layout_admini_tran_detail_02.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


/**
 *
 * 事务任务-任务详情
 * 任务轨迹
 * @author yyx
 */

class TransactTaskActivity : BaseActivity<TransactTaskContract.View, TransactTaskPresenter>(),
    TransactTaskContract.View {

    override var mPresenter: TransactTaskPresenter = TransactTaskPresenter()
    private val sdf1 = SimpleDateFormat("HH:mm")
    private val sdf = SimpleDateFormat("yyyy-MM-dd")

    private val sdf2 = SimpleDateFormat("MM/dd")
    var type: Int = 0
    var taskId = ""
    var userId = ""
    var tableTag = ""
    var updateTime = ""
    var mBeans: MutableList<TransactABean.Data> = mutableListOf()
    val listBean: MutableList<TransactList.Data> = mutableListOf()
    var transactTaskAdapter: TransactTaskAdapter? = null
    var transactListAdapter: TransactListAdapter? = null
    var timeZone: TimeSwitchUtils? = null
    var baiduMap: BaiduMap? = null
    var endDate: Long = 0
    var startDate: Long = 0

    val formatType = "yyyy-MM-dd HH:mm:ss"
    val formatType1 = "yyyy-MM-dd"
    override fun layoutResID(): Int = R.layout.activity_transact_task

    override fun initView() {
        EventBus.getDefault().register(this)
        taskId = intent.getStringExtra("taskId")
        userId = intent.getStringExtra("userId")
        tableTag = intent.getStringExtra("tableTag")
        val Time = intent.getLongExtra("updateTime", 1L)
        LogUtils.d(TAG, "Time:$Time ");
        type = intent.getIntExtra("type", 0)

//        updateTime = sdf.format(System.currentTimeMillis())
        updateTime = sdf.format(Date(if (Time == 1L) System.currentTimeMillis() else Time))
        tv_transact_task.layoutManager = GridLayoutManager(this, 1)
        transactTaskAdapter = TransactTaskAdapter(this, mBeans)
        tv_transact_task.adapter = transactTaskAdapter
        timeZone = TimeSwitchUtils()

        tv_time.text = timeZone?.thatTime(updateTime)?.replace("-", "/")
        baiduMap = mv_map_task.map
        mPresenter.loadRepositories()
    }

    override fun initData() {
        setOnClickListener(img_back)
        setOnClickListener(img_time_left)
        setOnClickListener(img_time_right)
        when (type) {
            1 -> {
                ll_transact_task.visibility = GONE
                tv_transact_task_01.visibility = GONE
            }
            else -> {
                rv_list_transact_task.layoutManager = GridLayoutManager(this, 1)
                transactListAdapter = TransactListAdapter(this, listBean)
                rv_list_transact_task.adapter = transactListAdapter
                //获取自己任务汇报
                mPresenter.taskReportList()
            }
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> {
                finish()
            }
            //时间切换
            img_time_left -> {

                val toLong = DataUtil.stringToLong(updateTime, formatType1)
                LogUtils.d(TAG, "toLong:$toLong ");
                LogUtils.d(TAG, "startDate:$startDate ");
                if (startDate < toLong) {
                    updateTime = timeZone?.imgTimeLeft()!!
                    tv_time.text = updateTime.replace("-", "/")
                    mPresenter.getEventTaskTraceList()
                    mPresenter.taskReportList()
                }
            }
            img_time_right -> {
                val toLong = DataUtil.stringToLong(updateTime, formatType1)
                LogUtils.d(TAG, "toLong:$toLong ");
                LogUtils.d(TAG, "endDate:$endDate ");
                if (endDate > toLong) {
                    updateTime = timeZone?.imgTimeRight()!!
                    tv_time.text = updateTime.replace("-", "/")
                    mPresenter.getEventTaskTraceList()
                    mPresenter.taskReportList()
                }
            }
            else -> {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun bitmapDrawable(event: IamgeBitmap) {
        val intent = Intent(this, PhotoViewActivity::class.java)
        intent.putExtra("bmpPath", event.bmpPath)
        startActivity(intent)
    }

    //数据展示
    @SuppressLint("SetTextI18n")
    override fun showData(data: TransactTaskBean.Data) {

        endDate = data.endDate
        startDate = data.startDate
        tv_title.text = data.taskName
        tv_admini_tran_01.text = data.projectName
        tv_admini_tran_02.text =
            "${sdf.format(Date(data.startDate))}至${sdf.format(Date(data.endDate))}\t${data.startTime}~${data.endTime}"
        tv_admini_tran_03.text = data.address
        tv_admini_tran_04.text = data.content
        tv_admini_tran_05.text = data.objectList.joinToString { it.objectName }
        tv_admini_tran_06.text = when (data.status.toInt()) {
            1 -> {
                updateTime = sdf.format(Date(data.startDate))
                timeZone?.thatTime(updateTime)
                tv_time.text = updateTime.replace("-", "/")
                mPresenter.taskReportList()
                tv_admini_tran_06.setTextColor(resources.getColor(R.color.c_txt_weikaishi))
                tv_admini_tran_06.setBackgroundColor(resources.getColor(R.color.c_bg_weikaishi))
                "未开始"
            }
            2 -> {
                tv_admini_tran_06.setTextColor(resources.getColor(R.color.c_txt_yirenchou))
                tv_admini_tran_06.setBackgroundColor(resources.getColor(R.color.c_bg_yirenchou))
                "进行中"
            }
            3 -> {
                updateTime = sdf.format(Date(data.startDate))
                timeZone?.thatTime(updateTime)
                tv_time.text = updateTime.replace("-", "/")
                mPresenter.taskReportList()
                tv_admini_tran_06.setTextColor(resources.getColor(R.color.c_txt_tjchenggong))
                tv_admini_tran_06.setBackgroundColor(resources.getColor(R.color.c_bg_tjchenggong))
                "已完成"
            }
            else -> {
                "草稿中"
            }
        }
    }

    var mutableList: MutableList<TransactABean.Data> = mutableListOf()
    override fun showTBeanList(pageData: MutableList<TransactABean.Data>) {
        transactTaskAdapter!!.refreshData(pageData)
        mutableList.clear()
        mutableList.addAll(pageData)
        baiduMap?.clear()
        if (pageData != null) {

            var mVList: MutableList<TransactABean.Data> = mutableListOf()
            for (i in pageData) {
                if (i.locateLatitude != null && i.locateLongitude != null) {
                    mVList.add(i)
                }
            }
            if (mVList.size > 0) {
                val point = LatLng(
                    mVList[0].locateLatitude.toDouble(),
                    mVList[0].locateLongitude.toDouble()
                )
                var mOptions: MutableList<LatLng> = mutableListOf()
                //创建OverlayOptions的集合
                val options = ArrayList<OverlayOptions>()
                for (it in mVList) {
                    if (!TextUtils.isEmpty(it.locateLatitude) && !TextUtils.isEmpty(it.locateLongitude)) {
                        mOptions.add(
                            LatLng(
                                it.locateLatitude.toDouble(),
                                it.locateLongitude.toDouble()
                            )
                        )
//                        p0.img.background = mContext.resources.getDrawable(if (data.status.toInt()==1)R.mipmap.icon_orange else R.mipmap.icon_red)
                        val view = LayoutInflater.from(this).inflate(R.layout.map_iamge, null)
                        val img = view.findViewById<ImageView>(R.id.img_img)
                        img.setImageResource(if (it.status.toInt() == 1) R.mipmap.icon_orange else R.mipmap.icon_red)
                        val tv = view.findViewById<TextView>(R.id.tv_img)
                        tv.text = it.order.toString()
                        val resource =
                            BitmapDescriptorFactory.fromView(view)
                        val point1 =
                            LatLng(it.locateLatitude.toDouble(), it.locateLongitude.toDouble())
                        val option = MarkerOptions()
                            .position(point1)
                            .icon(resource)
                        options.add(option)
                    }
                }
                try {
                    //获取线
                    val points = PolylineOptions().width(10)
                        .color(R.color.map_color).points(mOptions)
                    baiduMap?.addOverlay(points)
                } catch (e: Exception) {
                    e.printStackTrace()
                }


                val mMapStatus = MapStatus.Builder()
                    .target(point)
                    .zoom(15.toFloat())
                    .build()
                val newMapStatus = MapStatusUpdateFactory.newMapStatus(mMapStatus)
                //绘制点
                //在地图上批量添加
                baiduMap?.addOverlays(options);

                baiduMap?.animateMapStatus(newMapStatus)
            }
        }
        val isInfo =  mutableList.size > 0
        tv_list_transact_task1.visibility = if (isInfo) GONE else VISIBLE
        rl_transact.visibility = if (isInfo) VISIBLE else GONE

        for (i in pageData) {
            if (i.status == "0") {
                tv_transact_task_02.visibility = VISIBLE
                tv_transact_task_02.text = "${sdf2.format(
                    Date(
                        DataUtil.stringToLong(
                            i.locateTime,
                            formatType
                        )
                    )
                )}\t${i.remark}"
                return
            }
        }


    }

    override fun showTBeanLists(data: MutableList<TransactList.Data>) {
        val isInfo = data != null && data.size > 0
        tv_list_transact_task.visibility = if (isInfo) GONE else VISIBLE

        transactListAdapter?.refreshData(if (isInfo) data else listBean)
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                map["taskId"] = taskId
                map["tableTag"] = tableTag
            }
            2 -> {
                if (type == 1) {
                    map["userId"] = userId
                }
                map["date"] = updateTime
                map["taskId"] = taskId
                map["tableTag"] = tableTag
            }
            3 -> {
                map["taskId"] = taskId
                map["tableTag"] = tableTag
                map["date"] = updateTime
            }
            else -> {

            }
        }
        return map
    }

    override fun getUrl(url_type: Int): String {

        return when (url_type) {
            1 -> TaskManageImp.API_TASK_EVENTTASK
            2 -> TaskManageImp.API_TASK_EVENT_TASK_TRACE_LIST
            3 -> TaskManageImp.API_TASK_MYEVENT_TASK_REPOR_TLIST
            else -> {
                ""
            }
        }


    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
