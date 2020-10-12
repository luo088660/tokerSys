package com.toker.sys.view.home.activity.custom.mcfcustomrecom

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.dialog.custom.NCExistedDialog
import com.toker.sys.dialog.custom.NCSubPromptDialog
import com.toker.sys.dialog.my.CallPhoneDialog
import com.toker.sys.dialog.task.ApprTaskResultDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.service.LoaService
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.utils.network.params.SystemSettImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.adapter.FCustomDetailAdapter
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import com.toker.sys.view.home.activity.custom.event.NCSubPrompEvent
import com.toker.sys.view.home.activity.custom.newcustom.NewCustomActivity
import com.toker.sys.view.home.bean.Data
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import kotlinx.android.synthetic.main.activity_mcf_custom_recom.*
import kotlinx.android.synthetic.main.activity_mcf_custom_recom.btn_mcf_custom_01
import kotlinx.android.synthetic.main.activity_mcf_custom_recom.btn_mcf_custom_02
import kotlinx.android.synthetic.main.activity_mcf_customr_detail.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_f_custom_detail_01.*
import kotlinx.android.synthetic.main.layout_f_custom_detail_02.*
import kotlinx.android.synthetic.main.layout_mcf_custom_detail_01.*
import kotlinx.android.synthetic.main.layout_mcf_custom_detail_02.*
import kotlinx.android.synthetic.main.layout_mcf_custom_recom_02.*
import kotlinx.android.synthetic.main.layout_mcf_custom_recom_03.*
import kotlinx.android.synthetic.main.layout_new_custom_02.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

/**
 * 已推荐
 * 客户详情
 *
 * 推荐成功 显示 [撤销推荐]
 * 已推荐 隐藏 [撤销推荐]
 * @author yyx
 */

class McfCustomRecomActivity : BaseActivity<McfCustomRecomContract.View, McfCustomRecomPresenter>(),
    McfCustomRecomContract.View {


    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    override var mPresenter: McfCustomRecomPresenter = McfCustomRecomPresenter()
    //false 意向客户 ture 推荐客户
    var isType = false

    override fun layoutResID(): Int = R.layout.activity_mcf_custom_recom
    var bean: FollowCTBean.PageData? = null
    var isTStatus = 0
    override fun initView() {
        EventBus.getDefault().register(this)
        tv_title.text = "客户详情"
        bean = intent.getSerializableExtra("bean") as FollowCTBean.PageData
        isType = intent.getBooleanExtra("isType", false)
        LogUtils.d(TAG, "bean:$bean ")
        LogUtils.d(TAG, "bean:${bean?.outVisit} ")
        LogUtils.d(TAG, "bean:${bean?.outRecommend}")
        mPresenter.loadRepositories()
    }

    override fun initData() {
        setOnClickListener(img_back)
        setOnClickListener(btn_mcf_custom_recom_01)
        setOnClickListener(btn_mcf_custom_recom_02)
        setOnClickListener(btn_mcf_custom_01)
        setOnClickListener(btn_mcf_custom_02)

        ll_custom_detail1_01.visibility = GONE
        tv_custo_detail_022.visibility = GONE
        btn_mcf_custom_02.visibility = GONE
        tv_custo_detail_011.text = "推荐人"
        if (AppApplication.TYPE == Constants.RESCUE2) {
            ll_mcf_cus_rscon.visibility = GONE
            ll_mcf_custom_detail_btm_.visibility = GONE
        }
        tv_f_custom_044.text = "拓客时间"
        mPresenter.showProjectDate()
        tv_mcf_custom_detail_022.text = "城市"

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            //撤销推荐
            btn_mcf_custom_recom_01 -> {
                NCSubPromptDialog(
                    this,
                    if (isTStatus == 3) "确定重新激活客户【${mBean?.recommendVO?.name}】吗？" else "确定撤销推荐客户【${mBean?.recommendVO?.name}】?",
                    isTStatus
                )
            }
            //推荐其他项目
            btn_mcf_custom_recom_02 -> {

                if ("${btn_mcf_custom_recom_02.text}" == "重新推荐") {
                    //重新推荐项目
                    NCSubPromptDialog(this, "确定重新推荐客户【${mBean?.recommendVO?.name}】吗？", 1)

                } else {
                    val intent = Intent(this, NewCustomActivity::class.java)
                    intent.putExtra("type", true)
                    intent.putExtra("mBean", mBean)
                    startActivity(intent)
                }

            }
            btn_mcf_custom_01 -> CallPhoneDialog(this, mBean!!.recommendVO!!.phone)

            btn_mcf_custom_02 -> startActivity(Intent(this, NewCustomActivity::class.java))
            else -> {
            }
        }
    }

    var mBean: FCustomDetailTBean.Data? = null
    //数据展示
    override fun showData(data: FCustomDetailTBean.Data) {
        val ctm = data.recommendVO!!
//        ctm.id = ctm.recommendId
        mBean = data
        LogUtils.d(TAG, "mBean:$mBean ");
        tv_f_custom_01.text = ctm!!.name
        tv_f_custom_02.text = ctm!!.phone
//        ctm.status = "6"

        when (ctm.status.toInt()) {
            1 -> {
                tv_f_custom_03.setTextColor(resources.getColor(R.color.c_txt_tjchenggong))
                tv_f_custom_03.setBackgroundColor(resources.getColor(R.color.c_bg_tjchenggong))
                tv_f_custom_03.text = "推荐成功"
                btn_mcf_custom_recom_01.visibility = GONE
            }
            2 -> {
                tv_f_custom_03.setTextColor(resources.getColor(R.color.c_txt_yidaofang))
                tv_f_custom_03.setBackgroundColor(resources.getColor(R.color.c_bg_yidaofang))
                tv_f_custom_03.text = "已到访"
                btn_mcf_custom_recom_01.visibility = GONE
            }
            3 -> {
                tv_f_custom_03.setTextColor(resources.getColor(R.color.c_txt_yirenchou))
                tv_f_custom_03.setBackgroundColor(resources.getColor(R.color.c_bg_yirenchou))
                tv_f_custom_03.text = "已认筹"
                ll_mcf_custom_recom_02.visibility = GONE
            }
            4 -> {
                tv_f_custom_03.setTextColor(resources.getColor(R.color.c_txt_yirengou))
                tv_f_custom_03.setBackgroundColor(resources.getColor(R.color.c_bg_yirengou))
                tv_f_custom_03.text = "已认购"
                ll_mcf_custom_recom_02.visibility = GONE
            }
            5 -> {
                tv_f_custom_03.setTextColor(resources.getColor(R.color.c_txt_yiqianyue))
                tv_f_custom_03.setBackgroundColor(resources.getColor(R.color.c_bg_yiqianyue))
                tv_f_custom_03.text = "已签约"
                ll_mcf_custom_recom_02.visibility = GONE
            }
            else -> {
                tv_f_custom_03.setTextColor(resources.getColor(R.color.c_txt_yiguoqi))
                tv_f_custom_03.setBackgroundColor(resources.getColor(R.color.c_bg_yiguoqi))
                tv_f_custom_03.text = "已过期"
                isTStatus = 3
                btn_mcf_custom_recom_01.visibility = GONE
                if (bean?.outVisit == "1") {
                    btn_mcf_custom_recom_01.visibility = VISIBLE
                    btn_mcf_custom_recom_01.text = "激活"
                }
                if (bean?.outRecommend == "1") {
                    btn_mcf_custom_recom_01.visibility = GONE
                    btn_mcf_custom_recom_02.text = "重新推荐"
                }

            }
        }
        tv_f_custom_04.text = ctm!!.createTime
        tv_f_custom_05.text = ctm?.customerAddress
        tv_f_custom_07.text = ctm?.userName
        tv_f_custom_09.text = ctm?.remark

        val cusVO = data.reCustomerVO

        tv_mcf_custom_detail_01.text =
            if (cusVO.sixNumbers.isNullOrBlank()) "未填写" else cusVO?.sixNumbers
        tv_mcf_custom_detail_02.text =
            if (ctm.projectCity.isNullOrBlank()) "未填写" else ctm.projectCity
        tv_mcf_custom_detail_03.text =
            if (cusVO.recommendProject.isNullOrBlank()) "未填写" else cusVO?.recommendProject
        tv_mcf_custom_detail_04.text =
            if (cusVO.visitTime != null) cusVO.visitTime.replace("-", "/") else ""
        tv_mcf_custom_detail_05.text =
            if (cusVO.productType.isNullOrBlank()) "无" else cusVO.productType
        tv_mcf_custom_detail_06.text =
            if (cusVO.productAcreage.isNullOrBlank()) "无" else cusVO.productAcreage
        tv_mcf_custom_detail_07.text = if (cusVO.houseType.isNullOrBlank()) "无" else cusVO.houseType
        tv_mcf_custom_detail_08.text =
            if (cusVO.purchaseFocus.isNullOrBlank()) "无" else cusVO.purchaseFocus

        if (data.customerStateChangeVOList.isNullOrEmpty()) {
            tv_mcf_customm_01.text = "客户状态变更及更新记录(0)"
        } else {
            tv_mcf_customm_01.text = "客户状态变更及更新记录(${data.customerStateChangeVOList.size})"
            rv_mcf_customm_detail.layoutManager = GridLayoutManager(this, 1)

            val beans = mutableListOf<FCustomDetailTBean.CustomerRecordsVO>()
            for (i in data.customerStateChangeVOList) {

                beans.add(
                    FCustomDetailTBean.CustomerRecordsVO(
                        sdf.format(i.changeTime),
                        if (i.content == "已报备") "已推荐" else i.content,
                        i.userName
                    )
                )
            }
            rv_mcf_customm_detail.adapter = FCustomDetailAdapter(this, beans)
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun NCSubPrompEvent(event: NCSubPrompEvent) {
        Log.e(TAG, "event.isTStatus:${event.isTStatus} ");
        Log.e(TAG, "isTStatus:${isTStatus} ");
        when (event.isTStatus) {
            3 -> {
                //TODO 激活用户
                mPresenter.customerActivate()
            }
            1 -> {
                //重新推荐
                mPresenter.customerRecommend()
            }
            else -> {
            }
        }

    }

    override fun showSuccess(data: String) {
        //TODO
        NCExistedDialog(this, data)
    }

    override fun showError(desc: String) {
        if (desc.contains("请联系管理员")) {
            NCExistedDialog(this, "推荐失败,该拓客未认证恒房通")
        } else {
            NCExistedDialog(this, desc)
        }

    }

    private var projectName = ""
    private var projectId = ""
    override fun showProjectDate(data: MutableList<Data>) {

        if (!data.isNullOrEmpty()) {
//            projectName = data[1].projectName
//            projectId = data[1].projectId
        }
    }

    override fun showDatarActivate() {
        toast("激活成功")
        finish()
    }

    override fun showErrorrActivate(desc: String) {
        toast(desc)
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> CustomerManageImp.API_CUST_CUSTOMER_Y_DETAIL
            2 -> CustomerManageImp.API_CUSTOMER_CUSTOMER_ACTIVATE
            3 -> CustomerManageImp.API_CUST_CUSTOMER_ACTIVATE
            4 -> SystemSettImp.API_SYSTEM_PROJECT_LIST
            else -> ""
        }
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        var map = mutableMapOf<String, String>()
        when (url_type) {
//            //1.跟进中客户 4.推荐客户
            1 -> {
                map["id"] = bean!!.id
                map["tableTag"] = bean!!.tableTag
            }
            /* 2->{
                 map["id"] =  bean!!.id
                 map["tableTag"] = bean!!.tableTag
                 map["outVisit"] = "0"
                 map["outRecommend"] = "1"
             }*/
            2, 3 -> {
                map["id"] = bean!!.id
                map["tableTag"] = bean!!.tableTag
                map["outVisit"] = bean!!.outVisit
                map["outRecommend"] = bean!!.outRecommend
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
