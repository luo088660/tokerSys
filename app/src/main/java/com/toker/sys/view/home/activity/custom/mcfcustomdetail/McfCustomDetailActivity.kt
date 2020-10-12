package com.toker.sys.view.home.activity.custom.mcfcustomdetail

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.toker.sys.AppApplication
import com.toker.sys.R
import com.toker.sys.common.Constants
import com.toker.sys.dialog.custom.InvalidTagDialog
import com.toker.sys.dialog.custom.adapter.CustFollowRecordDialog
import com.toker.sys.dialog.my.CallPhoneDialog
import com.toker.sys.mvp.base.BaseActivity
import com.toker.sys.utils.network.params.CustomerManageImp
import com.toker.sys.utils.tools.LogUtils
import com.toker.sys.view.home.activity.adapter.FCustomDetailAdapter
import com.toker.sys.view.home.activity.custom.bean.FCustomDetailTBean
import com.toker.sys.view.home.activity.custom.editcustominfor.EditCustomInforActivity
import com.toker.sys.view.home.activity.custom.event.CustomerInvalid
import com.toker.sys.view.home.activity.custom.newcustom.NewCustomActivity
import com.toker.sys.view.home.fragment.custom.bean.FollowCTBean
import kotlinx.android.synthetic.main.activity_mcf_customr_detail.*
import kotlinx.android.synthetic.main.layout_content_title.*
import kotlinx.android.synthetic.main.layout_f_custom_detail_01.*
import kotlinx.android.synthetic.main.layout_f_custom_detail_02.*
import kotlinx.android.synthetic.main.layout_mcf_custom_detail_01.*
import kotlinx.android.synthetic.main.layout_mcf_custom_detail_02.*
import kotlinx.android.synthetic.main.layout_mcf_custom_recom_03.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat

/**
 * 意向客户
 *
 * 未推荐客户
 *
 * 客户详情
 * @author yyx
 */

class McfCustomDetailActivity :
    BaseActivity<McfCustomDetailContract.View, McfCustomDetailPresenter>(),
    McfCustomDetailContract.View {

    override var mPresenter: McfCustomDetailPresenter = McfCustomDetailPresenter()

    //无效客户 context
    private var invalidReason: String = ""
    private var record: String = ""
    var mBean: FCustomDetailTBean.Data? = null
    private val sdf = SimpleDateFormat("yyyy/MM/dd")
    override fun layoutResID(): Int = R.layout.activity_mcf_customr_detail
    var bean: FollowCTBean.PageData? = null
    override fun initView() {
        EventBus.getDefault().register(this)
        tv_title.text = "客户详情"
        bean = intent.getSerializableExtra("bean") as FollowCTBean.PageData
        LogUtils.d(TAG, "bean:$bean ")
        btn_mcf_custom_02.visibility = GONE
        if (TextUtils.isEmpty(bean!!.status)||bean!!.status.toInt() == 1) {
            ll_mcf_custom_recom_01.visibility = VISIBLE
            ll_mcf_custom_recom_02.visibility = GONE
        } else {
            ll_mcf_custom_recom_01.visibility = VISIBLE
            ll_mcf_custom_recom_02.visibility = GONE
            btn_mcf_datail_04.visibility = GONE
        }
    }

    override fun initData() {
        rv_f_custom_detail.layoutManager = GridLayoutManager(this, 1)
        setOnClickListener(img_back)
        setOnClickListener(btn_mcf_custom_01)
        setOnClickListener(btn_mcf_custom_02)
        setOnClickListener(btn_mcf_datail_01)
        setOnClickListener(btn_mcf_datail_02)
        setOnClickListener(btn_mcf_datail_03)
        setOnClickListener(btn_mcf_datail_04)
        setOnClickListener(btn_mcf_custom_recom_01)
        setOnClickListener(btn_mcf_custom_recom_02)
        ll_mcf_custom_detail_01.visibility = GONE
        tv_mcf_custom_detail_033.text = "意向级别"
        if(AppApplication.TYPE == Constants.RESCUE2){
            ll_mcf_cus.visibility = GONE
            ll_mcf_custom_recom_01.visibility = GONE
            ll_mcf_custom_recom_02.visibility = GONE
        }
    }

    override fun onStart() {
        super.onStart()
        mPresenter.loadRepositories()
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            img_back -> finish()
            //拨打电话
            btn_mcf_custom_01 -> CallPhoneDialog(this, bean!!.phone)
            //新增客户
            btn_mcf_custom_02 -> startActivity(Intent(this, NewCustomActivity::class.java))
            //编辑资料
            btn_mcf_datail_01 -> {
//                EditCustomInforActivity
                val intent = Intent(this, NewCustomActivity::class.java)
                intent.putExtra("type", false)
                intent.putExtra("mBean", mBean)
                startActivity(intent)


            }
            //客户跟进
            btn_mcf_datail_02 -> {
                CustFollowRecordDialog(this, bean!!.name, bean!!.phone)
            }
            //推荐项目
            btn_mcf_datail_03 -> {
                val intent = Intent(this, NewCustomActivity::class.java)
                intent.putExtra("type", true)
                intent.putExtra("mBean", mBean)
                startActivity(intent)
            }
            //标记无效 InvalidTagDialog
            btn_mcf_datail_04 -> InvalidTagDialog(this, bean!!.name)
            //撤销推荐
            btn_mcf_custom_recom_01 -> {
                mPresenter.customerRevocation()
            }
            //推荐其他项目
            btn_mcf_custom_recom_02 -> {
                val intent = Intent(this, NewCustomActivity::class.java)
                intent.putExtra("type", true)
                intent.putExtra("mBean", mBean)
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    //数据展示
    override fun showData(data: FCustomDetailTBean.Data) {

        val ctm = data.intentionCustomerVO
        data.intentionCustomerVO.id = ctm.customerId

        mBean = data
        LogUtils.d(TAG, "mBean:${mBean!!.intentionCustomerVO} ");
        tv_f_custom_01.text = ctm.name
        tv_f_custom_02.text = ctm.phone

        tv_f_custom_03.text = if (ctm.status == "1") "未推荐" else "已推荐"
        btn_mcf_datail_04.visibility = if (ctm.status =="1") VISIBLE else GONE
        tv_f_custom_03.setTextColor(resources.getColor(if (ctm.status != "1") R.color.c_txt_tjchenggong else R.color.c_red_4))
        tv_f_custom_03.setBackgroundColor(resources.getColor(if (ctm.status != "1") R.color.c_bg_tjchenggong else R.color.c_red_5))

        tv_f_custom_04.text = ctm.createTime
        tv_f_custom_05.text = ctm.customerAddress
        tv_f_custom_06.text = ctm.userName
        tv_f_custom_07.text = ctm.creator
        tv_f_custom_08.text = ctm.registerAddress
        tv_f_custom_09.text = ctm.remark
        tv_custo_detail_022.visibility = GONE
        val cusVO = data.reCustomerVO
        val recommendVO = data.recommendVO
        tv_mcf_custom_detail_03.text =  if(cusVO.intentionLevel.isNullOrBlank())"未填写" else "${cusVO?.intentionLevel}级"
        tv_mcf_custom_detail_01.text = if(cusVO.sixNumbers.isNullOrBlank())"未填写" else cusVO?.sixNumbers
        tv_mcf_custom_detail_02.text = if(cusVO.province.isNullOrBlank())"未填写" else cusVO?.province
//        tv_mcf_custom_detail_03.text = if(cusVO.recommendProject.isNullOrBlank())"未填写" else recommendVO?.projectName
        tv_mcf_custom_detail_04.text = if (cusVO.visitTime != null)cusVO.visitTime.replace("-", "/") else ""
        tv_mcf_custom_detail_05.text = if(cusVO.productType.isNullOrBlank())"无" else cusVO.productType
        tv_mcf_custom_detail_06.text = if(cusVO.productAcreage.isNullOrBlank())"无" else cusVO.productAcreage
        tv_mcf_custom_detail_07.text = if(cusVO.houseType.isNullOrBlank())"无" else cusVO.houseType
        tv_mcf_custom_detail_08.text = if(cusVO.purchaseFocus.isNullOrBlank())"无" else cusVO.purchaseFocus

        if (data.customerRecordsVOList.isNullOrEmpty()) {
            tv_f_custom_dt_02.text = "客户跟进记录(0)"
        } else {
            tv_f_custom_dt_02.text = "客户跟进记录(${data.customerRecordsVOList.size})"
            rv_f_custom_detail.adapter = FCustomDetailAdapter(this, data.customerRecordsVOList)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun CustomerInvalid(event: CustomerInvalid) {
        when (event.type) {
            //标记无效客户
            1 -> {
                invalidReason = event.context
                mPresenter.invalidReason()
            }
            //填写客户更近记录
            2 -> {
                record = event.context
                mPresenter.customerFollow()
            }
            else -> {
            }
        }

    }

    override fun showSuccess(i: Int) {
        when (i) {
            2 -> {
                toast("标记无效成功")
                finish()
            }
            3 -> {
                toast("成功跟进")
                mPresenter.loadRepositories()
            }
            2 -> {
                toast("撤销推荐成功")
                finish()
            }
            else -> {
            }
        }
    }

    override fun getUrl(url_type: Int): String {
        return when (url_type) {
            1 -> CustomerManageImp.API_CUST_MANAGE_DETAIL
            2 -> CustomerManageImp.API_CUST_MANAGE_INVALID
            3 -> CustomerManageImp.API_CUST_MANAGE_FOLLOW
            4 -> CustomerManageImp.API_CUST_MANAGE_REVOCATION
            else -> ""
        }
    }

    override fun getParams(url_type: Int, load_type: Int, bundle: Bundle?): Map<String, String> {
        var map = mutableMapOf<String, String>()
        when (url_type) {
            //1.意向客户-未推荐客户
            // 撤销推荐
            1, 4 -> {
                map["id"] = bean!!.id
                map["tableTag"] = bean!!.tableTag
            }
            //标记无效客户
            2 -> {
                map["id"] = bean!!.id
                map["tableTag"] = bean!!.tableTag
                map["invalidReason"] = invalidReason
            }
            //填写客户跟进记录
            3 -> {
                map["id"] = bean!!.id
                map["tableTag"] = bean!!.tableTag
                map["record"] = record
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
