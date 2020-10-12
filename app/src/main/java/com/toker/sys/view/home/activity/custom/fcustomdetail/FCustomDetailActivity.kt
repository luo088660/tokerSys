package com.toker.sys.view.home.activity.custom.fcustomdetail

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.Html
import android.text.TextUtils
import android.view.View
import com.alibaba.fastjson.JSON
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.dialog.custom.SelectUserDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.adapter.FCustomDetaYAdapter
import com.toker.sys.view.home.activity.adapter.FCustomDetailAdapter
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import com.toker.sys.view.home.activity.custom.newcustom.NewCustomActivity
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import com.toker.sys.view.home.fragment.custom.bean.SelectUserList
import com.toker.sys.view.home.fragment.custom.event.SelectUserEvent
import kotlinx.android.synthetic.main.activity_f_custom_detail.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_f_custom_detail_01.*
import kotlinx.android.synthetic.main.layout_f_custom_detail_02.*
import kotlinx.android.synthetic.main.layout_mcf_custom_recom_01.*
import kotlinx.android.synthetic.main.layout_mcf_custom_recom_03.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

/**
 * 客户详情
 * @author yyx
 */

class FCustomDetailActivity : BaseActivity<FCustomDetailContract.View, FCustomDetailPresenter>(),
    FCustomDetailContract.View {

    private var tkId: String? = ""
    override var mPresenter: FCustomDetailPresenter = FCustomDetailPresenter()
    private var tkName: String = ""
    var bean: FollowCTBean.PageData? = null
    var page = "1"
    var Userpage = "1"
    override fun layoutResID(): Int = R.layout.activity_f_custom_detail
    private val sdf = SimpleDateFormat("yyyy-MM-dd")
    //TODO type 2.公客池 or 3.无效客户
    private var type: Int = 0

    private var selectUserDialog: SelectUserDialog? = null
    var ctm: FCustomDetailTBean.IntentionCustomerVO? = null
    override fun initView() {
        EventBus.getDefault().register(this)
        tv_title.text = "客户详情"
        bean = intent.getSerializableExtra("bean") as FollowCTBean.PageData
        type = intent.getIntExtra("type", 0)
        LogUtils.d(TAG, "type:$type ");
        LogUtils.d(TAG, "bean:$bean ");
        if (type == 4) {
            mPresenter.customerYDetail()
        } else {
            mPresenter.loadRepositories()

            if (type == 5) {
                ll_f_custom_datail.visibility = GONE
                ll_mcf_custom_recom_02.visibility = GONE
            }

        }



        ll_mcf_custom_detail_btm.visibility =
            if (AppApplication.TYPE == Constants.RESCUE2) GONE else VISIBLE
        if (bean!!.notFollowDays != "0") {
            tv_f_custom_datail.text =
                Html.fromHtml(
                    String.format(
                        getString(R.string.tip_task_day_01),
                        bean!!.notFollowDays
                    )
                )
        } else {
            tv_f_custom_datail.visibility = GONE
        }

    }

    override fun initData() {
        setOnClickListener(img_back)
        setOnClickListener(btn_f_custom_datail)
        rv_f_custom_detail.layoutManager = GridLayoutManager(this, 1)
        when (type) {
            2, 3 -> {
                ll_f_custom_datail.visibility = VISIBLE
                btn_f_custom_datail.visibility = VISIBLE
                btn_f_custom_datail.setOnClickListener {
                    mPresenter.selectUserEvent()
                }
                ll_f_custom_datail_02.visibility = if (type == 3) VISIBLE else GONE
            }
            else -> {
            }
        }

    }

    override fun onClick(v: View?) {
        super.onClick(v)

        when (v) {
            img_back -> finish()
            //分配拓客员
            btn_f_custom_datail -> mPresenter.selectUserEvent()
            //激活
            btn_mcf_custom_recom_01 -> {

            }
            //激活 推荐其他项目
            btn_mcf_custom_recom_02 -> {
                val intent = Intent(this, NewCustomActivity::class.java)
                intent.putExtra("type", true)
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    //展示拓客员列表
    override fun showUserList(records: MutableList<SelectUserList.PageData>) {
        if (selectUserDialog == null) {
            selectUserDialog = SelectUserDialog(this, records)
        } else {
            selectUserDialog?.setData(records)
        }

    }

    //确认拓客员名称
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun recoRd(event: SelectUserList.PageData) {
        this.tkId = event.userId
        selectUserDialog = null
        mPresenter.publicPoCust()

    }

    //获取拓客员列表
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun selectUserEvent(event: SelectUserEvent) {
        this.tkName = event.name

        Userpage= "${event.page}"
        mPresenter.selectUserEvent()
    }

    override fun showAllocation() {
        toast("分配成功")
        finish()
    }

    //type 1,2,3
    @TargetApi(Build.VERSION_CODES.M)
    override fun showData(data: FCustomDetailTBean.Data) {

        layout_tjr_info.visibility = GONE
        ctm = data.intentionCustomerVO
        if (type == 3) {
            ll_f_custom_datail_01.visibility = VISIBLE
            tv_f_custom_datail_01.text = ctm!!.invalidUser
            tv_f_custom_datail_02.text =if (0L == ctm!!.invalidDate ) "暂无数据" else sdf.format(Date(ctm!!.invalidDate))
            tv_f_custom_datail_03.text = if (TextUtils.isEmpty(ctm!!.invalidReason)) "暂无数据" else ctm!!.invalidReason
        }

//        val day = TimeUtils.getDaySub(ctm!!.createTime).toString()
//

        tv_f_custom_01.text = ctm!!.name
        tv_f_custom_02.text = ctm!!.phone

        tv_f_custom_03.text = if (ctm!!.status == "1") "未推荐" else "已推荐"
        tv_f_custom_03.setTextColor(resources.getColor(if (ctm!!.status != "1") R.color.c_txt_tjchenggong else R.color.c_red_4,null))
        tv_f_custom_03.setBackgroundColor(resources.getColor(if (ctm!!.status != "1") R.color.c_bg_tjchenggong else R.color.c_red_5,null))

        tv_f_custom_04.text = ctm!!.createTime
        tv_f_custom_05.text = ctm?.customerAddress
        tv_f_custom_06.text = ctm?.userName
        tv_f_custom_07.text = ctm?.creator
        tv_f_custom_08.text = ctm?.registerAddress
        tv_f_custom_09.text = ctm?.remark
        tv_f_custom_dt_02.text = "客户跟进记录(${data.customerRecordsVOList.size})"
        rv_f_custom_detail.adapter = FCustomDetailAdapter(this, data.customerRecordsVOList)
        btn_f_custom_datail.visibility = if (ctm!!.status == "1"&&type != 1) VISIBLE else GONE

    }

    override fun showErrorData(desc: String) {
        toast(desc)
    }

    //已推荐客户
    @TargetApi(Build.VERSION_CODES.M)
    override fun customerYDetail(data: com.toker.sys.view.home.activity.custom.fcustomdetail.Data) {
        val ctm = data.customer
        tv_f_custom_01.text = ctm?.name
        tv_f_custom_02.text = ctm?.phone

        tv_f_custom_03.text = when (ctm.status.toInt()) {
            1 -> {
                tv_f_custom_03.setTextColor(resources.getColor(R.color.c_txt_tjchenggong,null))
                tv_f_custom_03.setBackgroundColor(resources.getColor(R.color.c_bg_tjchenggong,null))
                btn_mcf_custom_recom_01.visibility = GONE
                "推荐成功"
            }
            2 -> {
                tv_f_custom_03.setTextColor(resources.getColor(R.color.c_txt_yidaofang,null))
                tv_f_custom_03.setBackgroundColor(resources.getColor(R.color.c_bg_yidaofang,null))
                btn_mcf_custom_recom_01.visibility = GONE
                "已到访"
            }
            3 -> {
                tv_f_custom_03.setTextColor(resources.getColor(R.color.c_txt_yirenchou,null))
                tv_f_custom_03.setBackgroundColor(resources.getColor(R.color.c_bg_yirenchou,null))
                "已认筹"
            }
            4 -> {
                tv_f_custom_03.setTextColor(resources.getColor(R.color.c_txt_yirengou,null))
                tv_f_custom_03.setBackgroundColor(resources.getColor(R.color.c_bg_yirengou,null))
                "已认购"
            }
            5 -> {
                tv_f_custom_03.setTextColor(resources.getColor(R.color.c_txt_yiqianyue,null))
                tv_f_custom_03.setBackgroundColor(resources.getColor(R.color.c_bg_yiqianyue,null))
                "已签约"
            }
            else -> {
                tv_f_custom_03.setTextColor(resources.getColor(R.color.c_txt_yiguoqi,null))
                tv_f_custom_03.setBackgroundColor(resources.getColor(R.color.c_bg_yiguoqi,null))
                "已过期"
            }
        }

        tv_f_custom_04.text = sdf.format(Date(ctm!!.createTime))
        tv_f_custom_05.text = ctm?.customerAddress
        tv_f_custom_06.text = ctm?.userName
        tv_f_custom_07.text = ctm?.creator
        tv_f_custom_08.text = ctm?.registerAddress
        tv_f_custom_09.text = ctm?.remark

        tv_f_custom_dt_02.text = "客户状态变更及更新记录(${data.customerStateChanges.size})"
        val recommend = data.customerRecommend
        //被推荐人信息
        tv_mcf_custom_01.text = recommend.sixNumbers
        tv_mcf_custom_02.text = ctm?.customerProvince
        tv_mcf_custom_03.text = ctm?.recommendProject
        tv_mcf_custom_04.text = sdf.format(Date(recommend.visitTime))
        //TODO 测试数据 虚拟
        tv_mcf_custom_05.text = "产品类\n高层；别墅 \n面积段\n小于60㎡\n户型 \n两室 \n购房及关注点\n地段；周边配套；现房；学区；精修房"

        rv_f_custom_detail.adapter = FCustomDetaYAdapter(this, data.customerStateChanges)
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> CustomerManageImp.API_CUST_MANAGE_DETAIL
            2 -> CustomerManageImp.API_CUST_CUSTOMER_ALLOCATION
            3 -> CustomerManageImp.API_CUST_SELECT_USER_LIST
            4 -> CustomerManageImp.API_CUST_CUSTOMER_Y_DETAIL
            else -> ""
        }
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        var map = mutableMapOf<String, String>()

        when (url_type) {
            //1.跟进中客户 4.推荐客户
            1, 4 -> {
                map["id"] = bean!!.id
                map["tableTag"] = bean!!.tableTag
            }
            2 -> {
                val hmap = HashMap<String, String>()
                hmap["id"] = this.bean!!.id
                hmap["tableTag"] = this.bean!!.tableTag
                var list = mutableListOf<Map<String, String>>()
                list.add(hmap)
                map["customerIds"] = JSON.toJSONString(list)
                map["userId"] = this.tkId!!
                map["status"] = if (type == 2) "1" else "2"
            }
            3 -> {
                map["name"] = this.tkName
                map["pageSize"] = "10"
                map["page"] = Userpage
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
