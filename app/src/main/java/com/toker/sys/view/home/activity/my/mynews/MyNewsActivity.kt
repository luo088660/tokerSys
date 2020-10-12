package com.toker.sys.view.home.activity.my.mynews

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.common.request.MonthIndicImp
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.my.myatten.MyAttenActivity
import com.toker.sys.view.home.activity.sheet.waitcust.WaitCustActivity
import com.toker.sys.view.home.activity.sheet.waitforcust.WaitForCustActivity
import com.toker.sys.view.home.activity.task.adminitrandetail.AdminiTranDetailActivity
import com.toker.sys.view.home.activity.task.monthindicass.MonthIndicASActivity
import com.toker.sys.view.home.activity.task.transacttask.TransactTaskActivity
import com.toker.sys.view.home.fragment.event.ReadCountEvent
import com.toker.sys.view.home.fragment.event.TaskEvent
import com.toker.sys.view.home.fragment.my.item.bean.UnreadNewsBean
import com.toker.sys.view.home.fragment.my.item.mynews.haveread.HaveReadFragment
import com.toker.sys.view.home.fragment.my.item.mynews.unreadnews.UnreadNewsFragment
import com.toker.sys.view.home.fragment.task.adapter.TaskAdapter
import kotlinx.android.synthetic.main.activity_my_news.*
import kotlinx.android.synthetic.main.layout_content_title.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.regex.Pattern

/**
 * 我的消息
 * @author yyx
 */

class MyNewsActivity : BaseActivity<MyNewsContract.View, MyNewsPresenter>(), MyNewsContract.View{

    override var mPresenter: MyNewsPresenter = MyNewsPresenter()

    private var mBeans = arrayOf("")

    var taskAdapter : TaskAdapter?= null
    override fun layoutResID(): Int  = R.layout.activity_my_news
    override fun initView() {
        tv_title.text = "我的消息"

    }

    override fun initData() {
        EventBus.getDefault().register(this)
        mBeans = resources.getStringArray(R.array.my_news)
        rv_my_news.layoutManager = GridLayoutManager(this, 2)
        taskAdapter = TaskAdapter(this, mBeans)
        rv_my_news.adapter = taskAdapter
        setOnClickListener(img_back)
        setIntentFragment(UnreadNewsFragment.newInstance(), Bundle())
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN_ORDERED)
    fun TaskEvent(event: TaskEvent) {
        setIntentFragment(
            when (event.name) {
                //未读 UnreadNewsFragment
                mBeans[0] -> UnreadNewsFragment.newInstance()
                //已读 HaveReadFragment
//                mBeans[1]->HaveReadFragment.newInstance()
                else -> HaveReadFragment.newInstance()
            }, Bundle()
        )
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun readCountEvent(event: ReadCountEvent) {
        taskAdapter?.upDataView(0,event.numBer)
    }
    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            else -> {
            }
        }
    }


    //我的消息列表详情
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun unreadNewsBean(event: UnreadNewsBean.Records){
        LogUtils.d(TAG, "event:$event ")
        if (event.type == 0||event.type == 2) {
            return
        }
        when (event.msgType.toInt()) {
            //0待跟进客户提醒
            0 -> {
                when (AppApplication.TYPE) {
                    Constants.RESCUE1,
                    Constants.RESCUE2-> {
                        startActivity(Intent(this, WaitForCustActivity::class.java))
                    }
                    else -> {
                        startActivity(Intent(this, WaitCustActivity::class.java))
                    }
                }
            }
            //1任务新增通知
            1,
            //2任务变更通知
            2,
            //3任务审核通知
            3 -> {
//                var (id, tableTag) = splitData(event)
//                val bean = MonthIndicImp(event.businessId,"2019-11-01")
                val bean = MonthIndicImp(event.businessId,event.businessTag)
                LogUtils.d(TAG, "substr: ${bean.id}---${bean.tableTag}");
//                val intent = Intent(this, MonthIndicAssActivity::class.java)
                val intent = Intent(this, MonthIndicASActivity::class.java)
                intent.putExtra(Constants.BEANDATA,bean)
                startActivity(intent)
            }
            //4任务轨迹异常通知
            4 -> {
//                var (id, tableTag) = splitData(event)
                val intent = Intent(this, AdminiTranDetailActivity::class.java)
                intent.putExtra("type", 5)
                intent.putExtra("id", event.businessId)
                intent.putExtra("tableTag", event.businessTag)
                startActivity(intent)
            }
            //5考勤签到提醒
            5,
            //6考勤签退提醒
            6 -> {
                startActivity(Intent(this, MyAttenActivity::class.java))
            }
            7,
            8->{
                if (event.msgType.toInt() == 7 && AppApplication.TYPE == Constants.RESCUE2) {
                    return
                }
                //事务任务
                val intent = Intent(this, TransactTaskActivity::class.java)
                intent.putExtra("type", 2)
                intent.putExtra("taskId", event.businessId)
                intent.putExtra("userId", "")
                intent.putExtra("tableTag", event.businessTag)
                intent.putExtra("updateTime", event.sendTime)
                startActivity(intent)
            }
            //7工作轨迹异常通知
            else -> {

            }
        }
    }

    /**
     * 数据拆分
     */

    private fun splitData(event: UnreadNewsBean.Records): Pair<String, String> {
        var id = ""
        var tableTag = ""
        var attribute = event.addAttribute
        if (!TextUtils.isEmpty(attribute)) {
            attribute = attribute.replace(" ", "")
            attribute = attribute.replace("\"", "")
            var i1 = attribute.indexOf(",")
            val i = appearNumber(attribute, ",")
            for (i2 in 0 until i) {
                val substring = attribute.substring(0, i1)
                if (i2 == 0) {
                    id = substring
                }
                if (i2 == 1) {
                    tableTag = substring
                }
                LogUtils.d(TAG, "substr: $substring");
                attribute = attribute.substring(i1 + 1, attribute.length)
                i1 = attribute.indexOf(",")
            }
        }
        return Pair(id, tableTag)
    }


    /**
     * 获取指定字符串出现的次数
     *
     * @param srcText  源字符串
     * @param findText 要查找的字符串
     * @return
     */
    private fun appearNumber(srcText: String, findText: String): Int {
        var count = 0
        val p = Pattern.compile(findText)
        val m = p.matcher(srcText)
        while (m.find()) {
            count++
        }
        return count
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }
    private fun setIntentFragment(fragment: Fragment, bundle: Bundle) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        fragment.arguments = bundle
        beginTransaction.replace(R.id.fl_my_news, fragment)
        beginTransaction.commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)

    }
}
