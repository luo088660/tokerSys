package com.toker.sys.view.home.activity.sheet.waitforcust

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.dialog.ProjectListDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.view.home.activity.adapter.WaitForCustToAdapter
import com.toker.sys.view.home.activity.sheet.waitforcust.custodetail.CustoDetailActivity
import com.toker.sys.view.home.bean.ProjectToBean
import com.toker.sys.view.home.fragment.sheet.event.SheetEvent
import kotlinx.android.synthetic.main.activity_wait_for_cust.*
import kotlinx.android.synthetic.main.layout_content_title.*
import com.toker.sys.view.home.bean.*
import kotlinx.android.synthetic.main.item_layout_wait_for_t.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

/**
 *拓客中层、拓客管理员
 * 待跟进客户
 * @author yyx
 */

class WaitForCustActivity : BaseActivity<WaitForCustContract.View, WaitForCustPresenter>(),
    WaitForCustContract.View {

    override var mPresenter: WaitForCustPresenter = WaitForCustPresenter()
    var mBeans: MutableList<ProjectToBean.Record> = mutableListOf()
    var data: MutableList<Data>? = null
    override fun layoutResID(): Int = R.layout.activity_wait_for_cust
    var projectId = ""
    var recordTs: MutableList<ProjectToBean.Record> = mutableListOf()
    var custToAdapter: WaitForCustToAdapter? = null
    var pageSize = 1
    var isOrder1 = false
    var isOrder2 = false
    private var sortWay: String = "desc"
    private var sortBy: String = ""
    var drawableS: Drawable? = null
    var drawableX: Drawable? = null
    var drawableM: Drawable? = null
    override fun initView() {
        EventBus.getDefault().register(this)
        tv_title.text = "逾期未跟进客户"
        rv_wait_for.layoutManager = GridLayoutManager(this, 1)

        custToAdapter = WaitForCustToAdapter(this, recordTs)
        rv_wait_for.adapter = custToAdapter
    }

    override fun initData() {

        // 使用代码设置drawableleft
        drawableS = resources.getDrawable(R.mipmap.icon_xspx)
        drawableX = resources.getDrawable(R.mipmap.icon_xxpx)
        drawableM = resources.getDrawable(R.mipmap.icon_mrpx)
        // / 这一步必须要做,否则不会显示.
        drawableS!!.setBounds(0, 0, drawableS!!.minimumWidth, drawableS!!.minimumHeight)
        drawableX!!.setBounds(0, 0, drawableX!!.minimumWidth, drawableX!!.minimumHeight)
        drawableM!!.setBounds(0, 0, drawableM!!.minimumWidth, drawableM!!.minimumHeight)
        setOnClickListener(img_back)
        setOnClickListener(ll_wait_for)
        setOnClickListener(tv_wai_for_t_011)
        setOnClickListener(tv_wai_for_t_03_)
        setOnClickListener(tv_wai_for_t_04_)
        mPresenter.ProjectList()
        mPresenter.loadRepositories()
        initSpringView(sv_wait_for)
        tv_wai_for_t_010.text = Html.fromHtml(
            String.format(
                getString(R.string.tip_task1),
                SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))
            )
        )

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            //客户明细
//            ll_unread_news_01 -> startActivity(Intent(this, CustoDetailActivity::class.java))
            tv_wai_for_t_011 -> ProjectListDialog(this, data!!)

            tv_wai_for_t_03_ -> {
                Log.d(TAG, "tv_wai_for_t_03: ");
                tv_wai_for_t_03_.setCompoundDrawables(
                    null,
                    null,
                    if (isOrder1) drawableS else drawableX,
                    null
                )
                tv_wai_for_t_04_.setCompoundDrawables(null, null, drawableM, null)
                sortWay = if (isOrder1) "desc" else "asc"
                isOrder1 = !isOrder1
                sortBy = "overdueNum"
//                sortBy = "showNum"
                isOrder2 = false
                onFRefresh()

            }

            tv_wai_for_t_04_ -> {
                Log.d(TAG, "tv_wai_for_t_04: ");
                tv_wai_for_t_04_.setCompoundDrawables(
                    null,
                    null,
                    if (isOrder2) drawableS else drawableX,
                    null
                )
                tv_wai_for_t_03_.setCompoundDrawables(null, null, drawableM, null)
                sortWay = if (isOrder2) "desc" else "asc"
                isOrder2 = !isOrder2
                sortBy = "toPoolNum"
                isOrder1 = false
                onFRefresh()
            }
            ll_wait_for -> EventBus.getDefault().post(SheetEvent(8))
            else -> {
            }
        }
    }

    //选择项目
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectList(event: Data) {
        tv_wai_for_t_011.text = event.projectName
        projectId = event.projectId
        onFRefresh()
    }

    override fun onFRefresh() {
        super.onFRefresh()
        //刷新
        pageSize = 1
        mBeans.clear()
        mPresenter.loadRepositories()

    }

    override fun onFLoadmore() {
        super.onFLoadmore()
        //加载更多
        pageSize++
        mPresenter.loadRepositories()

    }

    override fun showDataA(data: ProjectToBean.Data) {
        try {
            if (data != null){
                tv_wai_for_t_03.text = "${data.summary.overdueNum}"
                tv_wai_for_t_04.text = "${data.summary.toPoolNum}"
                mBeans.addAll(data.page.records)
                custToAdapter?.refeshData(mBeans)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun ProjectToBean(event: ProjectToBean.Record) {
        if (AppApplication.TYPE == Constants.RESCUE1) return

        val intent = Intent(this, CustoDetailActivity::class.java)
        intent.putExtra("bean", event)
//        startActivity(intent)
    }

    override fun showListData(data: MutableList<Data>) {

        this.data = data

    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> CustomerManageImp.API_CUSTOMER_MGRUSER_PROJECTTO_FOLLOW
            2 -> SystemSettImp.API_SYSTEM_PROJECT_LIST
            else -> CustomerManageImp.API_CUSTOMER_MGRUSER_PROJECT_ONFOLLOW
        }
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        val map = mutableMapOf<String, String>()
        when (url_type) {
            1 -> {
                if (!TextUtils.isEmpty(projectId)) {
                    map["projectId"] = projectId
                }
                map["pageSize"] = "10"
                map["page"] = "$pageSize"

                map["overDate"] =
                    SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))

                map["sortWay"] = sortWay
                if (!TextUtils.isEmpty(sortBy)) {
                    map["sortBy"] = sortBy
                }

                //            逾期的那边sortby 是跟 showNum 的
                //            overdueNum toPoolNum
            }
            2 -> {
            }
            else -> {
            }
        }



        return map
    }

    override fun onSuccessData(url_type: Int, load_type: Int, msg: String, status: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
