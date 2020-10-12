package com.toker.sys.view.home.fragment.task.item.missreport

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toker.sys.mvp.base.BaseFragment
import com.toker.sys.R
import com.toker.sys.utils.network.params.TaskManageImp
import com.toker.sys.view.home.activity.sheet.event.TimeEvent
import com.toker.sys.view.home.activity.task.photoview.PhotoViewActivity
import com.toker.sys.view.home.activity.task.transacttask.TransactTaskActivity
import com.toker.sys.view.home.fragment.event.IamgeBitmap
import com.toker.sys.view.home.fragment.task.adapter.MissReportAdapter
import com.toker.sys.view.home.fragment.task.adapter.MissReportTAdapter
import com.toker.sys.view.home.fragment.task.bean.MissReportBean
import kotlinx.android.synthetic.main.fragment_miss_report.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat

/**
 * 任务汇报
 * @author yyx
 */

class MissReportFragment : BaseFragment<MissReportContract.View, MissReportPresenter>(), MissReportContract.View {

    companion object {
        @JvmStatic
        fun newInstance(): MissReportFragment {
            val args = Bundle()
            val fragment = MissReportFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val sdf = SimpleDateFormat("yyyy-MM-dd")
    private var id: String = ""
    private var tableTag: String = ""
    private var date: String = sdf.format(System.currentTimeMillis())
    private var records = mutableListOf<MissReportBean.Record>()
    private var unRecords = mutableListOf<MissReportBean.UnRecord>()
    var missReportAdapter: MissReportAdapter? = null
    var reportTAdapter: MissReportTAdapter? = null
    override var mPresenter: MissReportPresenter = MissReportPresenter()

    override fun onNetworkLazyLoad() {
    }

    override fun createView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        if (main_layout == null) {
            main_layout = inflateView(R.layout.fragment_miss_report, container!!)
        }
        return main_layout!!
    }

    override fun initView() {
        EventBus.getDefault().register(this)

        id = arguments!!.getString("id")
        tableTag = arguments!!.getString("tableTag")
        date = arguments!!.getString("date")
        mPresenter.loadRepositories()

        rv_miss_report_01.layoutManager = GridLayoutManager(activity!!, 1)
        rv_miss_report_02.layoutManager = GridLayoutManager(activity!!, 1)


        reportTAdapter = MissReportTAdapter(context!!, unRecords)
        rv_miss_report_02.adapter = reportTAdapter
        missReportAdapter = MissReportAdapter(context!!, records)
        rv_miss_report_01.adapter = missReportAdapter
        rv_miss_report_01.addItemDecoration(getItemDecoration())
    }

    override fun initData() {

    }
    override fun showData(data: MissReportBean.Data) {
        tv_miss_report_00.text = "未填写(${data.unRecordList.size})人"
        tv_miss_report_01.text = "已填写(${data.recordList.size})人"
        reportTAdapter!!.refreshView(data.unRecordList)
        missReportAdapter!!.refreshView(data.recordList)


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun bitmapDrawable(event: IamgeBitmap){
        val intent = Intent(context, PhotoViewActivity::class.java)
        intent.putExtra("bmpPath",event.bmpPath)
        startActivity(intent)
    }


    /**
     * 时间
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun TimeEvent(event: TimeEvent){
        date = event.time
        mPresenter.loadRepositories()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun record(event :MissReportBean.Record){
        val intent = Intent(context, TransactTaskActivity::class.java)
        intent.putExtra("type",1)
        intent.putExtra("taskId",event.taskId)
        intent.putExtra("userId",event.userId)
        intent.putExtra("tableTag",event.tableTag)
        intent.putExtra("updateTime",event.updateTime)
        context!!.startActivity(intent)
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["taskId"] = id
        map["tableTag"] = tableTag
        map["date"] = date
        return map
    }

    override fun getUrl(url_type: Int): String {
        return TaskManageImp.API_TASK_EVENT_TASKR_EPORTLIST
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

}