package com.toker.sys.view.home.activity.main

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.api.TagAliasCallback
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.service.LoaService
import com.toker.sys.utils.network.params.AttendSchedulImp
import com.toker.sys.utils.tools.GlideCacheUtil
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.utils.tools.Tool
import com.toker.sys.view.home.activity.main.event.MainEvent
import com.toker.sys.view.home.activity.my.myatten.MyAttenActivity
import com.toker.sys.view.home.activity.my.mynews.MyNewsActivity
import com.toker.sys.view.home.activity.my.perinfor.PerInforActivity
import com.toker.sys.view.home.fragment.event.CustomEvent
import com.toker.sys.view.home.fragment.event.TaskEventT
import com.toker.sys.view.home.fragment.event.TaskHomeEvent
import com.toker.sys.view.home.fragment.my.event.MyEvent
import com.toker.sys.view.home.fragment.sheet.event.SheetEvent
import com.toker.sys.view.home.menu.MainTabs
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat


/**
 * @author yyx
 */

class MainActivity : BaseActivity<MainContract.View, MainPresenter>(), MainContract.View {

    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    override var mPresenter: MainPresenter = MainPresenter()
    private var binder: LoaService.MyBinder? = null

    override fun layoutResID(): Int = R.layout.activity_main

    override fun initView() {
        EventBus.getDefault().register(this)
        initFragmentTabHost()
        setAlias()
    }


    override fun initData() {
        val intent = Intent(this, LoaService::class.java)
        bindService(intent, connection, BIND_AUTO_CREATE)
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
            if (binder != null) {
                log("----数据${binder!!.city}", "MainActivity")
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    fun MainEvent(event: MainEvent) = when (event.type) {

        //切换到首页
        1 -> ft_main.currentTab = 0
        2 -> {
            log(event.name)
            toast(event.name)
        }
        //个人信息 PerInforActivity
        3 -> {
            val intent = Intent(this, PerInforActivity::class.java)
            intent.putExtra("name", event.name)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        //客户地图
        4 -> {
            ft_main.currentTab = 3
            EventBus.getDefault().postSticky(MyEvent(2))
        }
        //我的考勤
        5 -> {
            startActivity(Intent(this, MyAttenActivity::class.java))
        }
        //我的排班
        6 -> {
            ft_main.currentTab = 3
            EventBus.getDefault().postSticky(MyEvent(4))
        }
        //团队考勤
        7 -> {
            ft_main.currentTab = 3
            EventBus.getDefault().postSticky(MyEvent(7))
        }
        //我的任务
        8 -> ft_main.currentTab = 1
        //待审批任务
        9 -> {
            LogUtils.d(TAG, "eve:${event.type}");
            ft_main.currentTab = 1
            EventBus.getDefault().postSticky(TaskEventT(resources.getString(R.string.tip_process)))
        }
        //待跟进客户
        10 -> {
            ft_main.currentTab = 0
            EventBus.getDefault().postSticky(SheetEvent(7))
        }
        //待分配客户
        11 -> {
            ft_main.currentTab = 2
            EventBus.getDefault()
                .postSticky(CustomEvent(resources.getString(R.string.tip_bus_pool)))
        }
        //待跟进任务
        12 -> ft_main.currentTab = 1

        //项目详情
        13 -> {
            log("项目详情", "MainActivity")
            ft_main.currentTab = 3
            EventBus.getDefault().postSticky(MyEvent(8))
        }
        //团队排名
        14 -> {
            ft_main.currentTab = 0
            EventBus.getDefault().postSticky(SheetEvent(3))
        }
        //拓客员排名
        15 -> {
            ft_main.currentTab = 0
            EventBus.getDefault().postSticky(SheetEvent(4))
        }
        //未读消息
        16 -> {
            startActivity(Intent(this, MyNewsActivity::class.java))
        }
        //我的当前任务
        17 -> {
            ft_main.currentTab = 1
            EventBus.getDefault().postSticky(TaskHomeEvent(1))
        }
        100 -> {
            Log.e(TAG, "uploadLocation: ");
            id = event.name
            mPresenter.uploadLocation()
        }
        else -> {
            //99 退出登录
//            Tool.remove(this,Constants.USERNAME)
//            Tool.remove(this,Constants.PWD)
            GlideCacheUtil.getInstance().clearImageAllCache(this)
            finish()
        }
    }

    var id = ""
    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
//        val thisTime = System.currentTimeMillis() + 10 * 60 * 1000
        val thisTime =
            System.currentTimeMillis() + (AppApplication.LOCATIONNUM.toInt()) * 60 * 1000 - 1000
        Log.e(TAG, "onSuccessData --thisTime:$thisTime ");
//                CacheUtil.getInstance().put(Constants.THATTIME,"$thisTime")
        Log.e(TAG, "uploadLocation:$thisTime ");
        Tool.commit(this, Constants.THATTIME, "$thisTime")
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        unbindService(connection)
        Tool.remove(this, "token")
    }

    /**
     * fragment 展示
     */
    private fun initFragmentTabHost() {
        ft_main.setup(this, supportFragmentManager, R.id.fl_main)
        val values = MainTabs.values()
        for (mainTabs in values) {
            val tabSpec = ft_main.newTabSpec(mainTabs.names!!)
            val view = View.inflate(this, R.layout.tab_indicator, null)
            val tvIn = view.findViewById<TextView>(R.id.tv_indicator)
            val imgV = view.findViewById<ImageView>(R.id.img_)
            tvIn.text = mainTabs.names
            imgV.setImageResource(mainTabs.icon)
            tabSpec.setIndicator(view)
            ft_main.addTab(tabSpec, mainTabs.cla!!, null)
        }
    }

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private fun setAlias() {

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, AppApplication.PHONE))
    }

    private var mAliasCallback: TagAliasCallback = TagAliasCallback { code, alias, tags ->
        val logs: String
        when (code) {
            0 -> {
                logs = "Set tag and alias success"
                LogUtils.i(TAG, logs)
            }
            6002 -> {
                logs = "Failed to set alias and tags due to timeout. Try again after 60s."
                LogUtils.i(TAG, logs)
                // 延迟 60 秒来调用 Handler 设置别名
                mHandler.sendMessageDelayed(
                    mHandler.obtainMessage(MSG_SET_ALIAS, alias),
                    (1000 * 60).toLong()
                )
            }
            else -> {
                logs = "Failed with errorCode = $code"
                LogUtils.e(TAG, logs)
            }
        }// 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
//        ExampleUtil.showToast(logs, this)
    }
    private val MSG_SET_ALIAS = 1001
    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: android.os.Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_SET_ALIAS -> {
                    LogUtils.d(TAG, "Set alias in handler.")
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(
                        this@MainActivity,
                        msg.obj as String,
                        null,
                        this@MainActivity.mAliasCallback
                    )
                }
                else -> LogUtils.i(TAG, "Unhandled msg - " + msg.what)
            }
        }
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            4 -> {
                map["attendanceId"] = id
                map["locateAddress"] = AppApplication.ADDRESS  //当前地址
                map["locateLongitude"] = AppApplication.LONGITUDE// 	当前地址经度
                map["locateLatitude"] = AppApplication.LATITUDE //当前地址纬度

            }
        }
        return map
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            4 -> {
                Log.e(TAG, "AppApplication.ADDRESS:${AppApplication.ADDRESS} ");
                if (TextUtils.isEmpty(AppApplication.ADDRESS)) {
                    return ""
                }

                val thisTime = System.currentTimeMillis() + 10 * 60 * 1000
                Log.e(TAG, "thisTime:$thisTime ");
//                CacheUtil.getInstance().put(Constants.THATTIME,"$thisTime")
//                Tool.commit(this,Constants.THATTIME,"$thisTime")


                AttendSchedulImp.API_CUST_ATTEND_UPLOADLOCATION
            }
            else -> {
                ""
            }
        }
    }
}
